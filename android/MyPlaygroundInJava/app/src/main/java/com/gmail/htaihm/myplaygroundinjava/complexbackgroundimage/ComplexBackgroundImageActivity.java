package com.gmail.htaihm.myplaygroundinjava.complexbackgroundimage;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gmail.htaihm.myplaygroundinjava.R;

public class ComplexBackgroundImageActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_complex_background_image2);
  }

  public static Intent createIntent(Context context) {
    return new Intent(context, ComplexBackgroundImageActivity.class);
  }
}