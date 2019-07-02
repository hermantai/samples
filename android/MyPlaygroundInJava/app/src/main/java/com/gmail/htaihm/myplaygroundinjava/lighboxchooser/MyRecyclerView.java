package com.gmail.htaihm.myplaygroundinjava.lighboxchooser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MyRecyclerView extends RecyclerView implements RequestChildFocusListenerHost {

  private final List<RequestChildFocusListener> requestChildFocusListeners = new ArrayList<>();

  public MyRecyclerView(Context context) {
    super(context);
  }

  public MyRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void requestChildFocus(View child, View focused) {
    super.requestChildFocus(child, focused);

    for (int i = requestChildFocusListeners.size() - 1; i >= 0; i--) {
      requestChildFocusListeners.get(i).onRequestChildFocus(child, focused);
    }
  }

  // Implement RequestChildFocusListenerHost

  @Override
  public void addRequestChildFocusListener(RequestChildFocusListener listener) {
    if (!requestChildFocusListeners.contains(listener)) {
      requestChildFocusListeners.add(listener);
    }
  }

  @Override
  public void removeRequestChildFocusListener(RequestChildFocusListener listener) {
    requestChildFocusListeners.remove(listener);
  }
}
