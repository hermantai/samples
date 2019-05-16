package com.gmail.htaihm.myplaygroundinjava;

import android.content.Intent;
import com.gmail.htaihm.myplaygroundinjava.colorchanger.ColorChangerActivity;
import java.util.Arrays;
import java.util.List;

public class MainListItems {

  public static final List<MainListItem> MAIN_LIST_ITEMS = Arrays.asList(
      new MainListItem("Color Changer", (context) -> ColorChangerActivity.createIntent(context))
  );
}
