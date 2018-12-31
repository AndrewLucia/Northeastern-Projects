package cs3500.animator.controller;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.view.AnimatorViewSVG;

/**
 * Represents the controller for the Easy Animator to output the animation as an SVG file.
 */
public class AnimatorControllerSVG implements AnimatorController {

  //represents the Easy Animator model
  private AnimatorModel model;
  //represents the Easy Animator View for SVG output
  private AnimatorViewSVG view;

  /**
   * Instantiates the SVG controller for the Easy Animator with the given model and SVG view.
   *
   * @param model the animator model
   * @param view the animator SVG view
   */
  public AnimatorControllerSVG(AnimatorModel model, AnimatorViewSVG view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void run(int fps) throws IllegalArgumentException {
    if (fps < 1) {
      throw new IllegalArgumentException("FPS must be positive, given: " + fps);
    } else {
      this.view.printSVG(model.getAllShapes());
    }
  }
}
