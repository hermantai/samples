package com.gmail.htaihm.myplaygroundinjava.cancelableasyncloader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import com.gmail.htaihm.myplaygroundinjava.R;
import com.gmail.htaihm.myplaygroundinjava.databinding.ActivityCancelableAsyncLoaderBinding;

public class CancelableAsyncLoaderActivity extends AppCompatActivity implements
    MyAsyncLoaderCallbacks.LoaderListener {

  private static final String TAG = CancelableAsyncLoaderActivity.class.getSimpleName();
  private static final int LOADER_ID = 101;

  private MyAsyncLoader mMyAsyncLoader;
  private ActivityCancelableAsyncLoaderBinding binding;

  public static Intent createIntent(Context context) {
    return new Intent(context, CancelableAsyncLoaderActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "onCreate");
    super.onCreate(savedInstanceState);

    mMyAsyncLoader = (MyAsyncLoader) LoaderManager.getInstance(this)
        .initLoader(LOADER_ID, null, new MyAsyncLoaderCallbacks(this, this));
    Log.i(TAG, "loader: " + mMyAsyncLoader);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_cancelable_async_loader);
    MyViewModel myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

    binding.setHandlers(new MyHandlers());
    binding.setViewmodel(myViewModel);

    if (!mMyAsyncLoader.isLoading()) {
      updateStatus("Not started");
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.i(TAG, "onStart");
  }

  // Implements MyAsyncLoaderCallbacks.LoaderListener

  @Override
  public void onFinished(String data) {
    Log.i(TAG, "onFinished");
    updateStatus(data);
  }

  @Override
  public void onCancelled() {
    Log.i(TAG, "onCancelled");
  }

  private void updateStatus(String status) {
    MyViewModel myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
    myViewModel.status = status;
    binding.invalidateAll();
  }

  // end of implements MyAsyncLoaderCallbacks.LoaderListener

  public static class MyViewModel extends ViewModel {

    public String status;
  }

  public class MyHandlers {

    public void onStartLoaderClicked() {
      updateStatus("Started");
      mMyAsyncLoader.forceLoad();
    }

    public void onCancelLoaderClicked() {
      mMyAsyncLoader.cancelLoad();
    }
  }
}
