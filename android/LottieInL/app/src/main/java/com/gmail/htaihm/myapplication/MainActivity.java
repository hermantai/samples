package com.gmail.htaihm.myapplication;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String url = "http://www.gs"
        + "tatic.co"
        + "m/play/points/i"
        + "cons/level"
        + "_badges/lot"
        + "tie_le"
        + "vel2.json";
    Log.i("MainActivity", "url is " + url);
    LottieAnimationView lottieAnimationView = findViewById(R.id.animation_view);

    lottieAnimationView.setAnimationFromUrl(url);
  }
}
