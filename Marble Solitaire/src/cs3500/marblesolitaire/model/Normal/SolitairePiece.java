package cs3500.marblesolitaire.model.hw02;

import java.util.Objects;

/**
 * abstract class for solitaire pieces.
 * CHANGE: had to make the class and methods public so it could be accessed
 * outside of the package
 */
public abstract class SolitairePiece {

  private int row;
  private int col;

  SolitairePiece(int r, int c) {
    this.row = r;
    this.col = c;
  }

  /**
   * getter for the row variable.
   * @return int
   */
  public int getRow() {
    return this.row;
  }

  /**
   * getter for the col variable.
   * @return int
   */
  public int getCol() {
    return this.col;
  }

  /**
   * moves the gamepiece to the given coordinates and moves that piece to this pieces place.
   * @param toRow row to go to
   * @param toCol col to go to
   * @param replace gamepiece to replace with
   */
  public void move(int toRow, int toCol, SolitairePiece replace) {
    Objects.requireNonNull(replace);

    replace.moveHelp(this.row, this.col);

    this.row = toRow;
    this.col = toCol;
  }


  /**
   * just moves the game piece to the given coordinates.
   * @param toRow move to row x
   * @param toCol move to column y
   */
  private void moveHelp(int toRow, int toCol) {

    this.row = toRow;
    this.col = toCol;
  }

  /**
   * returns whether the solitaire piece is a marble.
   * @return boolean
   */
  public abstract boolean isMarble();

  /**
   * returns whether the solitaire piece is a space.
   * @return boolean
   */
  public abstract boolean isSpace();

}
