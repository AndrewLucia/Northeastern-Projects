
import cs3500.marblesolitaire.controller.MarbleSolitaireController;
import cs3500.marblesolitaire.controller.MarbleSolitaireControllerImpl;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelImpl;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Test;

public class TestMarbleSolitaireController {

  //testing a valid constructor;
  @Test
  public void testConstructor() {
    boolean thrown = false;
    try {
      MarbleSolitaireController controller =
          new MarbleSolitaireControllerImpl(new InputStreamReader(System.in), System.out);
    } catch (IllegalArgumentException e) {
      thrown = true;
    }
    assertThat(thrown, is(false));
  }

  //testing an invalid constructor.
  @Test
  public void testConstructor2() {
    boolean thrown = false;
    try {
      MarbleSolitaireController controller =
          new MarbleSolitaireControllerImpl(null, null);
    } catch (IllegalArgumentException e) {
      thrown = true;
    }
    assertThat(thrown, is(true));
  }

  //testing state exceptions for readable
  @Test
  public void testStateException() {
    boolean thrown = false;
    MarbleSolitaireModel model = new MarbleSolitaireModelImpl();
    StringReader s = new StringReader("");
    MarbleSolitaireController controller =
        new MarbleSolitaireControllerImpl(s, System.out);
    try {
      controller.playGame(model);
    } catch (IllegalStateException e) {
      thrown = true;
    }
    assertThat(thrown, is(true));
  }

  //testing state exception for appendable
  @Test
  public void testStateException2() {
    boolean thrown = false;
    MarbleSolitaireModel model = new MarbleSolitaireModelImpl();
    BufferedWriter b = new BufferedWriter(new StringWriter());
    MarbleSolitaireController controller =
        new MarbleSolitaireControllerImpl(new InputStreamReader(System.in), b);
    try {
      try {
        b.close();
      } catch (IOException e) {
        fail("caught an IO exception");
      }
      controller.playGame(model);
    } catch (IllegalStateException e) {
      thrown = true;
    }
    assertThat(thrown, is(true));
  }

  //test making a valid move with spaces and ending with quit
  @Test
  public void testValid() {
    boolean thrown = false;
    MarbleSolitaireModel model = new MarbleSolitaireModelImpl();
    StringReader s = new StringReader("2  4 4 4\nq");
    StringBuilder b = new StringBuilder();
    MarbleSolitaireController controller =
        new MarbleSolitaireControllerImpl(s, b);
    try {
      controller.playGame(model);
    } catch (IllegalStateException e) {
      thrown = true;
    }
    assertThat(thrown, is(false));
    assertThat(b.toString(), is("    O O O\n"
        + "    O O O\n"
        + "O O O O O O O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score:32\n"
        + "    O O O\n"
        + "    O X O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score:31\n"
        + "Game quit!\n"
        + "State of game when quit:\n"
        + "    O O O\n"
        + "    O X O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score: 31\n"));
  }

  //test making a valid move with newline characters ending in quit
  @Test
  public void testValid2() {
    boolean thrown = false;
    MarbleSolitaireModel model = new MarbleSolitaireModelImpl();
    StringReader s = new StringReader("2\n4\n4\n4\nq");
    StringBuilder b = new StringBuilder();
    MarbleSolitaireController controller =
        new MarbleSolitaireControllerImpl(s, b);
    try {
      controller.playGame(model);
    } catch (IllegalStateException e) {
      thrown = true;
    }
    assertThat(thrown, is(false));
    assertThat(b.toString(), is("    O O O\n"
        + "    O O O\n"
        + "O O O O O O O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score:32\n"
        + "    O O O\n"
        + "    O X O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score:31\n"
        + "Game quit!\n"
        + "State of game when quit:\n"
        + "    O O O\n"
        + "    O X O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score: 31\n"));
  }

  //tests making a move with an invalid fromRow and then asking for a valid fromRow
  @Test
  public void testInvalid() {
    boolean thrown = false;
    MarbleSolitaireModel model = new MarbleSolitaireModelImpl();
    StringReader s = new StringReader("a \n2\n4 4 4 q");
    StringBuilder b = new StringBuilder();
    MarbleSolitaireController controller =
        new MarbleSolitaireControllerImpl(s, b);
    try {
      controller.playGame(model);
    } catch (IllegalStateException e) {
      thrown = true;
    }
    assertThat(thrown, is(false));
    assertThat(b.toString(), is("    O O O\n"
        + "    O O O\n"
        + "O O O O O O O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score:32\n"
        + "fromRow is not a valid int. Input a valid fromRow.\n"
        + "    O O O\n"
        + "    O X O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score:31\n"
        + "Game quit!\n"
        + "State of game when quit:\n"
        + "    O O O\n"
        + "    O X O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score: 31\n"));
  }

  //tests making a move with an invalid fromCol and then asking for a valid fromCol and quitting
  @Test
  public void testInvalidQuit() {
    boolean thrown = false;
    MarbleSolitaireModel model = new MarbleSolitaireModelImpl();
    StringReader s = new StringReader("2 a q");
    StringBuilder b = new StringBuilder();
    MarbleSolitaireController controller =
        new MarbleSolitaireControllerImpl(s, b);
    try {
      controller.playGame(model);
    } catch (IllegalStateException e) {
      thrown = true;
    }
    assertThat(thrown, is(false));
    assertThat(b.toString(), is("    O O O\n"
        + "    O O O\n"
        + "O O O O O O O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score:32\n"
        + "fromCol is not a valid int. Input a valid fromCol.\n"
        + "Game quit!\n"
        + "State of game when quit:\n"
        + "    O O O\n"
        + "    O O O\n"
        + "O O O O O O O\n"
        + "O O O X O O O\n"
        + "O O O O O O O\n"
        + "    O O O\n"
        + "    O O O\n"
        + "Score: 32\n"));
  }

  @Test
  public void testGameOver() {
    boolean thrown = false;
    MarbleSolitaireModel model = new MarbleSolitaireModelImpl();
    StringReader s = new StringReader("2 4 4 4 3 6 3 4 1 5 3 5 4 5 2 5 6 5 4 5 5 7 5 5 5 4"
        + " 5 6 3 7 5 7 5 7 5 5 3 3 3 5 3 1 3 3 5 2 5 4 5 4 5 6 5 6 3 6 3 6 3 4 3 4 3 2 7 3 5"
        + " 3 4 3 6 3 7 5 7 3 7 3 5 3 5 1 3 1 3 1 3 3 1 3 1 5 1 5 3 5 3 5 5 5 2 3 4 3 4 3 6 3"
        + " 6 3 6 5 6 5 4 5 4 5 4 3 4 2 4 4");
    StringBuilder b = new StringBuilder();
    MarbleSolitaireController controller =
        new MarbleSolitaireControllerImpl(s, b);
    try {
      controller.playGame(model);
    } catch (IllegalStateException e) {
      thrown = true;
    }
    assertThat(b.substring(b.length() - 102), is("Game over!\n"
        + "    X X X\n"
        + "    X X X\n"
        + "X X X X X X X\n"
        + "X X X O X X X\n"
        + "X X X X X X X\n"
        + "    X X X\n"
        + "    X X X\n"
        + "Score: 1\n"));
  }

}
