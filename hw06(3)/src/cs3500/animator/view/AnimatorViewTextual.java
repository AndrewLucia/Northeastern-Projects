package cs3500.animator.view;

import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.animation.AnimationSort;
import cs3500.animator.model.shape.Shape2D;
import cs3500.animator.model.shape.ShapeSort;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Textual view for the animator.
 */
public class AnimatorViewTextual {

  //builds the textual view as a String.
  private Appendable builder;

  /**
   * Constructs the Textual View.
   *
   * @param builder builds the textual view as a String.
   */
  public AnimatorViewTextual(Appendable builder) {
    Objects.requireNonNull(builder);
    this.builder = builder;
  }

  /**
   * Gets the description of all shapes and animations and concatenates them into the View.
   *
   * @param shapes list of shapes to print the descriptions from
   * @param ticks ticks per second
   * @return the string representation of the view
   */
  public String getDescription(List<Shape2D> shapes, int ticks) {
    List<Animation> animations = new ArrayList<Animation>();
    StringBuilder description = new StringBuilder();
    description.append("Shapes: " + System.lineSeparator());
    shapes.sort(new ShapeSort()); //sorts the list of shapes

    for (Shape2D shape : shapes) { //gets the description of each shape
      description.append(shape.toText(ticks) + System.lineSeparator());
      animations.addAll(shape.getAnimations());
    }
    animations.sort(new AnimationSort());
    for (Animation animation : animations) { //gets the description of each animation
      description.append(animation.toText(ticks) + System.lineSeparator());
    }
    description.substring(0,
        description.length() - System.lineSeparator().length()); //delete trailing newline
    try {
      builder.append(description);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return description.toString();
  }
}
