package com.gmail.htaihm.myplaygroundinjava.widgetsdemo;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gmail.htaihm.myplaygroundinjava.R;

public class WidgetsDemoActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_widgets_demo);
  }

  public static Intent createIntent(Context context) {
    return new Intent(context, WidgetsDemoActivity.class);
  }
}
