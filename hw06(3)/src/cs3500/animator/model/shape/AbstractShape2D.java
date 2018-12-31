package cs3500.animator.model.shape;

import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.color.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a two-dimensional shape. Each shape has a name, a shape type (e.g. rectangle),
 * an (X,Y) position, (X,Y) dimensions, appearance time, disappearance time, and a color.
 */
public abstract class AbstractShape2D implements Shape2D {
  //represents the name of this shape
  protected String name;
  //represents the type of this shape (e.g. rectangle, oval, etc.)
  protected String type;
  //represents the x-position of this shape
  protected double posX;
  //represents the y-position of this shape
  protected double posY;
  //represents the x-dimension of this shape (e.g. width, x-radius, etc.)
  protected double dimX;
  //represents the y-dimension of this shape (e.g. height, y-radius, etc.)
  protected double dimY;
  //represents the time this shape appears
  protected int timeAppears;
  //represents the time this shape disappears
  protected int timeDisappears;
  //represents the color of this shape
  protected Color color;
  //represents the list of all animations performed on this shape
  protected List<Animation> animations;

  /**
   * Constructs a two-dimensional shape.
   *
   * @param name           the name of this shape
   * @param type           the type of this shape
   * @param posX           the initial x-position of this shape
   * @param posY           the initial y-position of this shape
   * @param timeAppears    the time this shape appears
   * @param timeDisappears the time this shape disappears
   * @param color          the color of this shape
   * @param dimX           the x-dimension of this shape (e.g. width, x-radius)
   * @param dimY           the y-dimension of this shape (e.g. height, y-radius)
   * @throws IllegalArgumentException if the given name, type, or color are null
   * @throws IllegalArgumentException if the given times are not positive
   * @throws IllegalArgumentException if the time of disappearing is before appearing
   * @throws IllegalArgumentException if the x or y dimensions are not positive
   */
  public AbstractShape2D(String name, String type, double posX, double posY,
                         int timeAppears, int timeDisappears, Color color,
                         double dimX, double dimY) throws IllegalArgumentException {
    if (name == null || type == null || color == null) {
      throw new IllegalArgumentException("Name, type, color cannot be null");
    } else if (timeAppears < 0 || timeDisappears < 0) {
      throw new IllegalArgumentException("Times must positive");
    } else if (timeAppears >= timeDisappears) {
      throw new IllegalArgumentException("Time of disappearing must follow time of appearing");
    } else if (dimX < 1 || dimY < 1) {
      throw new IllegalArgumentException("Dimensions must be positive");
    } else {
      this.name = name;
      this.type = type;
      this.posX = posX;
      this.posY = posY;
      this.dimX = dimX;
      this.dimY = dimY;
      this.color = color;
      this.timeAppears = timeAppears;
      this.timeDisappears = timeDisappears;
      this.animations = new ArrayList<Animation>();
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public double getPosX() {
    return this.posX;
  }

  @Override
  public double getPosY() {
    return this.posY;
  }

  @Override
  public double getDimX() {
    return this.dimX;
  }

  @Override
  public double getDimY() {
    return this.dimY;
  }

  @Override
  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public int getTimeAppears() {
    return this.timeAppears;
  }

  @Override
  public int getTimeDisappears() {
    return this.timeDisappears;
  }

  @Override
  public void addAnimation(Animation animation) {
    this.animations.add(animation);
  }

  @Override
  public List<Animation> getAnimations() {
    return this.animations;
  }

  @Override
  public void scaleTo(double dimX, double dimY) {
    this.dimX = dimX;
    this.dimY = dimY;
  }

  @Override
  public void moveTo(double posX, double posY) {
    this.posX = posX;
    this.posY = posY;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public abstract String dimensionsAsText(double xDimension, double yDimension);

  @Override
  public abstract String toText(int ticks);

  @Override
  public abstract String getSVGOpenTag();

  @Override
  public abstract String getSVGCloseTag();

  @Override
  public abstract Shape2D getCopy();

  @Override
  public abstract void draw(Graphics g);
}
