package com.gmail.htaihm.myplaygroundinjava;

import com.gmail.htaihm.myplaygroundinjava.colorchanger.ColorChangerActivity;
import com.gmail.htaihm.myplaygroundinjava.fillscrollview.FillScrollViewActivity;
import com.gmail.htaihm.myplaygroundinjava.observescrolling.ObserveScrollingActivity;
import com.gmail.htaihm.myplaygroundinjava.lighboxchooser.LightboxChooserActivity;
import com.gmail.htaihm.myplaygroundinjava.viewpositioning.LinearLayoutViewPositioningActivity;
import java.util.Arrays;
import java.util.List;

public class MainListItems {

  public static final List<MainListItem> MAIN_LIST_ITEMS = Arrays.asList(

      new MainListItem("Color Changer", (context) -> ColorChangerActivity.createIntent(context)),
      new MainListItem("Fill ScrollView", (context) -> FillScrollViewActivity.createIntent(context)),
      new MainListItem("Lightbox Chooser",
          (context) -> LightboxChooserActivity
              .createIntent(context)),
      new MainListItem("Linear Layout view positioning",
          (context) -> LinearLayoutViewPositioningActivity
              .createIntent(context)),
      new MainListItem("Observe scrolling",
          (context) -> ObserveScrollingActivity
              .createIntent(context))
  );
}
