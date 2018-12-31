package cs3500.marblesolitaire.model.hw02;

/**
 * represents a Space in the Marble Solitaire game.
 * CHANGE: had to make the class and methods public so it could be accessed
 * outside of the package
 */
public class Space extends SolitairePiece {

  public Space(int r, int c) {

    super(r, c);
  }

  @Override
  public boolean isMarble() {

    return false;
  }

  @Override
  public boolean isSpace() {
    return true;
  }

}
