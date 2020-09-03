package com.gmail.htaihm.myplaygroundinjava.expandlistwithanimation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;
import com.gmail.htaihm.myplaygroundinjava.R;
import com.gmail.htaihm.myplaygroundinjava.colorchanger.SavedTextViewModel;

public class ExpandListWithAnimationActivity extends AppCompatActivity {

  private Button expandButton;
  private LinearLayout contentList;

  public static Intent createIntent(Context context) {
    return new Intent(context, ExpandListWithAnimationActivity.class);
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_expand_list_with_animation);

    expandButton = findViewById(R.id.expand_button);
    contentList = findViewById(R.id.content_list);

    ExpandListWithAnimationViewModel expandListWithAnimationViewModel = ViewModelProviders
        .of(this, new SavedStateViewModelFactory(getApplication(), this))
        .get(ExpandListWithAnimationViewModel.class);

    // make sure we don't show the animation when we just show the screen.
    expandListWithAnimationViewModel.setAnimate(false);
    expandListWithAnimationViewModel.getExpanded().observe(this, expanded -> {
      Log.i("ExpandListWithAnimation", "observe expanded: " + expanded);

      setExpandedState(expanded);
    });

    expandButton.setOnClickListener(v -> {
      toggle();
    });
  }

  @Override
  protected void onPostResume() {
    Log.i("ExpandListWithAnimation", "onPostResume");
    super.onPostResume();

    ExpandListWithAnimationViewModel expandListWithAnimationViewModel = ViewModelProviders
        .of(this, new SavedStateViewModelFactory(getApplication(), this))
        .get(ExpandListWithAnimationViewModel.class);
    // make sure we don't show the animation when we just show the screen.
    expandListWithAnimationViewModel.setAnimate(false);
  }

  private void toggle() {
    ExpandListWithAnimationViewModel expandListWithAnimationViewModel = ViewModelProviders
        .of(this, new SavedStateViewModelFactory(getApplication(), this))
        .get(ExpandListWithAnimationViewModel.class);
    expandListWithAnimationViewModel.setAnimate(true);
    expandListWithAnimationViewModel.toggle();
  }

  private void setExpandedState(boolean expanded) {
    Log.i("ExpandListWithAnimation", "setExpandedState: " + expanded);

    ExpandListWithAnimationViewModel expandListWithAnimationViewModel = ViewModelProviders
        .of(this, new SavedStateViewModelFactory(getApplication(), this))
        .get(ExpandListWithAnimationViewModel.class);
    expandButton.setText(expanded ? "Collapse" : "Expand");

    boolean animate = expandListWithAnimationViewModel.getAnimate();
    View rootView = contentList.getRootView();
    int toHeight;
    if (expanded) {
      int widthSpec =
          MeasureSpec.makeMeasureSpec(
              rootView.getWidth() - rootView.getPaddingLeft() - rootView.getPaddingRight(), MeasureSpec.EXACTLY);
      int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
      contentList.measure(widthSpec, heightSpec);
      toHeight = contentList.getMeasuredHeight();
    } else {
      toHeight = 0;
    }
    int fromHeight;
    if (expanded) {
      fromHeight = 0;
    } else {
      fromHeight = contentList.getHeight();
    }

    if (animate) {
      getListHeightScaleAnimator(fromHeight, toHeight).start();
      getListFadingAnimator(expanded).start();
    } else {
      ViewGroup.LayoutParams layoutParams = contentList.getLayoutParams();
      if (expanded && rootView.getMeasuredWidth() == 0) {
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
      } else {
        layoutParams.height = toHeight;
      }
      contentList.setLayoutParams(layoutParams);
      contentList.setAlpha(expanded ? 1f : 0f);
    }
  }

  private ValueAnimator getListHeightScaleAnimator(int fromHeight, int toHeight) {
    ValueAnimator valueAnimation = ValueAnimator.ofInt(fromHeight, toHeight);
    valueAnimation.addUpdateListener(
        valueAnimator -> {
          int interpolatedHeight = (Integer) valueAnimator.getAnimatedValue();
          ViewGroup.LayoutParams layoutParams = contentList.getLayoutParams();
          layoutParams.height = interpolatedHeight;
          contentList.setLayoutParams(layoutParams);
        });
    valueAnimation.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    if (VERSION.SDK_INT <= VERSION_CODES.KITKAT) {
      // if (getParent() instanceof View) {
      //   View parentView = (View) getParent();
      //   valueAnimation.addUpdateListener(
      //       animation -> {
      //         // On Kitkat, there are situations where the parent view wont correctly invalidate the
      //         // parts of the screen touched by our animation, leaving artifacts. Invalidate the
      //         // whole view to work around that.
      //         parentView.invalidate();
      //       });
      // }
      valueAnimation.addUpdateListener(
          animation -> {
            // On Kitkat, there are situations where the parent view wont correctly invalidate the
            // parts of the screen touched by our animation, leaving artifacts. Invalidate the
            // whole view to work around that.
            // parentView.invalidate();
            contentList.getRootView().invalidate();
          });
    }
    return valueAnimation;
  }

  private ValueAnimator getListFadingAnimator(boolean isExpand) {
    ObjectAnimator fadeInWhatsNewAnimation =
        ObjectAnimator.ofFloat(contentList, View.ALPHA, isExpand ? 1f : 0f);
    fadeInWhatsNewAnimation.setDuration(
        getResources().getInteger(android.R.integer.config_shortAnimTime));
    return fadeInWhatsNewAnimation;
  }
}