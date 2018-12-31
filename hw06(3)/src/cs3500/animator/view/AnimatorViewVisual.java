package cs3500.animator.view;

import cs3500.animator.model.animation.Frame;
import javax.swing.JFrame;

/**
 * Visual view of the animation.
 */
public class AnimatorViewVisual extends JFrame {

  //Drawing panel to draw stuff on.
  private VisualDrawingPanel panel;

  /**
   * Constructs the visual view.
   */
  public AnimatorViewVisual() {
    this.setSize(800, 800);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.panel = new VisualDrawingPanel();
    this.add(panel);
    this.setVisible(true);
  }

  /**
   * Draws all the shapes in the given Frame.
   *
   * @param frame the Frame to be drawn
   */
  public void drawFrame(Frame frame) {
    this.panel.drawShapes(frame.getShapesList());
  }
}
