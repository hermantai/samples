package com.gmail.htaihm.myplaygroundinjava.lighboxchooser;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

import android.view.animation.Interpolator;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.gmail.htaihm.myplaygroundinjava.R;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom view that displays a vertical list of items. Iteration through the items is customized
 * so that focused item is always centered vertically if possible. An overlay helps highlight the
 * focused item.
 */
public class LightboxChooserView extends FrameLayout implements RequestChildFocusListener {

  // adjust overlay animation duration to be more consistent with how fast the list scrolls
  private static final int FOCUS_OVERLAY_ANIM_DURATION = 230;

  private static final int ITEM_FONT_COLOR_CHANGE_DELAY_GAINING_FOCUS = 90;
  private static final int ITEM_FONT_COLOR_CHANGE_DELAY_LOSING_FOCUS = 77;

  private int itemGapPx;
  private View focusOverlay;
  private MyRecyclerView recyclerView;
  private final Interpolator focusOverlayAnimInterpolator = new FastOutSlowInInterpolator();

  public LightboxChooserView(Context context) {
    super(context);
    init();
  }

  public LightboxChooserView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public LightboxChooserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public int getItemCount() {
    LightboxChooserAdapter<? extends ViewHolder> adapter =
        (LightboxChooserAdapter<? extends ViewHolder>) recyclerView.getAdapter();
    if (adapter == null) {
      throw new IllegalStateException("No adapter attached to LightboxChooserView.");
    }
    return adapter.getItemCount();
  }

