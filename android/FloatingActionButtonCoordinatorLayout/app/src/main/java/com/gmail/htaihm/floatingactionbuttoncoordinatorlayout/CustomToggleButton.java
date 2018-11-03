package com.gmail.htaihm.floatingactionbuttoncoordinatorlayout;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class CustomToggleButton extends ToggleButton implements CoordinatorLayout.AttachedBehavior {
  public CustomToggleButton(Context context) {
    super(context);
  }

  public CustomToggleButton(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @NonNull
  @Override
  public CoordinatorLayout.Behavior getBehavior() {
    // this is the preferred way to attach a behavior to a view if we want to attach it automatically
    // https://medium.com/@zoha131/coordinatorlayout-behavior-basic-fd9c10d3c6e3
    return new CustomToggleButtonBehavior();
  }
}