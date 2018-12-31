import org.junit.Test;

import cs3500.animator.model.color.Color;
import cs3500.animator.model.color.ColorRGB;

import static org.junit.Assert.assertEquals;

public class ColorRGBTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNegR() {
    new ColorRGB(-2, 110, 140);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegG() {
    new ColorRGB(45, -110, 140);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegB() {
    new ColorRGB(20, 110, -140);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRLarge() {
    new ColorRGB(256, 110, 140);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGLarge() {
    new ColorRGB(150, 280, 140);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBLarge() {
    new ColorRGB(255, 110, 380);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDoubleRLarge() {
    new ColorRGB(1.2, 0.81, 0.72);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDoubleGLarge() {
    new ColorRGB(0.62, 1.004, 0.72);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDoubleBLarge() {
    new ColorRGB(0.513, 0.81, 3.4134);
  }

  @Test
  public void testIntGetR() {
    Color color = new ColorRGB(50, 80, 120);
    assertEquals(0.196, color.getRed(), 0.001);
  }

  @Test
  public void testIntGetG() {
    Color color = new ColorRGB(50, 80, 120);
    assertEquals(0.313, color.getGreen(), 0.001);
  }

  @Test
  public void testIntGetB() {
    Color color = new ColorRGB(50, 80, 120);
    assertEquals(0.470, color.getBlue(), 0.001);
  }

  @Test
  public void testDoubleGetR() {
    Color color = new ColorRGB(0.196, 0.313, 0.470);
    assertEquals(0.196, color.getRed(), 0.001);
  }

  @Test
  public void testDoubleGetG() {
    Color color = new ColorRGB(0.196, 0.313, 0.470);
    assertEquals(0.313, color.getGreen(), 0.001);
  }

  @Test
  public void testDoubleGetB() {
    Color color = new ColorRGB(0.196, 0.313, 0.470);
    assertEquals(0.470, color.getBlue(), 0.001);
  }
}
