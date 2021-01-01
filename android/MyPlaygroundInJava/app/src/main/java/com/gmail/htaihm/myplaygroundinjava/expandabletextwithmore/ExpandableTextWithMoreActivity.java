package com.gmail.htaihm.myplaygroundinjava.expandabletextwithmore;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gmail.htaihm.myplaygroundinjava.R;
import java.util.ArrayList;
import java.util.List;

public class ExpandableTextWithMoreActivity extends AppCompatActivity {

  public static Intent createIntent(Context context) {
    return new Intent(context, ExpandableTextWithMoreActivity.class);
  }

  @RequiresApi(api = VERSION_CODES.O)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_expandable_text_with_more);

    LinearLayout llContainer = findViewById(R.id.llContainer);
    llContainer.addView(getExpandableTextView("Hello short text"));
    llContainer.addView(getExpandableTextView("another short text"));
    llContainer.addView(getExpandableTextView(linesOfText(3)));
    llContainer.addView(getExpandableTextView(linesOfText(5)));
    llContainer.addView(getExpandableTextView(linesOfText(6)));

    // TextView tv = new TextView(this);
    // tv.setText("end");
    // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
    //     LayoutParams.WRAP_CONTENT);
    // tv.setLayoutParams(params);

    // llContainer.addView(tv);
  }

  @RequiresApi(api = VERSION_CODES.O)
  private String linesOfText(int n) {
    List<String> lines = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      lines.add(String.format("%s of %s: line of text", i + 1, n + 1));
    }
    return String.join("\n", lines);
  }

  private View getExpandableTextView(String text) {
    View view = getLayoutInflater().inflate(R.layout.expandable_text_with_more_text_view, findViewById(R.id.llContainer), false);

    TextView tvExpandableTextText = view.findViewById(R.id.tvExpandableTextText);
    tvExpandableTextText.setText(text);
    TextView tvMoreText = view.findViewById(R.id.tvMoreText);
    RelativeLayout rlMoreTextContainer = view.findViewById(R.id.rlMoreTextContainer);

    int collapsedMaxLines = 2;
    int expandedMaxLines = 5;

    view.addOnLayoutChangeListener(
        new OnLayoutChangeListener() {
          @Override
          public void onLayoutChange(
              View v,
              int left,
              int top,
              int right,
              int bottom,
              int oldLeft,
              int oldTop,
              int oldRight,
              int oldBottom) {
            if (v == view) {
              int textLines = tvExpandableTextText.getLineCount();
              Log.i("ExpandableTextWithMore", "v == view, lineCount = " + textLines);
              if (textLines <= collapsedMaxLines) {
                rlMoreTextContainer.setVisibility(View.GONE);
                tvMoreText.setOnClickListener(null);
                tvMoreText.setClickable(false);
              } else {
                OnClickListener onClickListener =
                    unused -> {
                      if (tvExpandableTextText.getMaxLines() == collapsedMaxLines) {
                        // Expand the text
                        tvExpandableTextText.setMaxLines(expandedMaxLines);
                        rlMoreTextContainer.setVisibility(View.GONE);
                      }
                    };
                tvExpandableTextText.setMaxLines(collapsedMaxLines);
                rlMoreTextContainer.setVisibility(View.VISIBLE);
                tvMoreText.setOnClickListener(onClickListener);
              }
              view.removeOnLayoutChangeListener(this);
            }
          }
        });
    return view;
  }
}