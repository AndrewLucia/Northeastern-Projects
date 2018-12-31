package cs3500.animator.model.animation;

import cs3500.animator.model.color.Color;
import cs3500.animator.model.color.ColorRGB;
import cs3500.animator.model.shape.Shape2D;

/**
 * Represents an animation that changes the color of a shape over a certain period of time.
 */
public class AnimationColorChange extends AbstractAnimation {

  //represents the original color of the shape
  private Color oldColor;
  //represents the new color of the shape
  private Color newColor;

  /**
   * Constructs this color changing animation over a specific period of time.
   *
   * @param shape the shape to change color
   * @param oldColor the old color the shape changes from
   * @param newColor the new color the shape changes to
   * @param timeStart the time to start changing color
   * @param timeEnd the time to stop changing color
   * @throws IllegalArgumentException if the given end time is earlier than start
   * @throws IllegalArgumentException if the shape is not visible within the given time interval
   */
  public AnimationColorChange(Shape2D shape, Color oldColor, Color newColor,
      int timeStart, int timeEnd) throws IllegalArgumentException {
    super(shape, "change-color", timeStart, timeEnd);
    this.newColor = newColor;
    this.oldColor = oldColor;
  }

  @Override
  public String toText(int ticks) {
    String text = "Shape %s changes color from %s to %s from t=%.1fs to t=%.1fs";
    return String.format(text, this.shape.getName(), this.oldColor.asText(), this.newColor.asText(),
        ((float) this.timeStart / ticks), ((float) this.timeEnd / ticks));
  }

  @Override
  public void animate(Shape2D theShape, int time) {
    if (time <= this.timeEnd && time >= this.timeStart) {
      double timeTotal = this.timeEnd - this.timeStart;
      double red = (this.oldColor.getRed() * ((this.timeEnd - time) / timeTotal))
          + (this.newColor.getRed() * ((time - this.timeStart) / timeTotal));
      double green = (this.oldColor.getGreen() * ((this.timeEnd - time) / timeTotal))
          + (this.newColor.getGreen() * ((time - this.timeStart) / timeTotal));
      double blue = (this.oldColor.getBlue() * ((this.timeEnd - time) / timeTotal))
          + (this.newColor.getBlue() * ((time - this.timeStart) / timeTotal));
      theShape.setColor(new ColorRGB(red, green, blue));
    }
  }

  @Override
  public String getSVG() {
    StringBuilder builder = new StringBuilder();

    builder.append("<animate attributeType=\"xml\" begin=\"%dms\" dur=\"%dms\" "
        + "attributeName=\"fill\" from=\"rgb(%d,%d,%d)\" to=\"rgb(%d,%d,%d)\" fill=\"freeze\" />");

    return String
        .format(builder.toString(), this.timeStart * 100, (this.timeEnd - this.timeStart) * 100,
            (int) (this.oldColor.getRed() * 255), (int) (this.oldColor.getGreen() * 255),
            (int) (this.oldColor.getBlue() * 255), (int) (this.newColor.getRed() * 255),
            (int) (this.newColor.getGreen() * 255), (int) (this.newColor.getBlue() * 255));
  }
}
