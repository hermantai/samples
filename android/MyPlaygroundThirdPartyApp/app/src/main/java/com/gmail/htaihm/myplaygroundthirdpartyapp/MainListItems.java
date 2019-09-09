package com.gmail.htaihm.myplaygroundthirdpartyapp;

import com.gmail.htaihm.myplaygroundthirdpartyapp.drawoverlayclient.DrawOverlayClientActivity;
import java.util.Arrays;
import java.util.List;

public class MainListItems {

  public static final List<MainListItem> MAIN_LIST_ITEMS = Arrays.asList(
      new MainListItem("Draw Overlay Client",
          (context) -> DrawOverlayClientActivity.createIntent(context))
  );
}
