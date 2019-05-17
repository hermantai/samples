package com.gmail.htaihm.myplaygroundinjava.colorchanger;

import androidx.annotation.ColorInt;
import androidx.lifecycle.ViewModel;

/** A simple {@link ViewModel} that stores a color resource */
public class ColorChangerViewModel extends ViewModel {

  private int mColorResource = 0xfff;

  public void setColorResource(@ColorInt int colorResource) {
    mColorResource = colorResource;
  }

  @ColorInt
  public int getColorResource() {
    return mColorResource;
  }
}
