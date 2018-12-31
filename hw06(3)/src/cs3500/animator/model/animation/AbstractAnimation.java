package cs3500.animator.model.animation;

import cs3500.animator.model.shape.Shape2D;

/**
 * Represents an Animation to be performed on a Shape2D over a time interval.
 */
public abstract class AbstractAnimation implements Animation {

  //represents the shape to be animated
  protected Shape2D shape;
  //represents the time at which this animation starts
  protected int timeStart;
  //represents the time at which this animation ends
  protected int timeEnd;
  //represents the type of animation this is
  protected String type;

  /**
   * Constructs an Animation to be performed on a shape over a specific time interval.
   *
   * @param shape the shape to animate
   * @param timeStart the start time of the animation
   * @param timeEnd the end time of the animation
   * @throws IllegalArgumentException if the given end time is earlier than start
   * @throws IllegalArgumentException if the shape is not visible within the given time interval
   */
  public AbstractAnimation(Shape2D shape, String type, int timeStart, int timeEnd)
      throws IllegalArgumentException {
    if (shape.getTimeAppears() > timeStart || shape.getTimeDisappears() < timeEnd) {
      throw new IllegalArgumentException("Shape must be visible at give times to animate");
    } else if (timeStart >= timeEnd) {
      throw new IllegalArgumentException("Animation must start before it can end");
    } else if (type == null) {
      throw new IllegalArgumentException("Type cannot be null");
    } else {
      this.shape = shape;
      this.type = type;
      this.timeStart = timeStart;
      this.timeEnd = timeEnd;
    }
  }

  @Override
  public boolean hasConflict() {
    for (Animation animation : this.shape.getAnimations()) {
      if (animation.getType().equals(this.getType())) {
        int start = animation.getStartTime();
        int end = animation.getEndTime();
        if (this.timeStart >= start && this.timeStart <= end) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Shape2D getShape() {
    return this.shape;
  }

  @Override
  public int getStartTime() {
    return this.timeStart;
  }

  @Override
  public int getEndTime() {
    return this.timeEnd;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public abstract String toText(int ticks);

  @Override
  public abstract void animate(Shape2D shape, int time);
}
