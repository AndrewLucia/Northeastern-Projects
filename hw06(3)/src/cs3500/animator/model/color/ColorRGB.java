package cs3500.animator.model.color;

/**
 * Represents a color as red, green, and blue values.
 */
public class ColorRGB implements Color {

  //represents the red value between 0.0 and 1.0
  private double red;
  //represents the green value between 0.0 and 1.0
  private double green;
  //represents the blue value between 0.0 and 1.0
  private double blue;

  /**
   * Constructs this color with the given RGB value. Valid RGB values are integers that range from 0
   * (inclusive) to 255 (inclusive)
   *
   * @param red the red value of this color
   * @param green the green value of this color
   * @param blue the blue value of this color
   * @throws IllegalArgumentException if any value is not within the range [0,255]
   */
  public ColorRGB(int red, int green, int blue) throws IllegalArgumentException {
    if ((red < 0 || green < 0 || blue < 0)
        || (red > 255 || green > 255 || blue > 255)) {
      throw new IllegalArgumentException(String.format("RGB values must be in range [0,255], "
          + "given: (%d,%d,%d)", red, blue, green));
    } else {
      this.red = ((double) red / 255.0);
      this.green = ((double) green / 255.0);
      this.blue = ((double) blue / 255.0);
    }
  }

  /**
   * Constructs this color with the given RGB value. Valid RGB values are doubles that range from
   * 0.0 (inclusive) to 1.0 (inclusive)
   *
   * @param red the red value of this color
   * @param green the green value of this color
   * @param blue the blue value of this color
   * @throws IllegalArgumentException if any value is not within the range [0.0,1.0]
   */
  public ColorRGB(double red, double green, double blue) {
    if ((red < 0.0 || green < 0.0 || blue < 0.0)
        || (red > 1.0 || green > 1.0 || blue > 1.0)) {
      throw new IllegalArgumentException(String.format("RGB values must be in range [0.0,1.0], "
          + "given: (%.1f,%.1f,%.1f)", red, blue, green));
    } else {
      this.red = red;
      this.green = green;
      this.blue = blue;
    }
  }

  @Override
  public String asText() {
    return String.format("(%.1f,%.1f,%.1f)", this.red, this.green, this.blue);
  }

  @Override
  public double getRed() {
    return this.red;
  }

  @Override
  public double getGreen() {
    return this.green;
  }

  @Override
  public double getBlue() {
    return this.blue;
  }
}
