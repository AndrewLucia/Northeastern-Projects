package cs3500.animator.model.animation;

import cs3500.animator.model.shape.Shape2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents one frame in an animation. A frame holds a list of shapes representing the shapes
 * attributes at that time in the animation.
 */
public class Frame {

  //represents all shapes in this animation
  private Map<String, Shape2D> shapes;
  //represents all shapes in this animation in the order they should be drawn
  private List<Shape2D> shapesList;

  /**
   * Constructs a frame with no shapes.
   */
  public Frame() {
    this.shapes = new HashMap<String, Shape2D>();
    this.shapesList = new ArrayList<Shape2D>();
  }

  /**
   * Adds a shape to this Frame.
   *
   * @param shape the shape to add to this frame
   */
  public void addShape(String name, Shape2D shape) {
    this.shapes.put(name, shape);
    this.shapesList.add(shape);
  }

  /**
   * Gets a map of all shapes from this frame. Each shape is stored with its name (as a String) as
   * its key. The value is the shape.
   *
   * @return the list of all shapes in this frame
   */
  public Map<String, Shape2D> getShapesMap() {
    return this.shapes;
  }

  public List<Shape2D> getShapesList() {
    return this.shapesList;
  }
}
