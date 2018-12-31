package cs3500.animator.model.animation;

import cs3500.animator.model.shape.Shape2D;

/**
 * Represents an animation that changes the size of a shape over a certain period of time.
 */
public class AnimationScale extends AbstractAnimation {

  //represents the original x-dimension of the shape before the animation
  private double oldDimX;
  //represents the original y-dimension of the shape before the animation
  private double oldDimY;
  //represents the new x-dimension of the shape after the animation
  private double newDimX;
  //represents the new y-dimension of the shape after the animation
  private double newDimY;

  /**
   * Constructs this size changing animation over a specific period of time.
   *
   * @param shape the shape to change size
   * @param oldDX the old x-dimension of the shape
   * @param oldDY the old y-dimension of the shape
   * @param newDimX the new x-dimension of the shape
   * @param newDimY the new y-dimension of the shape
   * @param timeStart the time to start animating
   * @param timeEnd the time to stop animating
   * @throws IllegalArgumentException if the given end time is earlier than start
   * @throws IllegalArgumentException if the shape is not visible within the given time interval
   */
  public AnimationScale(Shape2D shape, double oldDX, double oldDY, double newDimX, double newDimY,
      int timeStart, int timeEnd) throws IllegalArgumentException {
    super(shape, "scale", timeStart, timeEnd);
    this.newDimX = newDimX;
    this.newDimY = newDimY;
    this.oldDimX = oldDX;
    this.oldDimY = oldDY;
  }

  @Override
  public String toText(int ticks) {
    String text = "Shape %s scales from %s to %s from t=%.1fs to t=%.1fs";
    return String.format(text, this.shape.getName(),
        this.shape.dimensionsAsText(this.oldDimX, this.oldDimY),
        this.shape.dimensionsAsText(this.newDimX, this.newDimY),
        ((float) this.timeStart / ticks), ((float) this.timeEnd / ticks));
  }

  @Override
  public void animate(Shape2D theShape, int time) {
    if (time <= this.timeEnd && time >= this.timeStart) {
      double timeTotal = this.timeEnd - this.timeStart;
      double dimX = (this.oldDimX * ((this.timeEnd - time) / timeTotal))
          + (this.newDimX * ((time - this.timeStart) / timeTotal));
      double dimY = (this.oldDimY * ((this.timeEnd - time) / timeTotal))
          + (this.newDimY * ((time - this.timeStart) / timeTotal));
      theShape.scaleTo(dimX, dimY);
    }
  }

  @Override
  public String getSVG() {
    StringBuilder builder = new StringBuilder();
    switch (shape.getType()) {
      case "rectangle":
        builder.append("<animate attributeType=\"xml\" begin=\"%dms\" ");
        builder.append("dur=\"%dms\" attributeName=\"width\" from=\"%d\" to=\"%d\" ");
        builder.append("fill=\"freeze\" />" + System.lineSeparator());
        builder.append("<animate attributeType=\"xml\" begin=\"%dms\" ");
        builder.append("dur=\"%dms\" attributeName=\"height\" from=\"%d\" ");
        builder.append("to=\"%d\" fill=\"freeze\" />");
        break;
      case "oval":
        builder.append("<animate attributeType=\"xml\" begin=\"%dms\" ");
        builder.append("dur=\"%dms\" attributeName=\"rx\" from=\"%d\" to=\"%d\" ");
        builder.append("fill=\"freeze\" />" + System.lineSeparator());
        builder.append("<animate attributeType=\"xml\" begin=\"%dms\" ");
        builder.append("dur=\"%dms\" attributeName=\"ry\" from=\"%d\" ");
        builder.append("to=\"%d\" fill=\"freeze\" />");
        break;
      default:
        break;
    }
    return String
        .format(builder.toString(), this.timeStart * 100, (this.timeEnd - this.timeStart) * 100,
            (int) this.oldDimX, (int) this.newDimX, this.timeStart * 100,
            (this.timeEnd - this.timeStart) * 100,
            (int) this.oldDimY, (int) this.newDimY);
  }
}
