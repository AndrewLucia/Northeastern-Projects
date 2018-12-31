package cs3500.animator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import cs3500.animator.controller.AnimatorController;
import cs3500.animator.controller.AnimatorControllerSVG;
import cs3500.animator.controller.AnimatorControllerTextual;
import cs3500.animator.controller.AnimatorControllerVisual;
import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.util.AnimationFileReader;
import cs3500.animator.view.AnimatorViewSVG;
import cs3500.animator.view.AnimatorViewTextual;
import cs3500.animator.view.AnimatorViewVisual;

/**
 * EasyAnimator class to initialize the Animator.
 */
public class EasyAnimator {

  /**
   * EasyAnimator method to initialize the Animator. Valid input includes: Each input set is denoted
   * between "<" and ">" signs. These signs themselves are not part of the input. The sets may
   * appear in any order (e.g. the -iv set can appear first, followed by -if and so on). Within a
   * set, the input is in order. That is, if the user types -if then the next input must be the name
   * of an input file, and so on. Providing an input file (the -if set) and a view (the -iv set) are
   * mandatory. If the output set is not specified and the view needs it, the default should be
   * System.out. If the speed is not specified and the view needs it, the default is 1.
   * Example inputs: -if smalldemo.txt -iv text -o out -speed 2 -> use smalldemo.txt for the
   * animation file, and open a text view with its output going to System.out, and a speed of 2
   * ticks per second.
   * -iv svg -o out.svg -if buildings.txt -> use buildings.txt for the animation file, and open an
   * SVG view with its output going to the file out.svg, with a speed of 1 tick per second.
   * -if smalldemo.txt -iv text -> use smalldemo.txt for the animation file, and open a text view
   * with its output going to System.out.
   * -if smalldemo.txt -speed 50 -iv visual -> use smalldemo.txt for the animation file, and open a
   * visual view to show the animation at a speed of 50 ticks per second.
   *
   * @param args program arguments given in the form <-if name-of-animation-file> <-iv type-of-view>
   *     <-o where-output-show-go> <-speed integer-ticks-per-second>
   */
  public static void main(String[] args) {
    if (args.length % 2 != 0) { //ensures each input has a corresponding value
      throw new IllegalArgumentException("Unknown odd number of commands given");
    } else if (args.length < 4) {
      throw new IllegalArgumentException("Need at least 4 arguments.");
    } else {
      Appendable out = System.out; //default to System.out
      String fileInput = null; //mandatory input
      String view = null; //mandatory input
      int speed = 1; //default to 1

      for (int i = 0; i < args.length - 1; i += 2) {
        switch (args[i]) {
          case "-if":
            fileInput = args[i + 1];
            break;
          case "-iv":
            view = args[i + 1];
            break;
          case "-speed":
            try { //tries to parse an integer
              speed = Integer.parseInt(args[i + 1]);
              if (speed < 1) { //checks if the speed is non-positive
                throw new IllegalArgumentException("Given speed is not positive: " + args[i + 1]);
              }
            } catch (NumberFormatException e) {
              throw new IllegalArgumentException("Given speed is not an integer: " + args[i + 1]);
            }
            break;
          case "-o":
            try {
              out = new FileWriter(args[i + 1]);
            } catch (IOException e) {
              throw new IllegalArgumentException("Could not write to file: " + args[i + 1]);
            }
            break;
          default:
            throw new IllegalArgumentException("Unknown command given: " + args[i]);
        }
      }
      if (view == null || fileInput == null) {
        throw new IllegalArgumentException("View or File Input are null.");
      }
      try {
        AnimationFileReader reader = new AnimationFileReader();
        AnimatorModel model = reader.readFile(fileInput, new AnimatorModelImpl.Builder());
        AnimatorController controller;

        switch (view) {
          case "visual":
            AnimatorViewVisual viewVisual = new AnimatorViewVisual();
            controller = new AnimatorControllerVisual(model, viewVisual);
            break;
          case "text":
            AnimatorViewTextual viewTextual = new AnimatorViewTextual(out);
            controller = new AnimatorControllerTextual(model, viewTextual);
            break;
          case "svg":
            controller = new AnimatorControllerSVG(model, new AnimatorViewSVG(out));
            break;
          default:
            throw new IllegalArgumentException("View must be specified as svg, "
                + "text, or visual, given: \"" + view + "\"");
        }
        controller.run(speed); //executes the animation
        if (out.equals(System.out)) {
          PrintStream out1 = (PrintStream) out;
          out1.close();
        } else {
          FileWriter out2 = (FileWriter) out;
          out2.close();
        }
      } catch (IOException exception) {
        throw new IllegalArgumentException(
            "Could not read from specified input file: " + fileInput);
      }
    }
  }
}
