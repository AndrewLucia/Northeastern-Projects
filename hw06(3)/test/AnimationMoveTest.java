import static org.junit.Assert.assertEquals;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.animation.AnimationMove;
import cs3500.animator.model.color.Color;
import cs3500.animator.model.color.ColorRGB;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.model.shape.Shape2D;
import cs3500.animator.util.TweenModelBuilder;
import org.junit.Test;

public class AnimationMoveTest {

  @Test(expected = IllegalArgumentException.class)
  public void testBeforeAppears() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    new AnimationMove(shape, 0, 0, 200.0, 300.0, 3, 20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAfterDisappears() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    new AnimationMove(shape, 0, 0,  200.0, 300.0, 10, 26);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOutsideTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 0, 40, 60, 5, 25, color1);
    new AnimationMove(shape, 0, 0,  200.0, 300.0, 4, 26);
  }

  @Test
  public void testBeforeTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationMove(shape, 0, 0, 200.0, 300.0, 10, 20);

    animation.animate(shape, 4);
    assertEquals(0.0, shape.getPosX(), 0.001);
    assertEquals(10.0, shape.getPosY(), 0.001);

    animation.animate(shape, 8);
    assertEquals(0.0, shape.getPosX(), 0.001);
    assertEquals(10.0, shape.getPosY(), 0.001);
  }

  @Test
  public void testDuringTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationMove(shape, 0, 10, 200.0, 300.0, 10, 20);

    animation.animate(shape, 13);
    assertEquals(60.0, shape.getPosX(), 0.001);
    assertEquals(97.0, shape.getPosY(), 0.001);

    animation.animate(shape, 18);
    assertEquals(160.0, shape.getPosX(), 0.001);
    assertEquals(242.0, shape.getPosY(), 0.001);

    animation.animate(shape, 20);
    assertEquals(200.0, shape.getPosX(), 0.001);
    assertEquals(300.0, shape.getPosY(), 0.001);
  }

  @Test
  public void testAfterTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationMove(shape, 0, 0, 200.0, 300.0, 10, 20);

    animation.animate(shape, 20);
    assertEquals(200.0, shape.getPosX(), 0.001);
    assertEquals(300.0, shape.getPosY(), 0.001);

    animation.animate(shape, 22);
    assertEquals(200.0, shape.getPosX(), 0.001);
    assertEquals(300.0, shape.getPosY(), 0.001);

    animation.animate(shape, 25);
    assertEquals(200.0, shape.getPosX(), 0.001);
    assertEquals(300.0, shape.getPosY(), 0.001);
  }

  @Test
  public void testDescription() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationMove(shape, 0, 10, 18.0, 170.0, 10, 20);

    assertEquals("Shape R moves from (0.0,10.0) to "
        + "(18.0,170.0) from t=10.0s to t=20.0s", animation.toText(1));
  }

  @Test
  public void testGetTime() {
    Color color1 = new ColorRGB(80, 145, 170);
    Shape2D shape = new Rectangle("R", 0, 10, 40, 60, 5, 25, color1);
    Animation animation = new AnimationMove(shape, 0, 0, 50, 60, 10, 20);

    assertEquals(10, animation.getStartTime());
    assertEquals(20, animation.getEndTime());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConflict() {
    TweenModelBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();

    builder.addRectangle("R", 200, 200, 50, 100, 0, 0, 1, 1, 22);
    builder.addOval("C", 500, 100, 60, 30, 0, 0, 1, 6, 25);

    builder.addMove("C", 200, 200, 300, 300, 10, 15);
    builder.addMove("C", 200, 200, 300, 300, 14, 15);

    builder.build();
  }
}
