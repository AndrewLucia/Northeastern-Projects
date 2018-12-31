package cs3500.animator.model.color;

/**
 * Represents a color with red, green, and blue values.
 */
public interface Color {

  /**
   * Gets the red value of this color represented in RGB.
   *
   * @return the red value of this color
   */
  double getRed();

  /**
   * Gets the green value of this color represented in RGB.
   *
   * @return the green value of this color
   */
  double getGreen();

  /**
   * Gets the blue value of this color represented in RGB.
   *
   * @return the blue value of this color
   */
  double getBlue();

  /**
   * Returns this color as a String in the format (R,G,B), where R is the red
   * value of this color, G is the green value of this color, B is the blue value of
   * this color.
   *
   * @return the string representation of this color
   */
  String asText();
}
