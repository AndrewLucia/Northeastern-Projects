import static org.junit.Assert.assertEquals;

import cs3500.animator.controller.AnimatorController;
import cs3500.animator.controller.AnimatorControllerSVG;
import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.view.AnimatorViewSVG;
import org.junit.Test;

public class SVGViewTest {

  @Test
  public void ViewTest() {
    StringBuilder out = new StringBuilder();
    AnimatorViewSVG viewSVG = new AnimatorViewSVG(out);
    AnimatorModel model = new AnimatorModelImpl.Builder().addOval(
        "O", 5, 5, 10, 10, 0,
        0, 0, 10, 20).build();
    AnimatorController controller = new AnimatorControllerSVG(model, viewSVG);
    controller.run(1);
    assertEquals(out.toString(), "<svg width=\"800\" height=\"800\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">" + System.lineSeparator()
        + "<ellipse cx=\"5\" cy=\"5\" rx=\"10\" ry=\"10\" style=\"fill:rgb(0,0,0)\">" + System
        .lineSeparator()
        + "</ellipse>" + System.lineSeparator() + "</svg>");
    //------------------------------------------------------------------------
    out = new StringBuilder();
    model = new AnimatorModelImpl.Builder().addRectangle(
        "R", 5, 5, 10, 10, 0,
        0, 0, 10, 20).build();
    viewSVG = new AnimatorViewSVG(out);
    controller = new AnimatorControllerSVG(model, viewSVG);
    controller.run(40);
    assertEquals(out.toString(), "<svg width=\"800\" height=\"800\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">" + System.lineSeparator()
        + "<rect id=\"R\" x=\"5\" y=\"5\" width=\"10\" height=\"10\" style=\"fill:rgb(0,0,0)\">"
        + System.lineSeparator()
        + "</rect>" + System.lineSeparator()
        + "</svg>");
    //---------------------------------------------------------------
    out = new StringBuilder();
    viewSVG = new AnimatorViewSVG(out);
    model = new AnimatorModelImpl.Builder().addRectangle(
        "R", 5, 5, 10, 10, 0,
        0, 0, 10, 20).addMove("R", 5, 5,
        10, 10, 10, 20).addColorChange("R", 0,
        0, 0, 1, 1, 1, 11, 15).addScaleToChange(
        "R", 10, 10, 20, 20, 12, 18).build();
    controller = new AnimatorControllerSVG(model, viewSVG);
    controller.run(40);
    assertEquals(out.toString(), "<svg width=\"800\" height=\"800\" version=\"1.1\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">" + System.lineSeparator()
        + "<rect id=\"R\" x=\"5\" y=\"5\" width=\"10\" height=\"10\" style=\"fill:rgb(0,0,0)\">"
        + System.lineSeparator()
        + "<animate attributeType=\"xml\" begin=\"1000ms\" dur=\"1000ms\" attributeName=\"x\" "
        + "from=\"5\" to=\"10\" fill=\"freeze\" />" + System.lineSeparator()
        + "<animate attributeType=\"xml\" begin=\"1000ms\" dur=\"1000ms\" attributeName=\"y\" "
        + "from=\"5\" to=\"10\" fill=\"freeze\" />" + System.lineSeparator()
        + "<animate attributeType=\"xml\" begin=\"1100ms\" dur=\"400ms\" attributeName=\"fill\" "
        + "from=\"rgb(0,0,0)\" to=\"rgb(255,255,255)\" fill=\"freeze\" />" + System.lineSeparator()
        + "<animate attributeType=\"xml\" begin=\"1200ms\" dur=\"600ms\" attributeName=\"width\" "
        + "from=\"10\" to=\"20\" fill=\"freeze\" />" + System.lineSeparator()
        + "<animate attributeType=\"xml\" begin=\"1200ms\" dur=\"600ms\" attributeName=\"height\" "
        + "from=\"10\" to=\"20\" fill=\"freeze\" />" + System.lineSeparator()
        + "</rect>" + System.lineSeparator()
        + "</svg>");
    //--------------------------------------------------------------------------------
    out = new StringBuilder();
    viewSVG = new AnimatorViewSVG(out);
    model = new AnimatorModelImpl.Builder().addRectangle(
        "R", 5, 5, 10, 10, 0,
        0, 0, 10, 20).addOval(
        "O", 1, 1, 12, 12, 0, 0, 0,
        10, 20).addMove("O", 5, 5,
        10, 10, 10, 20).addColorChange("O", 0,
        0, 0, 1, 1, 1, 11, 15).addScaleToChange(
        "O", 10, 10, 20, 20, 12, 18).build();
    controller = new AnimatorControllerSVG(model, viewSVG);
    controller.run(40);
    assertEquals(out.toString(),
        "<svg width=\"800\" height=\"800\" version=\"1.1\" "
            + "xmlns=\"http://www.w3.org/2000/svg\">" + System.lineSeparator()
            + "<rect id=\"R\" x=\"5\" y=\"5\" width=\"10\" height=\"10\" style=\"fill:rgb(0,0,0)\">"
            + System.lineSeparator()
            + "</rect>" + System.lineSeparator()
            + "<ellipse cx=\"1\" cy=\"1\" rx=\"12\" ry=\"12\" style=\"fill:rgb(0,0,0)\">" + System
            .lineSeparator()
            + "<animate attributeType=\"xml\" begin=\"1000ms\" dur=\"1000ms\" attributeName=\"cx\" "
            + "from=\"5\" to=\"10\" fill=\"freeze\" />" + System.lineSeparator()
            + "<animate attributeType=\"xml\" begin=\"1000ms\" dur=\"1000ms\" attributeName=\"cy\" "
            + "from=\"5\" to=\"10\" fill=\"freeze\" />" + System.lineSeparator()
            + "<animate attributeType=\"xml\" begin=\"1100ms\" dur=\"400ms\" attributeName=\"fill\""
            + " from=\"rgb(0,0,0)\" to=\"rgb(255,255,255)\" fill=\"freeze\" />"
            + System.lineSeparator()
            + "<animate attributeType=\"xml\" begin=\"1200ms\" dur=\"600ms\" attributeName=\"rx\" "
            + "from=\"10\" to=\"20\" fill=\"freeze\" />"
            + System.lineSeparator()
            + "<animate attributeType=\"xml\" begin=\"1200ms\" dur=\"600ms\" attributeName=\"ry\" "
            + "from=\"10\" to=\"20\" fill=\"freeze\" />"
            + System.lineSeparator()
            + "</ellipse>" + System.lineSeparator()
            + "</svg>");

  }
}
