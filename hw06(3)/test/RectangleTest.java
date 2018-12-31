import static org.junit.Assert.assertEquals;

import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.animation.AnimationMove;
import cs3500.animator.model.animation.AnimationScale;
import cs3500.animator.model.color.Color;
import cs3500.animator.model.color.ColorRGB;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.model.shape.Shape2D;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class RectangleTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNullName() {
    Color color = new ColorRGB(0, 0, 0);
    new Rectangle(null, 20, 80, 4, 6, 0, 5, color);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegDimX() {
    Color color = new ColorRGB(0, 0, 0);
    new Rectangle("R", 20, 80, -4, 6, 0, 5, color);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegDimY() {
    Color color = new ColorRGB(0, 0, 0);
    new Rectangle("R", 20, 80, 4, -6, 0, 5, color);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegDims() {
    Color color = new ColorRGB(0, 0, 0);
    new Rectangle("R", 20, 80, -4, -6, 0, 5, color);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegAppear() {
    Color color = new ColorRGB(0, 0, 0);
    new Rectangle("R", 20, 80, 4, 6, -5, 10, color);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegDisappear() {
    Color color = new ColorRGB(0, 0, 0);
    new Rectangle("R", 20, 80, 4, 6, 0, -10, color);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegTime() {
    Color color = new ColorRGB(0, 0, 0);
    new Rectangle("R", 20, 80, 4, 6, -15, -10, color);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReverseTime() {
    Color color = new ColorRGB(0, 0, 0);
    new Rectangle("R", 20, 80, 4, 6, 15, 10, color);
  }

  @Test
  public void testGetName() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals("R", shape.getName());
  }

  @Test
  public void testToText() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals("Name: R" + System.lineSeparator()
        + "Type: rectangle" + System.lineSeparator()
        + "Corner: (20.0,80.0), Width: 4.0, Height: 6.0, Color: (0.0,0.0,0.0)" + System
        .lineSeparator()
        + "Appears at t=10.0s" + System.lineSeparator()
        + "Disappears at t=15.0s" + System.lineSeparator(), shape.toText(1));
  }

  @Test
  public void testGetColor() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals(color, shape.getColor());
  }

  @Test
  public void testGetDimensions() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals(4.0, shape.getDimX(), 0.001);
    assertEquals(6.0, shape.getDimY(), 0.001);
  }

  @Test
  public void testGetPosition() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals(20.0, shape.getPosX(), 0.001);
    assertEquals(80.0, shape.getPosY(), 0.001);
  }

  @Test
  public void testGetTime() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals(10, shape.getTimeAppears());
    assertEquals(15, shape.getTimeDisappears());
  }

  @Test
  public void testGetAnimations() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals(new ArrayList(), shape.getAnimations());
  }

  @Test
  public void testDimensionAsText() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals("Width: 4.0, Height: 6.0", shape.dimensionsAsText(4.0, 6.0));
  }

  @Test
  public void testMoveTo() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals(20.0, shape.getPosX(), 0.001);
    assertEquals(80.0, shape.getPosY(), 0.001);

    shape.moveTo(12.0, 15.0);
    assertEquals(12.0, shape.getPosX(), 0.001);
    assertEquals(15.0, shape.getPosY(), 0.001);

    shape.moveTo(3.0, 7.0);
    assertEquals(3.0, shape.getPosX(), 0.001);
    assertEquals(7.0, shape.getPosY(), 0.001);
  }

  @Test
  public void testScale() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals(4.0, shape.getDimX(), 0.001);
    assertEquals(6.0, shape.getDimY(), 0.001);

    shape.scaleTo(18.0, 24.0);
    assertEquals(18.0, shape.getDimX(), 0.001);
    assertEquals(24.0, shape.getDimY(), 0.001);

    shape.scaleTo(2.0, 4.0);
    assertEquals(2.0, shape.getDimX(), 0.001);
    assertEquals(4.0, shape.getDimY(), 0.001);
  }

  @Test
  public void testSetColor() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals(color, shape.getColor());

    Color color1 = new ColorRGB(10, 20, 30);
    shape.setColor(color1);
    assertEquals(color1, shape.getColor());

    Color color2 = new ColorRGB(0.98, 0.41, 0.69);
    shape.setColor(color2);
    assertEquals(color2, shape.getColor());
  }

  @Test
  public void testAddAnimation() {
    Color color = new ColorRGB(0, 0, 0);
    Shape2D shape = new Rectangle("R", 20, 80, 4, 6, 10, 15, color);
    assertEquals(new ArrayList<Animation>(), shape.getAnimations());
    List<Animation> l1 = new ArrayList<Animation>();

    Animation a1 = new AnimationMove(shape, 0, 0, 5.0, 10.0, 10, 14);
    shape.addAnimation(a1);
    l1.add(a1);
    assertEquals(l1, shape.getAnimations());

    Animation a2 = new AnimationScale(shape, 0, 0, 5.0, 10.0, 10, 14);
    shape.addAnimation(a2);
    l1.add(a2);
    assertEquals(l1, shape.getAnimations());
  }
}
