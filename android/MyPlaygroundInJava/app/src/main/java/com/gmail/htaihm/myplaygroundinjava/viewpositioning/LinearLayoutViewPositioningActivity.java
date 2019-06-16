package com.gmail.htaihm.myplaygroundinjava.viewpositioning;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.MarginLayoutParamsCompat;
import com.gmail.htaihm.myplaygroundinjava.R;

public class LinearLayoutViewPositioningActivity extends AppCompatActivity {

  public static Intent createIntent(Context context) {
    return new Intent(context, LinearLayoutViewPositioningActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_linear_layout_view_positioning);

    ImageView imagePlayground = findViewById(R.id.imagePlayground);
    imagePlayground.setImageResource(R.drawable.playground);

    LayoutParams layoutParams = imagePlayground.getLayoutParams();
    if (layoutParams instanceof FrameLayout.LayoutParams) {
      FrameLayout.LayoutParams relativeLp = (FrameLayout.LayoutParams) layoutParams;
      MarginLayoutParamsCompat.setMarginEnd(relativeLp, getDimension(imagePlayground, 20));
      relativeLp.topMargin = getDimension(imagePlayground, 20);

      imagePlayground.setLayoutParams(layoutParams);
    }

  }

  public static int getDimension(View view, float sizeDp) {
    return getDimension(
        view, sizeDp, TypedValue.COMPLEX_UNIT_DIP);
  }

  private static int getDimension(
      View view, float sizeDp, int defaultUnit) {

        return (int)
            TypedValue.applyDimension(
                defaultUnit, sizeDp, view.getResources().getDisplayMetrics());
  }
}
