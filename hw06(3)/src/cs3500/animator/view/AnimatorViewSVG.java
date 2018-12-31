package cs3500.animator.view;

import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.shape.Shape2D;
import java.io.IOException;
import java.util.List;

/**
 * SVG view for the animator.
 */
public class AnimatorViewSVG {

  //the appendable to write the SVG output to
  private Appendable appendable;

  /**
   * Constructs the SVG View.
   *
   * @param appendable the appendable to write the SVG output to
   */
  public AnimatorViewSVG(Appendable appendable) {
    this.appendable = appendable;
  }

  /**
   * Prints the SVG formatted string representation of this animation.
   *
   * @param shapes list of Shapes to print SVG for
   * @throws IllegalArgumentException if it can't write to the appendable
   */
  public void printSVG(List<Shape2D> shapes) throws IllegalArgumentException {
    StringBuilder builder = new StringBuilder();
    builder.append("<svg width=\"800\" height=\"800\" version=\"1.1\" ");

    builder.append("xmlns=\"http://www.w3.org/2000/svg\">"  + System.lineSeparator());
    for (Shape2D shape : shapes) {
      builder.append(shape.getSVGOpenTag() + System.lineSeparator());

      for (Animation animation : shape.getAnimations()) {
        builder.append(animation.getSVG() + System.lineSeparator());
      }
      builder.append(shape.getSVGCloseTag() + System.lineSeparator());
    }
    builder.append("</svg>");

    try {
      this.appendable.append(builder.toString());
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not write to specified SVG output");
    }
  }
}
