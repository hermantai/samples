package com.gmail.htaihm.myplaygroundinjava.cancelableasyncloader;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.content.AsyncTaskLoader;
import com.gmail.htaihm.myplaygroundinjava.cancelableasyncloader.CancelableAsyncLoaderActivity.MyViewModel;
import java.util.concurrent.TimeUnit;

public class MyAsyncLoader extends AsyncTaskLoader<String> {

  private static final String TAG = MyAsyncLoader.class.getSimpleName();
  private CancelListener cancelListener;
  private volatile boolean isLoading;

  public MyAsyncLoader(Context context, CancelListener cancelListener) {
    super(context);
    this.cancelListener = cancelListener;
  }

  @Override
  public String loadInBackground() {
    isLoading = true;

    int secondsToLoad = 10;

    long started = System.currentTimeMillis();
    while (true) {
      try {
        Thread.sleep(1000);

        long now = System.currentTimeMillis();

        int secondsPassed = (int) TimeUnit.SECONDS.convert(now - started, TimeUnit.MILLISECONDS);
        if (secondsPassed > secondsToLoad) {
          break;
        }
        Log.i(MyAsyncLoader.class.getSimpleName(),
            String.format("%s left", secondsToLoad - secondsPassed));
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

    if (!isLoading) {
      Log.i(TAG, "MyAsyncLoader.loadInBackground: not loading, still delivery data");
      // This is a bad example. If we are canceled, we should not call deliverResult!
      deliverResult("Cancelled!");
      return "blah";
    }
    return "Done";
  }

  @Override
  public void deliverResult(@Nullable String data) {
    isLoading = false;
    super.deliverResult(data);
  }

  @Override
  protected boolean onCancelLoad() {
    isLoading = false;
    cancelListener.onCancelLoad();
    return super.onCancelLoad();
  }

  public boolean isLoading() {
    return isLoading;
  }

  interface CancelListener {
    void onCancelLoad();
  }
}
