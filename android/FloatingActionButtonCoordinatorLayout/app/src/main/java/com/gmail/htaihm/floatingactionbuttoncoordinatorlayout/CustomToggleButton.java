package com.gmail.htaihm.floatingactionbuttoncoordinatorlayout;


import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.widget.ToggleButton;

@CoordinatorLayout.DefaultBehavior(CustomToggleButtonBehavior.class)
public class CustomToggleButton extends ToggleButton {
    public CustomToggleButton(Context context) { super(context); }

    public CustomToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}