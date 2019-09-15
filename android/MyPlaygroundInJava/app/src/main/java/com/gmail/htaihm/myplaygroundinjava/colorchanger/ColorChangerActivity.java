package com.gmail.htaihm.myplaygroundinjava.colorchanger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;
import com.gmail.htaihm.myplaygroundinjava.R;
import java.util.Random;

/**
 * Play around viewmodel and livedata.
 */
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
    Button btnChangeColor = findViewById(R.id.btnChangeColor);
    TextView tvDisplayFruit = findViewById(R.id.tvDisplayFruit);
    Button btnRefreshFruits = findViewById(R.id.btnRefreshFruits);

    ColorChangerViewModel colorChangerViewModel = ViewModelProviders.of(this)
        .get(ColorChangerViewModel.class);
    rootView.setBackgroundColor(colorChangerViewModel.getColorResource());

    btnChangeColor.setOnClickListener((v -> {
      int color = generateRandomColor();
      rootView.setBackgroundColor(color);
      colorChangerViewModel.setColorResource(color);
    }));

    FruitViewModel fruitViewModel = ViewModelProviders.of(this).get(FruitViewModel.class);
    fruitViewModel.getFruitList().observe(this, fruitList -> {
      Log.i(TAG, String.format("Got list of fruits: %s", fruitList));
      tvDisplayFruit.setText(fruitList.get(fruitIndex));
      fruitIndex = (fruitIndex + 1) % fruitList.size();
    });

    btnRefreshFruits.setOnClickListener(v -> fruitViewModel.loadFruits());

    TextView tvSavedText = findViewById(R.id.tvSavedText);

    SavedTextViewModel savedTextViewModel = ViewModelProviders
        .of(this, new SavedStateViewModelFactory(getApplication(), this))
        .get(SavedTextViewModel.class);
    savedTextViewModel.getText().observe(this, savedText -> {
      tvSavedText.setText(savedText);
    });

    EditText etTextToSave = findViewById(R.id.etTextToSave);
    Button btnSaveText = findViewById(R.id.btnSaveText);
    btnSaveText.setOnClickListener(v -> {
      savedTextViewModel.setText(etTextToSave.getText().toString());
    });
  }

  @ColorInt
  private int generateRandomColor() {
    Random random = new Random();
    return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
  }
}
