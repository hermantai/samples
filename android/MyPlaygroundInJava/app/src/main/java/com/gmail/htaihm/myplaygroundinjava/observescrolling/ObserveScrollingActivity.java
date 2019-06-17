package com.gmail.htaihm.myplaygroundinjava.observescrolling;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gmail.htaihm.myplaygroundinjava.R;

/**
 * https://stackoverflow.com/questions/30295827/detect-scroll-view-reaching-its-top-android
 */
public class ObserveScrollingActivity extends AppCompatActivity {

  private static final String TAG = ObserveScrollingActivity.class.getSimpleName();

  public static Intent createIntent(Context context) {
    return new Intent(context, ObserveScrollingActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_observe_scrolling);

    ScrollView scrollView = findViewById(R.id.scrollView);
    View lastElementInView = scrollView.getChildAt(scrollView.getChildCount() - 1);
    ImageView imagePlayground = findViewById(R.id.scrollingIndicator);

    final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
        ViewTreeObserver.OnScrollChangedListener() {

          @Override
          public void onScrollChanged() {
            Log.i(TAG, String.format(
                "scrolly = %s, height = %s, bottom = %s", scrollView.getScrollY(),
                scrollView.getHeight(), lastElementInView.getBottom()));

            if (scrollView.getScrollY() + scrollView.getHeight() >= lastElementInView.getBottom()) {
              imagePlayground.setVisibility(View.GONE);
            } else {
              imagePlayground.setVisibility(View.VISIBLE);
            }
          }
        };

    scrollView.getViewTreeObserver().addOnScrollChangedListener(onScrollChangedListener);

  }
}
