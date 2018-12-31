import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.animation.AnimationColorChange;
import cs3500.animator.model.color.Color;
import cs3500.animator.model.color.ColorRGB;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.model.shape.Shape2D;
import cs3500.animator.util.TweenModelBuilder;
import org.junit.Test;

public class AnimationColorChangeTest {

  @Test(expected = IllegalArgumentException.class)
  public void testAnimateBeforeAppear() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    new AnimationColorChange(shape, color1, color1,3, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnimateAfterDisappear() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    new AnimationColorChange(shape, color1, color1,8, 30);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnimateOutsideTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    new AnimationColorChange(shape, color1, color1,2, 28);
  }

  @Test
  public void testBeforeTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Color color2 = new ColorRGB(48, 50, 25);

    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    Animation animation = new AnimationColorChange(shape, color1, color2, 10, 20);
    assertEquals(shape.getColor(), color1);

    animation.animate(shape, 3);
    assertEquals(shape.getColor(), color1);

    animation.animate(shape, 7);
    assertEquals(shape.getColor(), color1);
  }

  @Test
  public void testDuringTime() {
    Color color1 = new ColorRGB(0.45, 0.78, 0.24);
    Color color2 = new ColorRGB(0.08, 0.23, 0.98);

    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    Animation animation = new AnimationColorChange(shape, color1, color2, 10, 20);
    assertEquals(shape.getColor(), color1);

    animation.animate(shape,14);
    assertEquals(0.302, shape.getColor().getRed(), 0.001);
    assertEquals(0.560, shape.getColor().getGreen(), 0.001);
    assertEquals(0.536, shape.getColor().getBlue(), 0.001);

    animation.animate(shape, 17);
    assertEquals(0.191, shape.getColor().getRed(), 0.001);
    assertEquals(0.395, shape.getColor().getGreen(), 0.001);
    assertEquals(0.758, shape.getColor().getBlue(), 0.001);

    animation.animate(shape, 20);
    assertEquals(0.08, shape.getColor().getRed(), 0.001);
    assertEquals(0.23, shape.getColor().getGreen(), 0.001);
    assertEquals(0.98, shape.getColor().getBlue(), 0.001);
  }

  @Test
  public void testAfterTime() {
    Color color1 = new ColorRGB(0.45, 0.78, 0.24);
    Color color2 = new ColorRGB(0.08, 0.23, 0.98);

    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    Animation animation = new AnimationColorChange(shape, color1, color2, 10, 20);
    assertEquals(shape.getColor(), color1);

    animation.animate(shape, 20);
    assertEquals(0.08, shape.getColor().getRed(), 0.001);
    assertEquals(0.23, shape.getColor().getGreen(), 0.001);
    assertEquals(0.98, shape.getColor().getBlue(), 0.001);

    animation.animate(shape, 24);
    assertEquals(0.08, shape.getColor().getRed(), 0.001);
    assertEquals(0.23, shape.getColor().getGreen(), 0.001);
    assertEquals(0.98, shape.getColor().getBlue(), 0.001);
  }

  @Test
  public void testDescription() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationColorChange(shape, color1, new ColorRGB(0, 0, 0), 10, 20);

    assertEquals("Shape R changes color from (0.3,0.6,0.7) "
        + "to (0.0,0.0,0.0) from t=10.0s to t=20.0s", animation.toText(1));
  }

  @Test
  public void testGetTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationColorChange(shape, color1, new ColorRGB(0, 0, 0), 10, 20);

    assertEquals(10, animation.getStartTime());
    assertEquals(20, animation.getEndTime());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConflict() {
    TweenModelBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();

    builder.addRectangle("R", 200, 200, 50, 100, 0, 0, 1, 1, 22);
    builder.addOval("C", 500, 100, 60, 30, 0, 0, 1, 6, 25);

    builder.addColorChange("C", 0, 0 ,0, 0, 0 ,0, 10, 15);
    builder.addColorChange("C", 0, 0 ,0, 0, 0 ,0, 14, 25);

    builder.build();
  }
}
