package cs3500.animator.controller;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.view.AnimatorViewTextual;

/**
 * Represents the controller for the Easy Animator to output the animation as a textual
 * description.
 */
public class AnimatorControllerTextual implements AnimatorController {

  //represents the Easy Animator model
  private AnimatorModel model;
  //represents the Easy Animator View for textual output
  private AnimatorViewTextual view;

  /**
   * Instantiates the textual controller for the Easy Animator with the given model and text view.
   *
   * @param model the animator model
   * @param view the animator textual view
   */
  public AnimatorControllerTextual(AnimatorModel model, AnimatorViewTextual view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void run(int fps) throws IllegalArgumentException {
    if (fps < 1) {
      throw new IllegalArgumentException("FPS must be positive, given: " + fps);
    } else {
      this.view.getDescription(model.getAllShapes(), fps);
    }
  }
}
