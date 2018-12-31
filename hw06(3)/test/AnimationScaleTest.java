import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.animation.AnimationScale;
import cs3500.animator.model.color.Color;
import cs3500.animator.model.color.ColorRGB;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.model.shape.Shape2D;
import cs3500.animator.util.TweenModelBuilder;
import org.junit.Test;

public class AnimationScaleTest {

  @Test(expected = IllegalArgumentException.class)
  public void testBeforeAppears() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    new AnimationScale(shape, 0, 0, 18.0, 170.0, 3, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAfterDisappears() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    new AnimationScale(shape, 0, 0,  18.0, 170.0, 10, 26);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOutsideTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    new AnimationScale(shape, 0, 0, 18.0, 170.0, 4, 26);
  }

  @Test
  public void testBeforeTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationScale(shape, 0, 0, 18.0, 170.0, 10, 20);

    animation.animate(shape, 4);
    assertEquals(40.0, shape.getDimX(), 0.001);
    assertEquals(60.0, shape.getDimY(), 0.001);

    animation.animate(shape, 8);
    assertEquals(40.0, shape.getDimX(), 0.001);
    assertEquals(60.0, shape.getDimY(), 0.001);
  }

  @Test
  public void testDuringTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationScale(shape, 40, 60, 18.0, 170.0, 10, 20);

    animation.animate(shape, 13);
    assertEquals(33.4, shape.getDimX(), 0.001);
    assertEquals(93.0, shape.getDimY(), 0.001);

    animation.animate(shape, 18);
    assertEquals(22.4, shape.getDimX(), 0.001);
    assertEquals(148.0, shape.getDimY(), 0.001);

    animation.animate(shape, 20);
    assertEquals(18.0, shape.getDimX(), 0.001);
    assertEquals(170.0, shape.getDimY(), 0.001);
  }

  @Test
  public void testAfterTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationScale(shape, 0, 0, 18.0, 170.0, 10, 20);

    animation.animate(shape, 20);
    assertEquals(18.0, shape.getDimX(), 0.001);
    assertEquals(170.0, shape.getDimY(), 0.001);

    animation.animate(shape, 22);
    assertEquals(18.0, shape.getDimX(), 0.001);
    assertEquals(170.0, shape.getDimY(), 0.001);

    animation.animate(shape, 25);
    assertEquals(18.0, shape.getDimX(), 0.001);
    assertEquals(170.0, shape.getDimY(), 0.001);
  }

  @Test
  public void testDescription() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationScale(shape, 40, 60, 18.0, 170.0, 10, 20);

    assertEquals("Shape R scales from Width: 40.0, Height: 60.0 to "
        + "Width: 18.0, Height: 170.0 from t=10.0s to t=20.0s", animation.toText(1));
  }

  @Test
  public void testGetTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationScale(shape, 0, 0, 10, 20, 10, 20);

    assertEquals(10, animation.getStartTime());
    assertEquals(20, animation.getEndTime());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConflict() {
    TweenModelBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();

    builder.addOval("C", 500, 100, 60, 30, 0, 0, 1, 6, 25);

    builder.addScaleToChange("C", 0, 0, 300, 300, 10, 15);
    builder.addScaleToChange("C", 0, 0, 300, 300, 15, 25);

    builder.build();
  }
}
