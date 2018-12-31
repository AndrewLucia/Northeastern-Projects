package cs3500.animator.model.shape;

import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.color.Color;
import java.awt.Graphics;

/**
 * Represents a two-dimensional Oval with a name, a shape type (oval), an (X,Y) position, (X,Y)
 * radii, appearance time, disappearance time, and a color.
 */
public class Oval extends AbstractShape2D {

  /**
   * Constructs a two-dimensional oval.
   *
   * @param name the name of this oval
   * @param centerX the initial x-position of the center of this oval
   * @param centerY the initial y-position of the center of this oval
   * @param timeAppear the time this oval appears
   * @param timeDisappear the time this oval disappears
   * @param color the color of this oval
   * @param radiusX the x-radius of this oval
   * @param radiusY the y-radius of this oval
   * @throws IllegalArgumentException if the given name, type, or color are null
   * @throws IllegalArgumentException if the given times are not positive
   * @throws IllegalArgumentException if the time of disappearing is before appearing
   * @throws IllegalArgumentException if the x or y dimensions are not positive
   */
  public Oval(String name, double centerX, double centerY, double radiusX, double radiusY,
      int timeAppear, int timeDisappear, Color color) throws IllegalArgumentException {
    super(name, "oval", centerX, centerY, timeAppear, timeDisappear, color, radiusX, radiusY);
  }

  @Override
  public String toText(int ticks) {
    StringBuilder text = new StringBuilder();
    text.append("Name: %s" + System.lineSeparator());
    text.append("Type: %s" + System.lineSeparator());
    text.append("Center: (%.1f,%.1f), %s, Color: %s" + System.lineSeparator());
    text.append("Appears at t=%.1fs" + System.lineSeparator());
    text.append("Disappears at t=%.1fs" + System.lineSeparator());

    return String.format(text.toString(), this.name, this.type, this.posX, this.posY,
        this.dimensionsAsText(this.dimX, this.dimY), this.color.asText(),
        ((float) this.timeAppears / ticks), ((float) this.timeDisappears / ticks));
  }

  @Override
  public Shape2D getCopy() {
    Shape2D oval = new Oval(this.name, this.posX, this.posY, this.dimX, this.dimY,
        this.timeAppears, this.timeDisappears, this.color);
    for (Animation animation : this.animations) {
      oval.addAnimation(animation);
    }
    return oval;
  }

  @Override
  public String dimensionsAsText(double radiusX, double radiusY) {
    return String.format("X radius: %.1f, Y radius: %.1f", radiusX, radiusY);
  }

  @Override
  public void draw(Graphics g) {
    g.setColor(new java.awt.Color((float) this.color.getRed(), (float) this.color.getGreen(),
        (float) this.color.getBlue()));
    g.fillOval((int) posX, (int) posY, (int) dimX, (int) dimY);
  }

  @Override
  public String getSVGOpenTag() {
    StringBuilder builder = new StringBuilder();
    builder.append("<ellipse cx=\"%d\" cy=\"%d\" rx=\"%d\" ry=\"%d\"");
    builder.append(" style=\"fill:rgb(%d,%d,%d)\">");
    return String.format(builder.toString(), (int) this.posX, (int) this.posY, (int) this.dimX,
        (int) this.dimY,
        (int) (this.color.getRed() * 255), (int) (this.color.getGreen() * 255),
        (int) (this.color.getBlue() * 255));
  }

  @Override
  public String getSVGCloseTag() {
    return "</ellipse>";
  }
}
