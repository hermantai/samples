package com.gmail.htaihm.myplaygroundthirdpartyapp.drawoverlayclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import com.gmail.htaihm.myplayground.IMyPlaygroundDrawOverlayService;
import com.gmail.htaihm.myplaygroundthirdpartyapp.R;
import com.gmail.htaihm.myplaygroundthirdpartyapp.databinding.ActivityDrawOverlayClientBinding;

public class DrawOverlayClientActivity extends AppCompatActivity {

  private static final String TAG = DrawOverlayClientActivity.class.getSimpleName();

  protected IMyPlaygroundDrawOverlayService myPlaygroundDrawOverlayService;

  public static Intent createIntent(Context context) {
    return new Intent(context, DrawOverlayClientActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_draw_overlay_client);
    ActivityDrawOverlayClientBinding binding = DataBindingUtil
        .setContentView(this, R.layout.activity_draw_overlay_client);
    binding.setHandlers(new MyHandlers(this));

    initConnection();
  }

  private void initConnection() {
    if (myPlaygroundDrawOverlayService == null) {
      Intent intent = new Intent(
          "com.gmail.htaihm.myplaygroundinjava.MyPlaygroundDrawOverlayService.BIND");

      intent.setPackage("com.gmail.htaihm.myplaygroundinjava");

      // binding to remote service
      Log.i(TAG,
          "bindService: " + bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE));
    }
  }

  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      Log.d(TAG, "Service Connected");
      myPlaygroundDrawOverlayService = IMyPlaygroundDrawOverlayService.Stub
          .asInterface((IBinder) iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
      Log.d(TAG, "Service Disconnected");
      myPlaygroundDrawOverlayService = null;
    }
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbindService(serviceConnection);
  }

  public class MyHandlers {

    private final Context context;

    public MyHandlers(Context context) {
      this.context = context;
    }

    public void onDrawOverlayByServer() {
      Log.i(TAG, "onDrawOverlayByServer");

      if (myPlaygroundDrawOverlayService != null) {
        View rootView = findViewById(android.R.id.content);
        try {
          myPlaygroundDrawOverlayService.drawOverlay(rootView.getWindowToken());
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
