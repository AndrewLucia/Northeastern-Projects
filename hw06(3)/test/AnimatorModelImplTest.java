import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.util.TweenModelBuilder;
import org.junit.Test;

public class AnimatorModelImplTest {

  @Test(expected = IllegalArgumentException.class)
  public void testConflict() {
    TweenModelBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();

    builder.addRectangle("R", 200, 200, 50, 100, 0, 0, 1, 1, 22);
    builder.addOval("C", 500, 100, 60, 30, 0, 0, 1, 6, 25);

    builder.addMove("C", 200, 200, 300, 300, 10, 15);
    builder.addMove("C", 200, 200, 300, 300, 14, 25);

    builder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConflict2() {
    TweenModelBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();

    builder.addRectangle("R", 200, 200, 50, 100, 0, 0, 1, 1, 22);
    builder.addOval("C", 500, 100, 60, 30, 0, 0, 1, 6, 25);

    builder.addMove("C", 200, 200, 300, 300, 10, 15);
    builder.addMove("C", 200, 200, 300, 300, 15, 20);

    builder.build();
  }

  @Test
  public void testAddShapeGetFrame() {
    TweenModelBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();

    builder.addRectangle("R", 200, 200, 50, 100, 0, 0, 1, 1, 22);
    builder.addOval("C", 500, 100, 60, 30, 0, 0, 1, 6, 25);

    AnimatorModel model = builder.build();

    assertEquals(0, model.getFrame(0).getShapesMap().size());
    assertEquals(1, model.getFrame(1).getShapesMap().size());
    assertEquals(2, model.getFrame(6).getShapesMap().size());
  }

  @Test
  public void testAddAnimation() {
    TweenModelBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();

    builder.addOval("C", 500, 100, 60, 30, 0, 0, 1, 6, 20);
    builder.addRectangle("R", 100, 100, 10, 10, 0, 0, 1, 2, 10);

    builder.addScaleToChange("R", 300, 300, 70, 70, 3, 5);
    builder.addMove("C", 200, 200, 300, 300, 10, 15);
    builder.addColorChange("C", 0, 0, 0, 1, 1, 1, 8, 18);

    AnimatorModel model = builder.build();

    assertEquals("move", model.getAllShapes().get(0).getAnimations().get(0).getType());
    assertEquals("change-color", model.getAllShapes().get(0).getAnimations().get(1).getType());
    assertEquals("scale", model.getAllShapes().get(1).getAnimations().get(0).getType());
  }

  @Test
  public void testAnimationOver() {
    TweenModelBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();

    builder.addOval("C", 500, 100, 60, 30, 0, 0, 1, 6, 20);
    builder.addRectangle("R", 100, 100, 10, 10, 0, 0, 1, 2, 10);

    builder.addScaleToChange("R", 300, 300, 70, 70, 3, 5);
    builder.addMove("C", 200, 200, 300, 300, 10, 15);
    builder.addColorChange("C", 0, 0, 0, 1, 1, 1, 8, 18);

    AnimatorModel model = builder.build();

    assertFalse(model.isAnimationOver(0));
    for (int i = 0; i < 20; i++) {
      assertFalse(model.isAnimationOver(i));
    }
    assertTrue(model.isAnimationOver(21));
  }
}