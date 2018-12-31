package cs3500.marblesolitaire.model.hw02;

/**
 * represents a Marble in the Marble Solitaire game.
 * CHANGE: had to make the class and methods public so it could be accessed
 * outside of the package
 */
public class Marble extends SolitairePiece {

  public Marble(int r, int c) {

    super(r, c);
  }

  @Override
  public boolean isMarble() {
    return true;
  }

  @Override
  public boolean isSpace() {
    return false;
  }



}
