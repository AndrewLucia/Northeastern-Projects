
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelImpl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModelImpl;
import org.junit.Test;

public class TestMarbleSolitaireModel {

  //testing invalid starting space coordinates
  @Test
  public void testConstructor() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel wrong = new MarbleSolitaireModelImpl(0, 0);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //testing valid starting space coordinates
  @Test
  public void testConstructor2() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel wrong = new MarbleSolitaireModelImpl(3, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(false));
  }


  //the from row and col are invalid
  @Test
  public void testMove1() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(0, 0, 3, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //the to row and col are invalid
  @Test
  public void testMove2() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(1, 3, 0, 0);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //the from row and col are out of array bounds
  @Test
  public void testMove3() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(-1, -1, 3, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //the to row and col are out of array bounds
  @Test
  public void testMove4() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(1, 3, -1, -1);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //all inputs are out of array bounds
  @Test
  public void testMove5() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(-1, -1, -1, -1);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //moving piece too far (more than 2 spaces) vertically
  @Test
  public void testMove6() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(0, 3, 5, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //moving piece too far (more than 2 spaces) vertically (other direction)
  @Test
  public void testMove7() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(5, 3, 0, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //moving to a slot that contains a marble
  @Test
  public void testMove8() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(0, 3, 2, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //valid move
  @Test
  public void testMove9() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(1, 3, 3, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(false));
  }

  //trying to move over a Space not over another Marble
  @Test
  public void testMove10() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();
    model1.move(1, 3, 3, 3);

    try  {
      model1.move(3, 3, 1, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //trying to move a Space object not a Marble object
  @Test
  public void testMove11() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(3, 3, 1, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //from Row way out of bounds again
  @Test
  public void testMove12() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(-2, 3, 1, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //tests diagonal move
  @Test
  public void testMove13() {
    boolean thrown = false;
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    try  {
      model1.move(4, 2, 2, 4);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //test getting score at start
  @Test
  public void testGetScore() {
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();
    assertThat(model1.getScore(), is(32));
  }

  //test getting score after some moves are made
  @Test
  public void testGetScore2() {
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();
    model1.move(1,3,3,3);
    model1.move(2,5,2,3);
    assertThat(model1.getScore(), is(30));
  }

  //test getting score after invalid move is made
  @Test
  public void testGetScore3() {
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();
    assertThat(model1.getScore(), is(32));
    try {
      model1.move(1,3,0,0);
    }
    catch (IllegalArgumentException e) {
      //just to stop the code from breaking before line below executes
    }
    assertThat(model1.getScore(), is(32));
  }

  //test starting game state
  @Test
  public void testGetGameState() {
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();
    assertThat(model1.getGameState(),
        is("    O O O\n    O O O\nO O O O O O O\nO O O X O O O\n"
            + "O O O O O O O\n    O O O\n    O O O"));

  }

  //test game state after a move is made
  @Test
  public void testGetGameState2() {
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();
    model1.move(1,3,3,3);
    assertThat(model1.getGameState(),
        is("    O O O\n    O X O\nO O O X O O O\nO O O O O O O\n"
            + "O O O O O O O\n    O O O\n    O O O"));

  }

  //test game state after invalid move is made
  @Test
  public void testGetGameState3() {
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();
    assertThat(model1.getGameState(),
        is("    O O O\n    O O O\nO O O O O O O\nO O O X O O O\n"
            + "O O O O O O O\n    O O O\n    O O O"));

    try {
      model1.move(0, 0,3,3);
    }
    catch (IllegalArgumentException e) {
      //just to stop the code from breaking before line below executes
    }
    assertThat(model1.getGameState(),
        is("    O O O\n    O O O\nO O O O O O O\nO O O X O O O\n"
            + "O O O O O O O\n    O O O\n    O O O"));

  }

  //testing is game over at the start
  @Test
  public void testIsGameOver() {
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    assertThat(model1.isGameOver(), is(false));
  }

  //testing is game over at the end
  @Test
  public void testIsGameOver2() {
    MarbleSolitaireModel model1 = new MarbleSolitaireModelImpl();

    //all the winning moves
    model1.move(1,3,3,3);
    model1.move(2,5,2,3);
    model1.move(0,4,2,4);
    model1.move(3,4,1,4);

    model1.move(5,4,3,4);

    model1.move(4,6,4,4);

    model1.move(4,3,4,5);

    model1.move(2,6,4,6);

    model1.move(4,6,4,4);

    model1.move(2,2,2,4);

    model1.move(2,0,2,2);
    model1.move(4,1,4,3);
    model1.move(4,3,4,5);
    model1.move(4,5,2,5);
    model1.move(2,5,2,3);
    model1.move(2,3,2,1);

    model1.move(6,2,4,2);

    model1.move(3,2,5,2);

    model1.move(6,4,6,2);
    model1.move(6,2,4,2);

    model1.move(4,0,2,0);
    model1.move(2,0,2,2);

    model1.move(0,2,0,4);

    model1.move(0,4,2,4);

    model1.move(2,4,4,4);

    model1.move(1,2,3,2);
    model1.move(3,2,5,2);
    model1.move(5,2,5,4);
    model1.move(5,4,3,4);
    model1.move(3,4,3,2);
    model1.move(3,1,3,3);

    assertThat(model1.isGameOver(), is(true));
  }

  //------------TRIANGLE SOLITAIRE MODEL TESTS---------------------

  //tests the base constructor with no parameters
  @Test
  public void testTriConstructor1() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel model = new TriangleSolitaireModelImpl();
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(false));
  }

  //tests the constructor with dimension parameter
  @Test
  public void testTriConstructor2() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel model = new TriangleSolitaireModelImpl(0);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //tests the constructor with dimension parameter
  @Test
  public void testTriConstructor3() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel model = new TriangleSolitaireModelImpl(1);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(false));
  }

  //tests the constructor with row, col parameters
  @Test
  public void testTriConstructor4() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel model = new TriangleSolitaireModelImpl(-1, -1);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //tests the constructor with row, col parameters
  @Test
  public void testTriConstructor5() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel model = new TriangleSolitaireModelImpl(1, 3);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //tests the constructor with row, col parameters
  @Test
  public void testTriConstructor6() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel model = new TriangleSolitaireModelImpl(3, 1);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(false));
  }

  //tests the constructor with dimension, row, col parameters
  @Test
  public void testTriConstructor7() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel model = new TriangleSolitaireModelImpl(-1, 3, 1);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //tests the constructor with dimension, row, col parameters
  @Test
  public void testTriConstructor8() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel model = new TriangleSolitaireModelImpl(4, 5, 1);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //tests the constructor with dimension, row, col parameters
  @Test
  public void testTriConstructor9() {
    boolean thrown = false;
    try {
      MarbleSolitaireModel model = new TriangleSolitaireModelImpl(5, 3, 1);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(false));
  }

  //tests the 6 correct possible moves
  @Test
  public void testTriMoves() {
    boolean thrown = false;

    //diagonally up left
    MarbleSolitaireModel model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(2, 2, 0, 0);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    //directly up
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(2, 0, 0, 0);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    //directly left
    model1 = new TriangleSolitaireModelImpl(5, 2, 0);
    try {
      model1.move(2, 2, 2, 0);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    //directly right
    model1 = new TriangleSolitaireModelImpl(5, 2, 2);
    try {
      model1.move(2, 0, 2, 2);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    //directly down
    model1 = new TriangleSolitaireModelImpl(5, 2, 0);
    try {
      model1.move(0, 0, 2, 0);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    //diagonally down right
    model1 = new TriangleSolitaireModelImpl(5, 2, 2);
    try {
      model1.move(0, 0, 2, 2);
    }
    catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(false));
  }

  //tests incorrect diagonal moves
  @Test
  public void testTriMovesWrong() {
    boolean thrown = false;

    //diagonally up right
    MarbleSolitaireModel model1 = new TriangleSolitaireModelImpl(5, 2, 2);
    try {
      model1.move(4, 0, 2, 2);
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //diagonally down left
    model1 = new TriangleSolitaireModelImpl(5, 4, 0);
    try {
      model1.move(2, 2, 4, 0);
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //tests other invalid moves
  @Test
  public void testTriMovesOtherInvalid() {
    boolean thrown = false;

    //not moving over a marble
    MarbleSolitaireModel model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(2, 2, 0, 0);
      model1.move(3, 3, 1, 1); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move to a slot with a marble in it
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(4, 4, 2, 2); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move to a row that doesn't exist
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(4, 4, 6, 4); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move to a column that doesn't exist
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(1, 1, 3, 4); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move to a slot with negative coordinates
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(1, 1, 1, -1); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move from a slot with a space in it
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(0, 0, 2, 2); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move from a row that doesn't exist
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(6, 4, 4, 4); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move from a column that doesn't exist
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(3, 4, 1, 1); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move from a slot with a space in it
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(0, 0, 2, 2); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move from a slot with negative coordinates
    model1 = new TriangleSolitaireModelImpl(5, 1, 0);
    try {
      model1.move(-1, 1, 1, 1); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
    //--------------------------------------------------------
    thrown = false;

    //trying to move 1 slot away (not over a slot)
    model1 = new TriangleSolitaireModelImpl(5, 0, 0);
    try {
      model1.move(1, 0, 0, 0); //incorrect
    } catch (IllegalArgumentException e) {
      thrown = true;
    }

    assertThat(thrown, is(true));
  }

  //tests getting the score.
  @Test
  public void testTriGetScore() {
    MarbleSolitaireModel model = new TriangleSolitaireModelImpl(5, 0, 0);
    assertThat(model.getScore(), is(14));
    model.move(2, 0, 0, 0);
    assertThat(model.getScore(), is(13));
    model.move(3, 2, 1, 0);
    assertThat(model.getScore(), is(12));
  }

  //tests getting the game state at the beginning.
  @Test
  public void testTriGetGameState() {
    MarbleSolitaireModel model = new TriangleSolitaireModelImpl(5, 0, 0);
    assertThat(model.getGameState(), is("    X\n"
        + "   O O\n"
        + "  O O O\n"
        + " O O O O\n"
        + "O O O O O"));
  }

  //tests getting the game state after some moves are made.
  @Test
  public void testTriGetGameState2() {
    MarbleSolitaireModel model = new TriangleSolitaireModelImpl(5, 0, 0);
    assertThat(model.getGameState(), is("    X\n"
        + "   O O\n"
        + "  O O O\n"
        + " O O O O\n"
        + "O O O O O"));
    model.move(2, 0, 0, 0);
    model.move(3, 2,1, 0);
    assertThat(model.getGameState(), is("    O\n"
        + "   O O\n"
        + "  X X O\n"
        + " O O X O\n"
        + "O O O O O"));
  }

  //tests if the game is over or not
  @Test
  public void testTriGameOver() {
    MarbleSolitaireModel model = new TriangleSolitaireModelImpl(5, 0, 0);
    assertThat(model.isGameOver(), is(false));

    model.move(2,0,0,0);
    assertThat(model.isGameOver(), is(false));
    model.move(2,2,2,0);
    assertThat(model.isGameOver(), is(false));
    model.move(0,0,2,2);
    assertThat(model.isGameOver(), is(false));
    model.move(3,0,1,0);
    assertThat(model.isGameOver(), is(false));
    model.move(4,2,2,0);
    assertThat(model.isGameOver(), is(false));
    model.move(1,0,3,0);
    assertThat(model.isGameOver(), is(false));
    model.move(3,3,3,1);
    assertThat(model.isGameOver(), is(false));
    model.move(3,0,3,2);
    assertThat(model.isGameOver(), is(false));
    model.move(4,4,4,2);
    assertThat(model.isGameOver(), is(false));
    model.move(4,1,4,3);
    assertThat(model.isGameOver(), is(false));
    model.move(2,2,4,2);
    assertThat(model.isGameOver(), is(false));
    model.move(4,3,4,1);
    assertThat(model.isGameOver(), is(false));
    model.move(4,0,4,2);
    assertThat(model.isGameOver(), is(true));

    assertThat(model.getGameState(), is("    X\n"
        + "   X X\n"
        + "  X X X\n"
        + " X X X X\n"
        + "X X O X X"));
  }


}
