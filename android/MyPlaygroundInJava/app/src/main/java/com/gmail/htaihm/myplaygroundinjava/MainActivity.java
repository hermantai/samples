package com.gmail.htaihm.myplaygroundinjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    RecyclerView rvMainLlist = findViewById(R.id.rvMainList);
    rvMainLlist.setLayoutManager(new LinearLayoutManager(this));
    rvMainLlist.setAdapter(new MainListItemAdapter());
  }

  class MainListItemAdapter extends RecyclerView.Adapter<MainListItemViewHolder> {

    @NonNull
    @Override
    public MainListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
      View view = layoutInflater.inflate(R.layout.main_list_item, parent, false);
      return new MainListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListItemViewHolder holder, int position) {
      MainListItem mainListItem = MainListItems.MAIN_LIST_ITEMS.get(position);
      holder.bindItem(mainListItem);
    }

    @Override
    public int getItemCount() {
      return MainListItems.MAIN_LIST_ITEMS.size();
    }
  }

  class MainListItemViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

    private TextView mTvMainListItem;
    private MainListItem mMainListItem;

    public MainListItemViewHolder(@NonNull View itemView) {
      super(itemView);

      mTvMainListItem = itemView.findViewById(R.id.tvMainListItem);
      itemView.setOnClickListener(this);
    }

    public void bindItem(MainListItem mainListItem) {
      mTvMainListItem.setText(mainListItem.getTitle());
      mMainListItem = mainListItem;
    }

    @Override
    public void onClick(View v) {
      startActivity(mMainListItem.createIntent(MainActivity.this));
    }
  }

}
