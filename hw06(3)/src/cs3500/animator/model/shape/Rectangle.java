package cs3500.animator.model.shape;

import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.color.Color;
import java.awt.Graphics;

/**
 * Represents a two-dimensional Rectangle with a name, a shape type (rectangle), an (X,Y) position,
 * (X,Y) width and height, appearance time, disappearance time, and a color.
 */
public class Rectangle extends AbstractShape2D {

  /**
   * Constructs a two-dimensional rectangle.
   *
   * @param name the name of this rectangle
   * @param cornerX the initial x-position of the corner of this rectangle
   * @param cornerY the initial y-position of the corner of this rectangle
   * @param timeAppears the time this rectangle appears
   * @param timeDisappears the time this rectangle disappears
   * @param color the color of this rectangle
   * @param width the width of this rectangle
   * @param height the height of this rectangle
   * @throws IllegalArgumentException if the given name, type, or color are null
   * @throws IllegalArgumentException if the given times are not positive
   * @throws IllegalArgumentException if the time of disappearing is before appearing
   * @throws IllegalArgumentException if the x or y dimensions are not positive
   */
  public Rectangle(String name, double cornerX, double cornerY, double width,
      double height, int timeAppears, int timeDisappears,
      Color color) throws IllegalArgumentException {
    super(name, "rectangle", cornerX, cornerY,
        timeAppears, timeDisappears, color, width, height);
  }

  @Override
  public String toText(int ticks) {
    StringBuilder text = new StringBuilder();
    text.append("Name: %s" + System.lineSeparator());
    text.append("Type: %s" + System.lineSeparator());
    text.append("Corner: (%.1f,%.1f), %s, Color: %s" + System.lineSeparator());
    text.append("Appears at t=%.1fs" + System.lineSeparator());
    text.append("Disappears at t=%.1fs" + System.lineSeparator());

    return String.format(text.toString(), this.name, this.type, this.posX, this.posY,
        this.dimensionsAsText(this.dimX, this.dimY), this.color.asText(),
        ((float) this.timeAppears / ticks), ((float) this.timeDisappears / ticks));
  }

  @Override
  public Shape2D getCopy() {
    Shape2D rect = new Rectangle(this.name, this.posX, this.posY, this.dimX, this.dimY,
        this.timeAppears, this.timeDisappears, this.color);
    for (Animation animation : this.animations) {
      rect.addAnimation(animation);
    }
    return rect;
  }

  @Override
  public String dimensionsAsText(double width, double height) {
    return String.format("Width: %.1f, Height: %.1f", width, height);
  }

  @Override
  public void draw(Graphics g) {
    g.setColor(new java.awt.Color((float) this.color.getRed(), (float) this.color.getGreen(),
        (float) this.color.getBlue()));
    g.fillRect((int) posX, (int) posY, (int) dimX, (int) dimY);
  }

  @Override
  public String getSVGOpenTag() {
    StringBuilder builder = new StringBuilder();
    builder.append("<rect id=\"%s\" x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\"");
    builder.append(" style=\"fill:rgb(%d,%d,%d)\">");

    return String
        .format(builder.toString(), this.name, (int) this.posX, (int) this.posY, (int) this.dimX,
            (int) this.dimY,
            (int) (this.color.getRed() * 255), (int) (this.color.getGreen() * 255),
            (int) (this.color.getBlue() * 255));
  }

  @Override
  public String getSVGCloseTag() {
    return "</rect>";
  }
}
