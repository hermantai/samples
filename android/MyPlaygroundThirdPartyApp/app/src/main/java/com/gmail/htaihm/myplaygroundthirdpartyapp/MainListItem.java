package com.gmail.htaihm.myplaygroundthirdpartyapp;

import android.content.Context;
import android.content.Intent;
import androidx.arch.core.util.Function;

public class MainListItem {
  private final String mTitle;
  private final Function<Context, Intent> mIntentCreator;

  public MainListItem(String title, Function<Context, Intent> intentCreator) {
    mTitle = title;
    mIntentCreator =intentCreator;
  }

  public String getTitle() {
    return mTitle;
  }

  public Intent createIntent(Context context) {
    return mIntentCreator.apply(context);
  }
}