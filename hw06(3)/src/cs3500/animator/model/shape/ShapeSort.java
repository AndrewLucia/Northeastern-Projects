package cs3500.animator.model.shape;

import java.util.Comparator;

/**
 * Represents the comparator to sort shapes according to their initial time of appearance.
 */
public class ShapeSort implements Comparator<Shape2D> {

  /**
   * Compares two shapes according to their time of appearance.
   *
   * @param o1 the first shape to be compared
   * @param o2 the second shape to be compared
   * @return a negative integer if the first shape appears first, zero if they appear at the
   *     same time, or a positive integer if the second shape appears first
   */
  @Override
  public int compare(Shape2D o1, Shape2D o2) {
    return o1.getTimeAppears() - o2.getTimeAppears();
  }
}
