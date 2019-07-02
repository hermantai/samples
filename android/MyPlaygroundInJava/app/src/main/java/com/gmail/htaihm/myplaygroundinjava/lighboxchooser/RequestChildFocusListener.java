package com.gmail.htaihm.myplaygroundinjava.lighboxchooser;

import android.view.View;
import android.view.ViewParent;

/**
 * Notifies the implementor of this interface of {@link ViewParent#requestChildFocus} events just
 * after they have happened.
 */
public interface RequestChildFocusListener {
  /**
   * Called after {@link ViewParent#requestChildFocus} events have happened in a {@link
   * RequestChildFocusListenerHost} if the implementor of this interface has added itself as a
   * listener.
   *
   * @param childView the child asking for focus, passed from {@link ViewParent#requestChildFocus}
   * @param focused the actual view that has focus, passed from {@link ViewParent#requestChildFocus}
   */
  void onRequestChildFocus(View childView, View focused);
}


