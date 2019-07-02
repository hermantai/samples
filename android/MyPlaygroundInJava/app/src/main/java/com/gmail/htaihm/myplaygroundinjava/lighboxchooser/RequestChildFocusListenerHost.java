package com.gmail.htaihm.myplaygroundinjava.lighboxchooser;

import android.view.ViewParent;

/**
 * The interface for a {@link ViewParent} that allows its {@link ViewParent#requestChildFocus} calls
 * to be listened to.
 */
public interface RequestChildFocusListenerHost {
  void addRequestChildFocusListener(RequestChildFocusListener listener);

  void removeRequestChildFocusListener(RequestChildFocusListener listener);
}
