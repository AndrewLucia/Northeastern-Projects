package cs3500.animator.model.animation;

import cs3500.animator.model.shape.Shape2D;

/**
 * Represents an animation to be performed on a shape over a time interval.
 */
public interface Animation {

  /**
   * Gets the shape that performs this animation.
   *
   * @return the shape
   */
  Shape2D getShape();

  /**
   * Gets the text description of this animation. The description includes the animating shape, the
   * operation being performed, and the time interval of animation.
   *
   * @param ticksPerSecond the number of ticks per second
   * @return the text description
   */
  String toText(int ticksPerSecond);

  /**
   * Performs this animation on the given shape for the given time.
   *
   * @param shape the shape to animate
   * @param time the time to animate this shape for
   */
  void animate(Shape2D shape, int time);

  /**
   * Gets the start time (t) of this animation. Time returned will always be non-negative.
   *
   * @return the start time
   */
  int getStartTime();

  /**
   * Gets the end time (t) of this animation. Time returned will always be non-negative.
   *
   * @return the end time
   */
  int getEndTime();

  /**
   * Returns true if this animation conflicts with another animation of this animating shape.
   * Conflicting animations are two animations of the same operation (e.g. move, scale, change
   * color) that animate the same shape during overlapping time intervals.
   *
   * @return whether or not a conflict exists
   */
  boolean hasConflict();

  /**
   * Gets this type of animation, represented as a String (e.g. "move", "change-color", "scale").
   *
   * @return the type of animation
   */
  String getType();

  /**
   * Gets this animation in an SVG format.
   *
   * @return the SVG format of this animation
   */
  String getSVG();
}
