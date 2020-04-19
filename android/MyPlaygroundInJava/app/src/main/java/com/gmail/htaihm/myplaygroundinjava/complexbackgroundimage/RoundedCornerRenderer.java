package com.gmail.htaihm.myplaygroundinjava.complexbackgroundimage;


import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.RectF;
import android.view.View;
import androidx.annotation.Px;

/** Helper for a {@link android.view.ViewGroup} to display child views with round corners. */
public class RoundedCornerRenderer {

  /** Callback for {@code drawChild} from the super class. */
  public interface SuperDrawChildMethod {
    @SuppressWarnings("GoodTime") // TODO(b/146115126): fix GoodTime violation
    boolean drawChild(Canvas canvas, View child, long drawingTime);
  }

  @Px
  private final int cornerRadius;
  private final SuperDrawChildMethod superDrawChildMethod;
  private final Path path = new Path();
  private final RectF childBound = new RectF();

  /**
   * Creates a renderer for a {@link android.view.ViewGroup}.
   *
   * @param superDrawChildMethod should be {@code super::drawChild}.
   */
  public RoundedCornerRenderer(@Px int cornerRadius, SuperDrawChildMethod superDrawChildMethod) {
    this.cornerRadius = cornerRadius;
    this.superDrawChildMethod = superDrawChildMethod;
    path.setFillType(FillType.WINDING);
  }

  /** Call this method in {@link android.view.ViewGroup#drawChild} to display round corners. */
  @SuppressWarnings("GoodTime") // TODO(b/146115126): fix GoodTime violation
  public boolean drawChildWithRoundCorner(Canvas canvas, View child, long drawingTime) {
    childBound.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
    path.addRoundRect(childBound, cornerRadius, cornerRadius, Direction.CCW);
    canvas.save();
    canvas.clipPath(path);
    boolean result = superDrawChildMethod.drawChild(canvas, child, drawingTime);
    canvas.restore();
    path.reset();
    return result;
  }
}
