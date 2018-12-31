package cs3500.animator.model.shape;

import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.color.Color;
import java.awt.Graphics;
import java.util.List;

/**
 * Represents a two-dimensional shape. Each shape has a name, a shape type (e.g. rectangle),
 * an (X,Y) position, (X,Y) dimensions, appearance time, disappearance time, and a color.
 */
public interface Shape2D {

  /**
   * Gets the text description of this shape, with the name, type, (x,y) position,
   * (x,y) dimensions, and color.
   *
   * @return the text description of this shape
   */
  String toText(int ticksPerSecond);

  /**
   * Gets the name of this shape.
   *
   * @return the String representing the name
   */
  String getName();

  /**
   * Gets the color of this shape.
   *
   * @return the color of this shape
   */
  Color getColor();

  /**
   * Gets all animations that this shape performs.
   *
   * @return a list of all animations performed
   */
  List<Animation> getAnimations();

  /**
   * Gets the given dimensions of this shape as a text description.
   *
   * @param x the x-dimension of this shape (e.g. width, x-radius)
   * @param y the y-dimension of this shape (e.g. height, y-radius)
   * @return the text description representing the given dimensions
   */
  String dimensionsAsText(double x, double y);

  /**
   * Gets the time this shape appears.
   *
   * @return the time of appearance
   */
  int getTimeAppears();

  /**
   * Gets the time this shape disappears.
   *
   * @return the time of disappearance
   */
  int getTimeDisappears();

  /**
   * Changes the color of this shape to the given color.
   *
   * @param color the new color of this shape
   */
  void setColor(Color color);

  /**
   * Gets the x-position of this shape.
   *
   * @return the x-position
   */
  double getPosX();

  /**
   * Gets the y-position of this shape.
   *
   * @return the y-position
   */
  double getPosY();

  /**
   * Gets the x-dimension of this shape (e.g. width, x-radius).
   *
   * @return the x-dimension
   */
  double getDimX();

  /**
   * Gets the y-dimension of this shape (e.g. height, y-radius).
   *
   * @return the y-dimension
   */
  double getDimY();

  /**
   * Moves this shape to given (X,Y) position.
   *
   * @param posX the new x-position of this shape
   * @param posY the new y-position of this shape
   */
  void moveTo(double posX, double posY);

  /**
   * Scales this shape to the given (X,Y) dimensions.
   *
   * @param dimX the new x-dimension of this shape
   * @param dimY the new y-dimension of this shape
   */
  void scaleTo(double dimX, double dimY);

  /**
   * Adds a new animation to be performed on this shape.
   *
   * @param animation the animation to perform
   */
  void addAnimation(Animation animation);

  /**
   * Returns a copy of this shape with all properties of the original.
   *
   * @return the copy of this shape
   */
  Shape2D getCopy();

  /**
   * Draws the shape on the given graphics object.
   *
   * @param g the graphics to draw onto
   */
  void draw(Graphics g);

  /**
   * Returns the opening SVG tag for this shape with all shape attributes.
   *
   * @return the opening SVG tag
   */
  String getSVGOpenTag();

  /**
   * Returns the closing SVG tag for this shape.
   *
   * @return the SVG closing tag
   */
  String getSVGCloseTag();

  /**
   * Returns the type of the shape (e.g. rectangle, oval)
   *
   * @return the type of this shape
   */
  String getType();
}
