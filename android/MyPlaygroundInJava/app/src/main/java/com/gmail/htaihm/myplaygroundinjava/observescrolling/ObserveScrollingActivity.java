package com.gmail.htaihm.myplaygroundinjava.observescrolling;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gmail.htaihm.myplaygroundinjava.R;
import com.gmail.htaihm.myplaygroundinjava.utils.DimensionUtil;

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

    populateContent(Color.argb(255, 0, 0, 0));

    findViewById(R.id.btnScrollToBottom).setOnClickListener(
        v -> scrollViewToBottom(getString(R.string.scroll_view_tag), /* durationInMs= */ 500));
    findViewById(R.id.btnScrollToBottomDefault).setOnClickListener(
        v -> scrollViewToBottom(getString(R.string.scroll_view_tag), /* durationInMs= */ 0));

    scrollView.setFadingEdgeLength(500);
    Button btnClickFade = findViewById(R.id.btnClickFade);
    btnClickFade.setOnClickListener(new View.OnClickListener() {
      private boolean fadeOff = true;

      @Override
      public void onClick(View v) {
        fadeOff = !fadeOff;

        scrollView.setVerticalFadingEdgeEnabled(fadeOff);
        btnClickFade.setText(fadeOff ? "Fade off" : "Fade on");
      }
    });

    Button btnFlipColor = findViewById(R.id.btnFlipColor);
    btnFlipColor.setOnClickListener(new View.OnClickListener() {
      private boolean flipped = false;

      @Override
      public void onClick(View v) {
        flipped = !flipped;

        if (flipped) {
          flipToDark();
        } else {
          flipToLight();
        }
      }

      private void flipToDark() {
        scrollView.setBackgroundColor(Color.argb(255, 0, 0, 0));
        populateContent(Color.argb(255, 255, 255, 255));
      }

      private void flipToLight() {
        scrollView.setBackgroundColor(Color.argb(255, 255, 255, 255));
        populateContent(Color.argb(255, 0, 0, 0));
      }
    });

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

  private void populateContent(int color) {
    LinearLayout llContentContainer = findViewById(R.id.llContentContainer);
    llContentContainer.removeAllViews();
    for (int i = 0; i < 50; i++) {
      TextView textView = new TextView(this);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
          LayoutParams.WRAP_CONTENT);
      params.setMargins(0, DimensionUtil.getDimension(this, 2), 0, 0);
      textView.setLayoutParams(params);
      textView.setText("text " + i);
      textView.setTextColor(color);
      llContentContainer.addView(textView);
    }
  }

  private void scrollViewToBottom(String tag, long durationInMs) {
    View rootView = getWindow().getDecorView().getRootView();

    if (rootView == null) {
      Log.i(TAG, "Root view not found");
      return;
    }

    View foundView = rootView.findViewWithTag(tag);
    if (foundView == null) {
      Log.i(TAG, "View with tag " + tag + " not found");
      return;
    }

    if (!(foundView instanceof ScrollView)) {
      Log.i(TAG, "foundView is not a ScrollView");
      return;
    }

    ScrollView foundScrollView = (ScrollView) foundView;

    if (foundScrollView.getChildCount() == 0) {
      Log.i(TAG, "foundScrollView getChildCount == 0");
      return;
    }

    View lastChild = foundScrollView.getChildAt(foundScrollView.getChildCount() - 1);
    int scrollViewBottom = lastChild.getBottom() + foundScrollView.getPaddingBottom();
    int currentPosition = foundScrollView.getScrollY() + foundScrollView.getHeight();
    int delta = scrollViewBottom - currentPosition;

    if (durationInMs > 0) {
      Log.i(TAG,
          String.format("lastChild bottom: %s, paddingBottom: %s, scrollViewBottom: %s",
              lastChild.getBottom(), foundScrollView.getPaddingBottom(), scrollViewBottom));
      // foundScrollView.smoothScrollBy is not smooth in practice, so we create our own scrolling.
      ObjectAnimator.ofInt(foundScrollView, "scrollY", scrollViewBottom)
          .setDuration(durationInMs)
          .start();
    } else {
      // Fallback to foundScrollView.smoothScrollBy when durationInMs
      // is not set, so the scrolling happens no matter it's smooth or not.
      foundScrollView.smoothScrollBy(0, delta);
    }
  }
}
