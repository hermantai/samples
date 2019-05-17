package com.gmail.htaihm.myplaygroundinjava.colorchanger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.gmail.htaihm.myplaygroundinjava.R;
import java.util.Random;

public class ColorChangerActivity extends AppCompatActivity {

  private static final String TAG = ColorChangerActivity.class.getSimpleName();

  private int fruitIndex = 0;

  public static Intent createIntent(Context context) {
    return new Intent(context, ColorChangerActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.i(TAG, "onCreate");
    setContentView(R.layout.activity_color_changer);

    setTitle("Color Changer");

    View rootView = findViewById(R.id.root_view);
    Button changeColorbutton = findViewById(R.id.change_color_button);
    TextView displayText = findViewById(R.id.display_text);
    Button refreshFruitButton = findViewById(R.id.refresh_fruits_button);

    ColorChangerViewModel colorChangerViewModel = ViewModelProviders.of(this)
        .get(ColorChangerViewModel.class);
    rootView.setBackgroundColor(colorChangerViewModel.getColorResource());

    changeColorbutton.setOnClickListener((v -> {
      int color = generateRandomColor();
      rootView.setBackgroundColor(color);
      colorChangerViewModel.setColorResource(color);
    }));

    FruitViewModel fruitViewModel = ViewModelProviders.of(this).get(FruitViewModel.class);
    fruitViewModel.getFruitList().observe(this, fruitList -> {
      Log.i(TAG, String.format("Got list of fruits: %s", fruitList));
      displayText.setText(fruitList.get(fruitIndex));
      fruitIndex = (fruitIndex + 1) % fruitList.size();
    });

    refreshFruitButton.setOnClickListener(v -> fruitViewModel.loadFruits());
  }

  @ColorInt
  private int generateRandomColor() {
    Random random = new Random();
    return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
  }
}
