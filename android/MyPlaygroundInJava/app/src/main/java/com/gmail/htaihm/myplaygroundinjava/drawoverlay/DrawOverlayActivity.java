package com.gmail.htaihm.myplaygroundinjava.drawoverlay;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import com.gmail.htaihm.myplaygroundinjava.R;
import com.gmail.htaihm.myplaygroundinjava.databinding.ActivityDrawOverlayBinding;

public class DrawOverlayActivity extends AppCompatActivity {

  private static final String TAG = DrawOverlayActivity.class.getSimpleName();

  public static Intent createIntent(Context context) {
    return new Intent(context, DrawOverlayActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityDrawOverlayBinding binding = DataBindingUtil
        .setContentView(this, R.layout.activity_draw_overlay);
    binding.setHandlers(new MyHandlers(this));

    Log.i(TAG, "onCreate(): My main thread is " + android.os.Process.myTid());
    Log.i(TAG, "onCreate(): My process id is " + android.os.Process.myPid());
  }

  public static class MyHandlers {

    private final Context context;
    private final Handler handler = new Handler(Looper.getMainLooper());
    public MyHandlers(Context context) {
      this.context = context;
    }

    public void onDrawOverlay() {
      drawOverlay(null);
    }

    public void drawOverlay(@Nullable IBinder windowToken) {
      handler.post(() -> onDrawOverlayHelper(windowToken, /* overlay= */ true));
    }

    public void onDrawWithinApplication(@Nullable IBinder windowToken) {
      handler.post(() -> onDrawOverlayHelper(windowToken, /* overlay= */ false));
    }

    public void onDrawWithinApplicationDelayed(@Nullable IBinder windowToken) {
      handler.postDelayed(() -> onDrawOverlayHelper(windowToken, /* overlay= */ false), 10_000);
    }

    public void onDrawOverlayHelper(@Nullable IBinder windowToken, boolean overlay) {
      Log.i(TAG, "onDrawOverlayHelper");
      Log.i(TAG, "My main thread is " + android.os.Process.myTid());
      Log.i(TAG, "My process id is " + android.os.Process.myPid());

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // Show alert dialog to the user saying a separate permission is needed
        if (!Settings.canDrawOverlays(context)) {
          Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
              Uri.parse("package:" + context.getPackageName()));
          context.startActivity(intent);
        }

        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        RelativeLayout parentLayout = new RelativeLayout(context);

        ImageView chatHead = new ImageView(context);
        chatHead.setImageResource(R.drawable.ic_chat_head);
        // Cannot use onLongClickListener because we are using OnTouchListener
//        chatHead.setOnLongClickListener(v -> {
//          wm.removeView(parentLayout);
//          return true;
//        });

        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT);
        relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        WindowManager.LayoutParams windowManagerLayoutParams;

        // Explanation of the difference between types can be found:
        // https://www.jianshu.com/p/98eb2f70e3a5.
        if (windowToken == null) {
          windowManagerLayoutParams = new WindowManager.LayoutParams(
              WindowManager.LayoutParams.WRAP_CONTENT,
              WindowManager.LayoutParams.WRAP_CONTENT,
              overlay ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY: WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
              WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
              PixelFormat.TRANSLUCENT);
        } else {
          Log.i(TAG, "onDrawOverlayHelper, windows layout params with token: " + windowToken);
          windowManagerLayoutParams = new WindowManager.LayoutParams(
              WindowManager.LayoutParams.WRAP_CONTENT,
              WindowManager.LayoutParams.WRAP_CONTENT,
              overlay ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY: WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
              WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
              PixelFormat.TRANSLUCENT);
          windowManagerLayoutParams.token = windowToken;
        }

        windowManagerLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowManagerLayoutParams.x = 0;
        windowManagerLayoutParams.y = 100;

        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
          private int initialX;
          private int initialY;
          private float initialTouchX;
          private float initialTouchY;
          private long action_down_time;

          @Override
          public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
              case MotionEvent.ACTION_DOWN:
                initialX = windowManagerLayoutParams.x;
                initialY = windowManagerLayoutParams.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                action_down_time = System.currentTimeMillis();
                return true;
              case MotionEvent.ACTION_UP:
                if (clicked(event) && System.currentTimeMillis() - action_down_time < 1000) {
                  Log.e(TAG, "It's a single click ! ");
                }
                //long click triggered if held for at least 1 sec (1000ms)
                if (clicked(event) && System.currentTimeMillis() - action_down_time > 1000) {
                  Log.e(TAG, "It's a long click ! ");
                  wm.removeView(parentLayout);
                }
                return true;
              case MotionEvent.ACTION_MOVE:
                windowManagerLayoutParams.x = initialX
                    + (int) (event.getRawX() - initialTouchX);
                windowManagerLayoutParams.y = initialY
                    + (int) (event.getRawY() - initialTouchY);
                wm.updateViewLayout(parentLayout, windowManagerLayoutParams);
                return true;
            }
            return false;
          }

          private boolean clicked(MotionEvent event) {
            return (Math.abs(initialTouchX - event.getRawX()) < 5) && (
                Math.abs(initialTouchY - event.getRawY()) < 5);
          }
        });

        parentLayout.addView(chatHead, relativeLayoutParams);
        wm.addView(parentLayout, windowManagerLayoutParams);
      }
    }

  }
}
