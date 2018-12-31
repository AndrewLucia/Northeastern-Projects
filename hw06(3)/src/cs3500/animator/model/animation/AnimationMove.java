package cs3500.animator.model.animation;

import cs3500.animator.model.shape.Shape2D;

/**
 * Represents an animation that changes the position of a shape over a certain period of time.
 */
public class AnimationMove extends AbstractAnimation {

  //represents the new x-position of the shape at after the animation
  private double newPosX;
  //represents the new y-position of the shape at after the animation
  private double newPosY;
  //represents the original x-position of the shape at before the animation
  private double oldPosX;
  //represents the original y-position of the shape at before the animation
  private double oldPosY;

  /**
   * Constructs this position changing animation over a specific period of time.
   *
   * @param shape the shape to change color
   * @param oldPosX the old x-position the shape moves from
   * @param oldPosY the old y-position the shape moves from
   * @param newPosX the new x-position to move the shape to
   * @param newPosY the new y-position to move the shape to
   * @param timeStart the time to start changing color
   * @param timeEnd the time to stop changing color
   * @throws IllegalArgumentException if the given end time is earlier than start
   * @throws IllegalArgumentException if the shape is not visible within the given time interval
   */
  public AnimationMove(Shape2D shape, double oldPosX, double oldPosY, double newPosX,
      double newPosY, int timeStart, int timeEnd) throws IllegalArgumentException {
    super(shape, "move", timeStart, timeEnd);
    this.newPosX = newPosX;
    this.newPosY = newPosY;
    this.oldPosX = oldPosX;
    this.oldPosY = oldPosY;
  }

  @Override
  public String toText(int ticks) {
    String text = "Shape %s moves from (%.1f,%.1f) to (%.1f,%.1f) from t=%.1fs to t=%.1fs";
    return String.format(text, this.shape.getName(), this.oldPosX, this.oldPosY, this.newPosX,
        this.newPosY, ((float) this.timeStart / ticks), ((float) this.timeEnd / ticks));
  }

  @Override
  public void animate(Shape2D theShape, int time) {
    if (time <= this.timeEnd && time >= this.timeStart) {
      double timeTotal = this.timeEnd - this.timeStart;
      double posX = (this.oldPosX * ((this.timeEnd - time) / timeTotal))
          + (this.newPosX * ((time - this.timeStart) / timeTotal));
      double posY = (this.oldPosY * ((this.timeEnd - time) / timeTotal))
          + (this.newPosY * ((time - this.timeStart) / timeTotal));
      theShape.moveTo(posX, posY);
    }
  }

  @Override
  public String getSVG() {
    StringBuilder builder = new StringBuilder();
    switch (shape.getType()) {
      case "rectangle":
        builder.append("<animate attributeType=\"xml\" begin=\"%dms\" ");
        builder.append("dur=\"%dms\" attributeName=\"x\" from=\"%d\" to=\"%d\" ");
        builder.append("fill=\"freeze\" />" + System.lineSeparator());
        builder.append("<animate attributeType=\"xml\" begin=\"%dms\" ");
        builder.append("dur=\"%dms\" attributeName=\"y\" from=\"%d\" to=\"%d\" fill=\"freeze\" />");
        break;
      case "oval":
        builder.append("<animate attributeType=\"xml\" begin=\"%dms\" ");
        builder.append("dur=\"%dms\" attributeName=\"cx\" from=\"%d\" to=\"%d\" ");
        builder.append("fill=\"freeze\" />" + System.lineSeparator());
        builder.append("<animate attributeType=\"xml\" begin=\"%dms\" ");
        builder
            .append("dur=\"%dms\" attributeName=\"cy\" from=\"%d\" to=\"%d\" fill=\"freeze\" />");
        break;
      default:
        break;
    }
    return String
        .format(builder.toString(), this.timeStart * 100, (this.timeEnd - this.timeStart) * 100,
            (int) this.oldPosX, (int) this.newPosX, this.timeStart * 100,
            (this.timeEnd - this.timeStart) * 100,
            (int) this.oldPosY, (int) this.newPosY);
  }
}
