package com.gmail.htaihm.stackedcomponents;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ViewGroup vg = findViewById(R.id.container);

    for (int i = 0; i < 10; i++) {
      TextView tv = new TextView(this);
      tv.setText("text " + i);
      vg.addView(tv);
    }

    //animationContainer.addView()
  }
}
