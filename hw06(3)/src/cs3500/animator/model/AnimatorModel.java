package cs3500.animator.model;

import java.util.List;

import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.animation.Frame;
import cs3500.animator.model.shape.Shape2D;

/**
 * Represents the model for the Easy Animator. The model keeps track of all animations,
 * shapes, and their respective states over time.
 */
public interface AnimatorModel {

  /**
   * Adds a new shape to this animation.
   *
   * @param shape the new shape
   * @throws IllegalArgumentException if the given shape is null
   */
  void addShape(Shape2D shape) throws IllegalArgumentException;

  /**
   * Adds a new animation to this animation.
   *
   * @param animation the new animation
   * @throws IllegalArgumentException if the given animation is null
   * @throws IllegalArgumentException if the given animation has a time conflict
   */
  void addAnimation(Animation animation) throws IllegalArgumentException;

  /**
   * Checks if all animations have been completed for the given time (t).
   *
   * @return whether or not the animation is over
   */
  boolean isAnimationOver(int time);

  /**
   * Gets the animation frame for the given time. One frame represents one
   * tick in an animation and holds all shapes for that given time.
   *
   * @param time the time of the frame to return
   * @return the frame
   */
  Frame getFrame(int time);

  /**
   * Gets all shapes for this model. The list is returned in the order shapes
   * should be drawn.
   *
   * @return the list of all shapes
   */
  List<Shape2D> getAllShapes();
}
