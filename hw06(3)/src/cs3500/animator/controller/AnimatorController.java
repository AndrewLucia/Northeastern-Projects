package cs3500.animator.controller;

/**
 * Represents the controller for the Easy Animator. The controller executes an animation when the
 * run method is called.
 */
public interface AnimatorController {

  /**
   * Runs this Animation at the given number of ticks per second.
   *
   * @param ticksPerSecond the number of ticks per second
   * @throws IllegalArgumentException if the number of ticks per second is not positive
   */
  void run(int ticksPerSecond) throws IllegalArgumentException;
}
