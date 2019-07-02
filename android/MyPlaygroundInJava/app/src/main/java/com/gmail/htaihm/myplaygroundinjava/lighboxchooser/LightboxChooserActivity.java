package com.gmail.htaihm.myplaygroundinjava.lighboxchooser;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.gmail.htaihm.myplaygroundinjava.R;
import com.gmail.htaihm.myplaygroundinjava.lighboxchooser.LightboxChooserView.MyLightboxChooserAdapter;
import com.gmail.htaihm.myplaygroundinjava.lighboxchooser.LightboxChooserView.LightboxChooserItemElement;
import java.util.ArrayList;
import java.util.List;

public class LightboxChooserActivity extends AppCompatActivity {

  static final float DIM_AMOUNT = 0.75f;

  public static Intent createIntent(Context context) {
    return new Intent(context, LightboxChooserActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    View view = getLayoutInflater().inflate(R.layout.activity_lightbox_chooser, null);
    view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
    WindowManager.LayoutParams lp = getWindow().getAttributes();
    lp.dimAmount = DIM_AMOUNT;
    getWindow()
        .setBackgroundDrawable(
            new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));
    getWindow().setAttributes(lp);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    setContentView(view);

    List<LightboxChooserItemElement> lightboxChooserItemElements = new ArrayList<>();
    for (int i = 0; i < 60; i++) {
      lightboxChooserItemElements.add(new LightboxChooserItemElement("title " + i, ""));
    }

    MyLightboxChooserAdapter myLightboxChooserAdapter = new MyLightboxChooserAdapter(
        getLayoutInflater(), lightboxChooserItemElements);
    myLightboxChooserAdapter.setPositionToBeFocused(0);
    LightboxChooserView lightboxChooserView = findViewById(R.id.lightboxChooserView);
    lightboxChooserView.setAdapter(myLightboxChooserAdapter);
    lightboxChooserView.setLayoutManager(new LinearLayoutManager(this));
    lightboxChooserView.setItemGap(10);
  }
}
