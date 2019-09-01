package com.gmail.htaihm.myplaygroundinjava.cancelableasyncloader;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;

public class MyAsyncLoaderCallbacks implements LoaderCallbacks<String>, MyAsyncLoader.CancelListener {

  private final Context context;
  private final LoaderListener loaderListener;

  private MyAsyncLoader mMyAsyncLoader;

  public MyAsyncLoaderCallbacks(Context context, LoaderListener loaderListener) {
    this.context = context.getApplicationContext();
    this.loaderListener = loaderListener;
  }

  @NonNull
  @Override
  public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
    mMyAsyncLoader = new MyAsyncLoader(context, this);
    return mMyAsyncLoader;
  }

  @Override
  public void onLoadFinished(@NonNull Loader<String> loader, String data) {
    loaderListener.onFinished(data);
  }


  @Override
  public void onLoaderReset(@NonNull Loader<String> loader) {

  }

  @Override
  public void onCancelLoad() {
    loaderListener.onCancelled();
  }

  public interface LoaderListener {

    void onFinished(String data);

    void onCancelled();
  }
}
