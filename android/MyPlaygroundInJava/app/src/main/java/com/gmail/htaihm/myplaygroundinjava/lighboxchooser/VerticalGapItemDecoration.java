package com.gmail.htaihm.myplaygroundinjava.lighboxchooser;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.State;

/**
 * Item decoration impl that adds a spacer on the bottom of each item (except the last item) in a
 * RecyclerView.
 */
public class VerticalGapItemDecoration extends RecyclerView.ItemDecoration {

  private final int verticalGapPx;

  public VerticalGapItemDecoration(int verticalGapPx) {
    this.verticalGapPx = verticalGapPx;
  }

  @Override
  public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
    if (recyclerView.getChildAdapterPosition(view)
        != recyclerView.getAdapter().getItemCount() - 1) {
      rect.bottom = verticalGapPx;
    }
  }
}
