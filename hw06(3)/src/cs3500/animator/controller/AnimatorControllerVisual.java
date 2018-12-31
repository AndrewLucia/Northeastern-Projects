package cs3500.animator.controller;

import cs3500.animator.model.AnimatorModel;
import cs3500.animator.view.AnimatorViewVisual;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Represents the controller for the Easy Animator to output the animation as a visual description.
 */
public class AnimatorControllerVisual implements ActionListener, AnimatorController {

  //represents the Easy Animator model
  private AnimatorModel model;
  //represents the Easy Animator View for visual output
  private AnimatorViewVisual view;
  //represents the timer to keep track of frames per second
  private Timer timer;
  //represents the number of frames per second
  private int fps;
  //represents the current frame number to be displayed
  private int frameCount;

  /**
   * Instantiates the visual controller for the Easy Animator with the given model and visual view.
   *
   * @param model the animator model
   * @param view the animator visual view
   */
  public AnimatorControllerVisual(AnimatorModel model, AnimatorViewVisual view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void run(int fps) throws IllegalArgumentException {
    if (fps < 1) {
      throw new IllegalArgumentException("FPS must be positive, given: " + fps);
    } else {
      this.fps = fps;
      this.frameCount = 0;
      this.timer = new Timer(1000 / this.fps, this);
      this.timer.start(); //starts the timer to display the visual view
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.model.isAnimationOver(this.frameCount)) {
      this.timer.stop();
    } else {
      this.view.drawFrame(this.model.getFrame(this.frameCount));
      this.frameCount++;
    }
  }
}
