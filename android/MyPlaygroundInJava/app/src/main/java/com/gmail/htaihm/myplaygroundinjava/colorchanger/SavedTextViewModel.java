package com.gmail.htaihm.myplaygroundinjava.colorchanger;

import androidx.annotation.ColorInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

/**
 * A ViewModel that can handle the System Initiated Process Death (indirectly through
 * onSaveInstanceState).
 */
public class SavedTextViewModel extends ViewModel {

  private static final String KEY_SAVED_TEXT = "saved_text";

  // SavedStateHandle has live data support, so we don't need to store the live data here.
  private SavedStateHandle mState;

  public SavedTextViewModel(SavedStateHandle savedStateHandle) {
    mState = savedStateHandle;
  }

  public void setText(String text) {
    mState.set(KEY_SAVED_TEXT, text);
  }

  public LiveData<String> getText() {
    return mState.getLiveData(KEY_SAVED_TEXT);
  }
}
