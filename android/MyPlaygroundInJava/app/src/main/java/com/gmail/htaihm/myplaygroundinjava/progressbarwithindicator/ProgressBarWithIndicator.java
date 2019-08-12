package com.gmail.htaihm.myplaygroundinjava.progressbarwithindicator;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import com.gmail.htaihm.myplaygroundinjava.R;

public class ProgressBarWithIndicator extends AppCompatActivity {

  static final int MAX_PROGRESS_OFFSET = 85;

  private ProgressBar myProgressBar;
  private Guideline indicatorConstraintGuideline;

  private LinearLayout indicatorContainer;
  private TextView indicatorText;
  private TextView usedAmountText;

  private float maxAmount = 1000;
  private float usedAmount = 800;

  public static Intent createIntent(Context context) {
    return new Intent(context, ProgressBarWithIndicator.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_progress_bar_with_indicator);

    usedAmountText = findViewById(R.id.used_amount_text);
    myProgressBar = findViewById(R.id.my_progress_bar);
    indicatorConstraintGuideline = findViewById(R.id.indicator_constraint_guideline);
    indicatorContainer = findViewById(R.id.indicator_container);
    indicatorText = findViewById(R.id.indicator_text);

    Button addUsedButton = findViewById(R.id.add_used_button);
    addUsedButton.setOnClickListener(v -> {
      usedAmount += 100;
      bindData();
    });
    Button minusUsedButton = findViewById(R.id.minus_used_button);
    minusUsedButton.setOnClickListener(v -> {
      usedAmount -= 100;
      bindData();
    });

    bindData();
  }

  private void bindData() {
    // If the used amount is less than the max amount, fix the indicator dot at 85%.
    // Otherwise the progress bar should reach 85%, and the indicator dot should move
    // left relative to the overused.
    float progressBarIndicatorOffset =
        (usedAmount < maxAmount) || usedAmount == 0.0f
            ? MAX_PROGRESS_OFFSET
            : ((Math.min(maxAmount / usedAmount, 1.0f) * MAX_PROGRESS_OFFSET));

    // The progress bar state should be the inverse of the indicaot offset.  If the used amount
    // is greater than the max amount, fix the progress bar at 85%.  Otherwise calculate
    // percentage relative to a ceiling of 85%.
    int progressBarPercent =
        (usedAmount < maxAmount) && maxAmount != 0.0f
            ? (int) ((Math.min(usedAmount / maxAmount, 1.0f) * MAX_PROGRESS_OFFSET))
            : MAX_PROGRESS_OFFSET;

    usedAmountText.setText("Used: " + usedAmount);
    indicatorText.setText(Float.toString(maxAmount));
    ConstraintLayout.LayoutParams layoutParams =
        (ConstraintLayout.LayoutParams)
            indicatorConstraintGuideline.getLayoutParams();
    layoutParams.guidePercent = progressBarIndicatorOffset / 100;
    indicatorConstraintGuideline.setLayoutParams(layoutParams);
    indicatorContainer.setVisibility(View.VISIBLE);

    // Ensure full used amount stays on screen by right aligning used amount label if used/max > 50%
    ConstraintLayout constraintLayout =
        findViewById(R.id.indicator_constraint_container);
    ConstraintSet constraintSet = new ConstraintSet();
    constraintSet.clone(constraintLayout);
    if (progressBarIndicatorOffset > 50f) {
      indicatorContainer.setGravity(Gravity.END);
      constraintSet.connect(
          indicatorContainer.getId(),
          ConstraintSet.RIGHT,
          indicatorConstraintGuideline.getId(),
          ConstraintSet.RIGHT);
      constraintSet.applyTo(constraintLayout);
    } else {
      indicatorContainer.setGravity(Gravity.START);
      constraintSet.connect(
          indicatorContainer.getId(),
          ConstraintSet.LEFT,
          indicatorConstraintGuideline.getId(),
          ConstraintSet.LEFT);
      constraintSet.applyTo(constraintLayout);
    }

    myProgressBar.setProgress(progressBarPercent);
    myProgressBar.setVisibility(View.VISIBLE);
  }
}
