package com.gmail.htaihm.myplaygroundinjava.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

public final class DimensionUtil {

  public static int getDimension(Context context, float sizeDp) {
    return getDimension(
        context, sizeDp, TypedValue.COMPLEX_UNIT_DIP);
  }

  private static int getDimension(
      Context context, float sizeDp, int unit) {

    return (int)
        TypedValue.applyDimension(
            unit, sizeDp, context.getResources().getDisplayMetrics());
  }

  private DimensionUtil() {}
}
