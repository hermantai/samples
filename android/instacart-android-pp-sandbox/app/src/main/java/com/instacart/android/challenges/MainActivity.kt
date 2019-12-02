package com.instacart.android.challenges

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.instacart.android.challenges.network.NetworkService
import com.instacart.android.challenges.network.OrdersResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var svc : NetworkService
    private var disposable : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()

        viewModel = ViewModelProviders.of(this).get()
        viewModel.setStateUpdateListener(object : MainActivityViewModel.UpdateListener {
            override fun onUpdate(state: ItemListViewState) = renderItemList(state)
        })

        svc = NetworkService()


    }

    override fun onResume() {
        super.onResume()

        disposable = svc.api.fetchOrdersObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap{ ordersResponse ->
                Observable.fromArray(ordersResponse.orders.map{svc.api.fetchOrderByIdObservable(it)})
            }
            .subscribe(
                {fetchOrderByIdObservables -> Observable.merge(fetchOrderByIdObservables)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {orderResponse -> adapter.update(
                            orderResponse.items.map { deliveryItem ->
                                ItemRow(deliveryItem.name, deliveryItem.count) })},
                        {error -> Log.i("MainActivity", "Error: " + error)}
                    )
                },
                {error -> Log.i("MainActivity", "Error: " + error)})
    }

    override fun onStop() {
        super.onStop()

        disposable?.dispose()
    }

    private fun renderItemList(state: ItemListViewState) {
        adapter.update(state.items)
    }

    private fun bindViews() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ItemAdapter()
        recyclerView.adapter = adapter
    }
}