  public void setItemGap(int itemGapDp) {
    this.itemGapPx =
        (int)
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, itemGapDp, getResources().getDisplayMetrics());
    recyclerView.addItemDecoration(new VerticalGapItemDecoration(itemGapPx));
  }

  public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
    recyclerView.setLayoutManager(layoutManager);
  }

  public void setAdapter(LightboxChooserAdapter<? extends ViewHolder> adapter) {
    recyclerView.setAdapter(adapter);
  }

  public void focusOnItemAtPosition(int position) {
    LightboxChooserAdapter<? extends ViewHolder> adapter =
        (LightboxChooserAdapter<? extends ViewHolder>) recyclerView.getAdapter();
    if (adapter == null) {
      throw new IllegalStateException("No adapter attached to LightboxChooserView.");
    }
    adapter.setPositionToBeFocused(position);
    recyclerView.scrollToPosition(position);
  }

  private void init() {
    inflate(getContext(), R.layout.lightbox_chooser_view, this);
    focusOverlay = findViewById(R.id.focus_overlay);
    recyclerView = findViewById(R.id.recycler_view);
    recyclerView.addRequestChildFocusListener(this);
  }

  // ---- Implements RequestChildFocusListener

  // By default RecyclerView scrolls just enough to show entire focused item, while the method below
  // helps scroll the focused item to the middle of the RecyclerView if within scrollable range;
  // otherwise as far as it's scrollable.
  // Meanwhile, the method below animates the focus overlay to overlap with the focused item. In
  // order to do that, we need to calculate the actual distance the RecyclerView will scroll across,
  // taking into consideration of its scrollable range, and then apply the distance to move the
  // overlay.
  @Override
  public void onRequestChildFocus(View child, View focused) {
    if (!(child.getParent() instanceof MyRecyclerView) || !child.isFocused()) {
      return;
    }
    MyRecyclerView recyclerView = (MyRecyclerView) child.getParent();
    int childAdapterPosition = recyclerView.getChildAdapterPosition(child);
    int cellHeight = child.getHeight() + itemGapPx;

    // get position of RecyclerView
    int offset = (recyclerView.getHeight() - child.getHeight()) / 2;
    int[] rvPosition = new int[2];
    recyclerView.getLocationInWindow(rvPosition);
    int rvTop = rvPosition[1];
    int rvBottom = rvPosition[1] + recyclerView.getHeight();

    // defines an area in the middle of the RecyclerView (with height of an item in the RV)
    int minTop = rvTop + offset;
    int maxBottom = rvBottom - offset;

    // get position of RecyclerView child to gain focus
    int[] childPosition = new int[2];
    child.getLocationInWindow(childPosition);
    int childTop = childPosition[1];
    int childBottom = childPosition[1] + child.getHeight();

    // get position of focus overlay
    int[] overlayPosition = new int[2];
    focusOverlay.getLocationInWindow(overlayPosition);
    int hlTop = overlayPosition[1];
    int hlBottom = overlayPosition[1] + child.getHeight();

    // get scrollable range of the child to-be-focused
    int lowestChildTopCanBe = rvTop + cellHeight * childAdapterPosition;
    int highestChildBottomCanBe =
        rvBottom
            - cellHeight * (recyclerView.getAdapter().getItemCount() - childAdapterPosition - 1);

    // smooth scroll recyclerView to move focused child to middle of the screen (into the area
    // defined earlier). And animate the overlay to the same location as the focused child.
    int overlaySlideDist;
    if (childTop < minTop) {
      recyclerView.smoothScrollBy(0, childTop - minTop);
      overlaySlideDist = Math.min(lowestChildTopCanBe, minTop) - hlTop;
    } else if (childBottom > maxBottom) {
      recyclerView.smoothScrollBy(0, childBottom - maxBottom);
      overlaySlideDist = Math.max(maxBottom, highestChildBottomCanBe) - hlBottom;
    } else {
      overlaySlideDist = childTop - hlTop;
    }
    // in case list contains items of different sizes, adjust overlay height
    ViewGroup.LayoutParams layoutParams = focusOverlay.getLayoutParams();
    if (layoutParams.height != child.getHeight()) {
      layoutParams.height = child.getHeight();
      focusOverlay.setLayoutParams(layoutParams);
    }
    focusOverlay.setVisibility(VISIBLE);
    focusOverlay
        .animate()
        .yBy(overlaySlideDist)
        .setInterpolator(focusOverlayAnimInterpolator)
        .setDuration(FOCUS_OVERLAY_ANIM_DURATION)
        .start();
  }

  /**
   * Custom adapter required by {@link LightboxChooserView}. It works with {@link LightboxChooserView} hand-in-hand to
   * support scrolling to and requesting focus on an item at a given adapter position.
   */
  public abstract static class LightboxChooserAdapter<T extends ViewHolder> extends RecyclerView.Adapter<T> {
    private int positionToBeFocused = -1;
    private boolean focusToBeFulfilled;

    void setPositionToBeFocused(int positionToBeFocused) {
      this.positionToBeFocused = positionToBeFocused;
      focusToBeFulfilled = true;
    }

    @Override
    public void onViewAttachedToWindow(T viewHolder) {
      if (focusToBeFulfilled && viewHolder.getAdapterPosition() == positionToBeFocused) {
        focusToBeFulfilled = false;
        positionToBeFocused = -1;
        // make sure child is laid out before request focus on it
        viewHolder
            .itemView
            .getViewTreeObserver()
            .addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                  @Override
                  public void onGlobalLayout() {
                    viewHolder.itemView.requestFocus();
                    viewHolder.itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                  }
                });
      }
    }
  }

  static class MyLightboxChooserAdapter extends
      LightboxChooserAdapter<LightboxChooserItemViewHolder> {
    private static final int TEXT_COLOR = 0x99E8EAED;

    private final LayoutInflater layoutInflater;
    private final List<LightboxChooserItemElement> items = new ArrayList<>();
    private boolean initialFocus = true;

    MyLightboxChooserAdapter(
        LayoutInflater layoutInflater,
        List<LightboxChooserItemElement> items) {
      this.layoutInflater = layoutInflater;
      this.items.addAll(items);
    }

    @Override
    public LightboxChooserItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
      View itemView =
          layoutInflater.inflate(
              R.layout.lightbox_chooser_item, viewGroup, /* attachToRoot= */ false);
      return new LightboxChooserItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LightboxChooserItemViewHolder viewHolder, int i) {
      LightboxChooserItemElement itemElement = items.get(i);
      viewHolder.title.setText(itemElement.getTitle());
      viewHolder.title.setVisibility(View.VISIBLE);
      viewHolder.title.setTextColor(TEXT_COLOR);
      applyFontColorOnFocusChange(viewHolder);

      if (itemElement.getSubtitle().isEmpty()) {
        viewHolder.subtitle.setVisibility(View.GONE);
      } else {
        viewHolder.subtitle.setText(itemElement.getSubtitle());
      }

      viewHolder.itemView.setOnClickListener((v) -> {
        Log.i("LightboxChooserView", "item " + itemElement.getTitle() + " clicked");
      });
    }

    @Override
    public int getItemCount() {
      return items.size();
    }

    // special handling of font color change to avoid visual flicker - when overlay moves from item
    // A to item B, font color of A changes first, followed by change on B.
    private void applyFontColorOnFocusChange(LightboxChooserItemViewHolder viewHolder) {
      int buttonTextSelectedColor = 0xFF202124;

      viewHolder.itemView.setOnFocusChangeListener(
          (v, hasFocus) -> {
            if (initialFocus) {
              // No delay in font color change when the lightbox chooser is first loaded
              initialFocus = false;
              viewHolder.title.setTextColor(hasFocus ? buttonTextSelectedColor : TEXT_COLOR);
            } else {
              v.postOnAnimationDelayed(
                  () ->
                      viewHolder.title.setTextColor(
                          hasFocus ? buttonTextSelectedColor : TEXT_COLOR),
                  hasFocus
                      ? ITEM_FONT_COLOR_CHANGE_DELAY_GAINING_FOCUS
                      : ITEM_FONT_COLOR_CHANGE_DELAY_LOSING_FOCUS);
            }
          });
    }
  }

  static class LightboxChooserItemViewHolder extends RecyclerView.ViewHolder {
    final TextView title;
    final TextView subtitle;

    LightboxChooserItemViewHolder(View view) {
      super(view);
      title = view.findViewById(R.id.title);
      subtitle = view.findViewById(R.id.subtitle);
    }
  }

  static class LightboxChooserItemElement {
    private String title;
    private String subtitle;

    LightboxChooserItemElement(String title, String subtitle) {
      this.title = title;
      this.subtitle = subtitle;
    }

    public String getTitle() {
      return title;
    }

    public String getSubtitle() {
      return subtitle;
    }
  }
}
