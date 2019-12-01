/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.cheesefinder

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.raywenderlich.android.cheesefinder.database.Cheese
import com.raywenderlich.android.cheesefinder.database.CheeseDatabase
import com.raywenderlich.android.cheesefinder.database.CheeseUtil
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cheeses.*

abstract class BaseSearchActivity : AppCompatActivity() {

  protected lateinit var cheeseSearchEngine: CheeseSearchEngine
  private val cheeseAdapter = CheeseAdapter()
  private val compositeDisposable = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cheeses)

    list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
    list.adapter = cheeseAdapter

    cheeseSearchEngine = CheeseSearchEngine(this@BaseSearchActivity)

    val initialLoadDisposable = loadInitialData(this@BaseSearchActivity).subscribe()
    compositeDisposable.add(initialLoadDisposable)
  }

  override fun onDestroy() {
    super.onDestroy()
    CheeseDatabase.destroyInstance()
    compositeDisposable.clear()
  }

  protected fun showProgress() {
    progressBar.visibility = VISIBLE
  }

  protected fun hideProgress() {
    progressBar.visibility = GONE
  }

  protected fun showResult(result: List<Cheese>) {
    if (result.isEmpty()) {
      Toast.makeText(this, R.string.nothing_found, Toast.LENGTH_SHORT).show()
    }
    cheeseAdapter.cheeses = result
  }

  private fun loadInitialData(context: Context): Flowable<List<Long>> {
    return Maybe.fromAction<List<Long>> {

      val database = CheeseDatabase.getInstance(context = context).cheeseDao()

      val cheeseList = arrayListOf<Cheese>()
      for (cheese in CheeseUtil.CHEESES) {
        cheeseList.add(Cheese(name = cheese))
      }
      database.insertAll(cheeseList)

    }.toFlowable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnComplete {
          Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_LONG).show()
        }
        .doOnError {
          Toast.makeText(context, context.getString(R.string.error_inserting_cheeses), Toast.LENGTH_LONG).show()
        }
  }
}