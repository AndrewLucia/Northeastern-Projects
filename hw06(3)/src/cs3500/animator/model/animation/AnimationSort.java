package cs3500.animator.model.animation;

import java.util.Comparator;

/**
 * Represents the comparator to sort animations according to their start time.
 */
public class AnimationSort implements Comparator<Animation> {

  /**
   * Compares two animations according to their start time.
   *
   * @param o1 the first animation to be compared
   * @param o2 the second animation to be compared
   * @return a negative integer if the first animation start first, zero if they start at the
   *     same time, or a positive integer if the second animation starts first
   */
  @Override
  public int compare(Animation o1, Animation o2) {
    return o1.getStartTime() - o2.getStartTime();
  }
}
