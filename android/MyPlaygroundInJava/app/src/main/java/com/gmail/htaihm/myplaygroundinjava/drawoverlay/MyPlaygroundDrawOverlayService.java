package com.gmail.htaihm.myplaygroundinjava.drawoverlay;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.gmail.htaihm.myplayground.IMyPlaygroundDrawOverlayService;
import com.gmail.htaihm.myplayground.IMyPlaygroundDrawOverlayService.Stub;
import com.gmail.htaihm.myplaygroundinjava.drawoverlay.DrawOverlayActivity.MyHandlers;

public class MyPlaygroundDrawOverlayService extends Service {

  private static final String TAG = MyPlaygroundDrawOverlayService.class.getSimpleName();

  public MyPlaygroundDrawOverlayService() {
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  private final IMyPlaygroundDrawOverlayService.Stub mBinder = new Stub() {
    @Override
    public void drawOverlay(IBinder windowsToken) throws RemoteException {
      Log.i(TAG, String.format("drawOverlay(%s)", windowsToken));

      new MyHandlers(MyPlaygroundDrawOverlayService.this).drawOverlay(windowsToken);
    }

    @Override
    public void drawWithinApplication(IBinder windowsToken) throws RemoteException {
      new MyHandlers(MyPlaygroundDrawOverlayService.this).onDrayWithinAplication(windowsToken);
    }
  };
}
