package com.gmail.htaihm.myplaygroundinjava.expandlistwithanimation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class ExpandListWithAnimationViewModel extends ViewModel {
  private static final String KEY_SAVED_TOGGLE_STATE = "toggle_state";
  private static final String KEY_SAVED_ANIMATE_STATE = "animate_state";

  // SavedStateHandle has live data support, so we don't need to store the live data here.
  private SavedStateHandle mState;

  public ExpandListWithAnimationViewModel(SavedStateHandle savedStateHandle) {
    mState = savedStateHandle;
  }

  public void toggle() {
    mState.set(KEY_SAVED_TOGGLE_STATE, !getExpanded().getValue());
  }

  public void setAnimate(boolean animate) {
    mState.set(KEY_SAVED_ANIMATE_STATE, animate);
  }

  public LiveData<Boolean> getExpanded() {
    return mState.getLiveData(KEY_SAVED_TOGGLE_STATE, false);
  }

  public boolean getAnimate() {
    return mState.get(KEY_SAVED_ANIMATE_STATE);
  }
}