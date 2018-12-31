package cs3500.animator.view;

import cs3500.animator.model.shape.Shape2D;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Drawing panel that you draw all shapes on.
 */
public class VisualDrawingPanel extends JPanel {

  //List of shapes to be drawn.
  private List<Shape2D> toDraw;

  /**
   * Constructs the drawing panel and calls the JPanel Super.
   */
  public VisualDrawingPanel() {
    super();
    this.toDraw = new ArrayList<Shape2D>();
  }

  /**
   * Draws the given list of shapes.
   *
   * @param toDraw the list of shapes to draw
   */
  public void drawShapes(List<Shape2D> toDraw) {
    this.toDraw = toDraw;
    this.repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    for (Shape2D shape : this.toDraw) {
      shape.draw(g);
    }
  }
}
