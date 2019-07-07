package com.gmail.htaihm.myplaygroundinjava.fillscrollview;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gmail.htaihm.myplaygroundinjava.R;
import com.gmail.htaihm.myplaygroundinjava.utils.DimensionUtil;

/**
 * Make the scroll view filled its child.
 *
 * http://www.curious-creature.com/2010/08/15/scrollviews-handy-trick/
 */
public class FillScrollViewActivity extends AppCompatActivity {

  public static Intent createIntent(Context context) {
    return new Intent(context, FillScrollViewActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fill_scroll_view);

    LinearLayout llTextContainer = findViewById(R.id.llTextContainer);
    LinearLayout llButtonContainer = findViewById(R.id.llButtonContainer);
    Button btnFillText = findViewById(R.id.btnFillText);
    btnFillText.setOnClickListener(v -> {
      llTextContainer.removeView(llButtonContainer);
      for (int i = 0; i < 50; i++) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DimensionUtil.getDimension(this, 2), 0, 0);
        textView.setLayoutParams(params);
        textView.setText("text " + i);
        llTextContainer.addView(textView);
      }

      llTextContainer.addView(llButtonContainer);
    });

    Button btnRestart = findViewById(R.id.btnRestart);
    btnRestart.setOnClickListener(v -> recreate());
  }
}
