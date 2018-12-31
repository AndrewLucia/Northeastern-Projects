package cs3500.animator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.animator.model.animation.Animation;
import cs3500.animator.model.animation.AnimationColorChange;
import cs3500.animator.model.animation.AnimationMove;
import cs3500.animator.model.animation.AnimationScale;
import cs3500.animator.model.animation.AnimationSort;
import cs3500.animator.model.animation.Frame;
import cs3500.animator.model.color.ColorRGB;
import cs3500.animator.model.shape.Oval;
import cs3500.animator.model.shape.Rectangle;
import cs3500.animator.model.shape.Shape2D;
import cs3500.animator.util.TweenModelBuilder;

/**
 * Represents an implementation of the Easy Animator. The animator keeps track of all shapesMap and
 * all animations to be performed over specified time intervals.
 */
public class AnimatorModelImpl implements AnimatorModel {

  //represents a map of all shapesMap, stored by their names
  private Map<String, Shape2D> shapesMap;
  //represents a list of all shapesMap in the order they should be drawn
  private List<Shape2D> shapesList;
  //represents a list of all animations to be performed
  private List<Animation> animations;
  //represents a list of frames for this whole animation
  private List<Frame> frames;
  //represents the time the animation ends
  private int timeEnd;

  /**
   * Instantiates the Animator with initial time t=0, an empty list of shapes, and an empty list of
   * animations, and empty list of frames, and an empty map of shapes.
   */
  private AnimatorModelImpl() {
    this.shapesMap = new HashMap<String, Shape2D>();
    this.animations = new ArrayList<Animation>();
    this.frames = new ArrayList<Frame>();
    this.shapesList = new ArrayList<Shape2D>();
    this.timeEnd = 0;
  }

  @Override
  public void addShape(Shape2D shape) throws IllegalArgumentException {
    if (shape == null) {
      throw new IllegalArgumentException("Shape cannot be null");
    } else {
      this.shapesList.add(shape);
      this.shapesMap.put(shape.getName(), shape); //adds the shape to the list

      for (int i = shape.getTimeAppears(); i <= shape.getTimeDisappears(); i++) {
        this.fillFrames(i); //creates all non-existent frames
        this.frames.get(i).addShape(shape.getName(), shape.getCopy()); //creates copies every frame
      }

      if (shape.getTimeDisappears() > this.timeEnd) {
        this.timeEnd = shape.getTimeDisappears(); //stores the time the last shape disappears
      }
    }
  }

  /**
   * Creates all non-existent frames from t=0 up until the given time (t) value.
   *
   * @param to the given end time to create all previous non-existent frames
   */
  private void fillFrames(int to) {
    if (to > this.frames.size() - 1) {
      while (to >= this.frames.size()) {
        this.frames.add(new Frame());
      }
    }
  }

  @Override
  public void addAnimation(Animation animation) throws IllegalArgumentException {
    if (animation == null) {
      throw new IllegalArgumentException("Animation cannot be null");
    } else if (animation.hasConflict()) { //checks for conflicting animations
      throw new IllegalArgumentException("Conflict adding animation: " + animation.toText(1));
    } else {
      this.animations.add(animation); //adds the animation to the list
      this.animations.sort(new AnimationSort()); //sorts the list by start time
      animation.getShape().addAnimation(animation); //adds given animation to its shape

      for (int i = animation.getStartTime(); i <= animation.getEndTime(); i++) {
        Shape2D shape = this.frames.get(i).getShapesMap().get(animation.getShape().getName());
        animation.animate(shape, i); //animates the shape in the frame for the given time
      }

      if (!this.shapesMap.containsKey(animation.getShape().getName())) {
        this.addShape(animation.getShape()); //ensures all shapesMap are stored in this list
      }
    }
  }

  /**
   * Animates all shapesMap in all frames following each animation end time. This ensures that all
   * shapesMap maintain their final animated state after their animation end time, until the end of
   * the entire animation.
   */
  private void fillAnimations() {
    for (Animation animation : this.animations) {
      if (animation.getEndTime() < this.timeEnd) {
        int end = this.shapesMap.get(animation.getShape().getName()).getTimeDisappears();
        for (int i = animation.getEndTime(); i <= end; i++) {
          Shape2D shape = this.frames.get(i).getShapesMap().get(animation.getShape().getName());
          animation.animate(shape, animation.getEndTime());
        }
      }
    }
  }

  @Override
  public Frame getFrame(int time) throws IllegalArgumentException {
    if (time > this.timeEnd) {
      throw new IllegalArgumentException("Given time (t=" + time + ") exceeds animation end time");
    } else {
      return this.frames.get(time);
    }
  }

  @Override
  public boolean isAnimationOver(int time) {
    return (time > this.timeEnd);
  }

  @Override
  public List<Shape2D> getAllShapes() {
    return this.shapesList;
  }

  /**
   * The builder to create an instance of AnimatorModelImpl with given shapesMap and animations.
   */
  public static final class Builder implements TweenModelBuilder<AnimatorModel> {

    private Map<String, Shape2D> shapesMap = new HashMap<String, Shape2D>();
    private List<Animation> animations = new ArrayList<Animation>();
    private List<Shape2D> shapes = new ArrayList<Shape2D>();

    @Override
    public TweenModelBuilder<AnimatorModel> addOval(String name, float cx, float cy, float xRadius,
        float yRadius, float red, float green, float blue, int startOfLife, int endOfLife) {
      Shape2D oval = new Oval(name, cx, cy, xRadius, yRadius, startOfLife, endOfLife,
          new ColorRGB(red, green, blue));
      this.shapesMap.put(name, oval);
      this.shapes.add(oval);
      return this;
    }

    @Override
    public TweenModelBuilder<AnimatorModel> addRectangle(String name, float lx, float ly,
        float width, float height, float red, float green, float blue, int startOfLife,
        int endOfLife) {
      Shape2D rectangle = new Rectangle(name, lx, ly, width, height, startOfLife, endOfLife,
          new ColorRGB(red, green, blue));
      this.shapesMap.put(name, rectangle);
      this.shapes.add(rectangle);
      return this;
    }

    @Override
    public TweenModelBuilder<AnimatorModel> addMove(String name, float moveFromX, float moveFromY,
        float moveToX, float moveToY, int startTime, int endTime) {
      this.animations.add(
          new AnimationMove(this.shapesMap.get(name), moveFromX, moveFromY, moveToX, moveToY,
              startTime, endTime));
      return this;
    }

    @Override
    public TweenModelBuilder<AnimatorModel> addColorChange(String name, float oldR, float oldG,
        float oldB, float newR, float newG, float newB, int startTime, int endTime) {
      this.animations.add(
          new AnimationColorChange(this.shapesMap.get(name), new ColorRGB(oldR, oldG, oldB),
              new ColorRGB(newR, newG, newB), startTime, endTime));
      return this;
    }

    @Override
    public TweenModelBuilder<AnimatorModel> addScaleToChange(String name, float fromSx,
        float fromSy, float toSx, float toSy, int startTime, int endTime) {
      this.animations.add(
          new AnimationScale(this.shapesMap.get(name), fromSx, fromSy, toSx, toSy, startTime,
              endTime));
      return this;
    }

    @Override
    public AnimatorModel build() {
      AnimatorModelImpl model = new AnimatorModelImpl();
      for (Shape2D shape : this.shapes) {
        model.addShape(shape);
      }
      for (Animation animation : this.animations) {
        model.addAnimation(animation);
      }
      model.fillAnimations();
      return model;
    }
  }
}


