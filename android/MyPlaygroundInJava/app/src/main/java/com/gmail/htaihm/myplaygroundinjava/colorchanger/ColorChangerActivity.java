package com.gmail.htaihm.myplaygroundinjava.colorchanger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.gmail.htaihm.myplaygroundinjava.R;

public class ColorChangerActivity extends AppCompatActivity {

  public static Intent createIntent(Context context) {
    return new Intent(context, ColorChangerActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_color_changer);
  }
}
