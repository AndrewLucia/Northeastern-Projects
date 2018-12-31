import cs3500.animator.controller.AnimatorController;
import cs3500.animator.controller.AnimatorControllerTextual;
import cs3500.animator.model.AnimatorModel;
import cs3500.animator.model.AnimatorModelImpl;
import cs3500.animator.view.AnimatorViewTextual;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TextualViewTest {

  @Test
  public void testTextualView() {
    StringBuilder out = new StringBuilder();
    AnimatorViewTextual viewTextual = new AnimatorViewTextual(out);
    AnimatorModel model = new AnimatorModelImpl.Builder().addOval(
        "O", 5, 5, 10, 10, 0,
        0, 0, 10, 20).build();
    AnimatorController controller = new AnimatorControllerTextual(model, viewTextual);
    controller.run(1);
    assertThat(out.toString(), is("Shapes: " + System.lineSeparator()
        + "Name: O" + System.lineSeparator()
        + "Type: oval" + System.lineSeparator()
        + "Center: (5.0,5.0), X radius: 10.0, Y radius: 10.0, Color: (0.0,0.0,0.0)"
        + System.lineSeparator() + "Appears at t=10.0s" + System.lineSeparator()
        + "Disappears at t=20.0s" + System.lineSeparator() + System.lineSeparator()));
    //--------------------------------------------------------------------------
    out = new StringBuilder();
    model = new AnimatorModelImpl.Builder().addRectangle(
        "R", 5, 5, 10, 10, 0,
        0, 0, 10, 20).build();
    viewTextual = new AnimatorViewTextual(out);
    controller = new AnimatorControllerTextual(model, viewTextual);
    controller.run(40);
    assertEquals(out.toString(), "Shapes: " + System.lineSeparator()
        + "Name: R" + System.lineSeparator()
        + "Type: rectangle" + System.lineSeparator()
        + "Corner: (5.0,5.0), Width: 10.0, Height: 10.0, Color: (0.0,0.0,0.0)"
        + System.lineSeparator() + "Appears at t=0.3s" + System.lineSeparator()
        + "Disappears at t=0.5s" + System.lineSeparator() + System.lineSeparator());
    //------------------------------------------------------------------------
    out = new StringBuilder();
    viewTextual = new AnimatorViewTextual(out);
    model = new AnimatorModelImpl.Builder().addRectangle(
        "R", 5, 5, 10, 10, 0,
        0, 0, 10, 20).addMove("R", 5, 5,
        10, 10, 10, 20).addColorChange("R", 0,
        0, 0, 1, 1, 1, 11, 15).addScaleToChange(
        "R", 10, 10, 20, 20, 12, 18).build();
    controller = new AnimatorControllerTextual(model, viewTextual);
    controller.run(40);
    assertEquals(out.toString(), "Shapes: " + System.lineSeparator()
        + "Name: R" + System.lineSeparator()
        + "Type: rectangle" + System.lineSeparator()
        + "Corner: (5.0,5.0), Width: 10.0, Height: 10.0, Color: (0.0,0.0,0.0)" + System
        .lineSeparator()
        + "Appears at t=0.3s" + System.lineSeparator()
        + "Disappears at t=0.5s" + System.lineSeparator()
        + "" + System.lineSeparator()
        + "Shape R moves from (5.0,5.0) to (10.0,10.0) from t=0.3s to t=0.5s" + System
        .lineSeparator()
        + "Shape R changes color from (0.0,0.0,0.0) to (1.0,1.0,1.0) from t=0.3s to t=0.4s" + System
        .lineSeparator()
        + "Shape R scales from Width: 10.0, Height: 10.0 to Width: 20.0, Height: 20.0 from t=0.3s "
        + "to t=0.4s" + System.lineSeparator());
    //--------------------------------------------------------------------------
    out = new StringBuilder();
    viewTextual = new AnimatorViewTextual(out);
    model = new AnimatorModelImpl.Builder().addRectangle(
        "R", 5, 5, 10, 10, 0,
        0, 0, 10, 20).addOval("O", 1, 1, 12,
        12, 0, 0, 0, 10, 20).addMove(
        "O", 5, 5, 10, 10, 10, 20)
        .addColorChange("O", 0, 0, 0, 1, 1, 1,
            11, 15).addScaleToChange("O", 10, 10, 20,
            20, 12, 18).build();
    controller = new AnimatorControllerTextual(model, viewTextual);
    controller.run(40);
    assertEquals(out.toString(), "Shapes: " + System.lineSeparator()
        + "Name: R" + System.lineSeparator()
        + "Type: rectangle" + System.lineSeparator()
        + "Corner: (5.0,5.0), Width: 10.0, Height: 10.0, Color: (0.0,0.0,0.0)" + System
        .lineSeparator()
        + "Appears at t=0.3s" + System.lineSeparator()
        + "Disappears at t=0.5s" + System.lineSeparator()
        + "" + System.lineSeparator()
        + "Name: O" + System.lineSeparator()
        + "Type: oval" + System.lineSeparator()
        + "Center: (1.0,1.0), X radius: 12.0, Y radius: 12.0, Color: (0.0,0.0,0.0)" + System
        .lineSeparator()
        + "Appears at t=0.3s" + System.lineSeparator()
        + "Disappears at t=0.5s" + System.lineSeparator()
        + "" + System.lineSeparator()
        + "Shape O moves from (5.0,5.0) to (10.0,10.0) from t=0.3s to t=0.5s" + System
        .lineSeparator()
        + "Shape O changes color from (0.0,0.0,0.0) to (1.0,1.0,1.0) from t=0.3s to t=0.4s" + System
        .lineSeparator()
        + "Shape O scales from X radius: 10.0, Y radius: 10.0 to X radius: 20.0, Y radius: 20.0 "
        + "from t=0.3s to t=0.4s" + System.lineSeparator());
  }
}
