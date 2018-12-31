package cs3500.marblesolitaire.model.hw04;

import cs3500.marblesolitaire.model.hw02.Marble;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.SolitairePiece;
import cs3500.marblesolitaire.model.hw02.Space;
import java.util.ArrayList;
import java.util.List;

public class TriangleSolitaireModelImpl implements MarbleSolitaireModel {

  private List<SolitairePiece[]> grid = new ArrayList<SolitairePiece[]>();
  private List<SolitairePiece> pieces = new ArrayList<SolitairePiece>();

  /**
   * Constructor that creates a grid of size 5 and starting space at (0, 0).
   */
  public TriangleSolitaireModelImpl() {
    this.initGrid(5, 0, 0);
  }

  /**
   * Constructor that creates a grid of size (dimensions) and starting space at (0, 0).
   * @param dimensions the size of the grid
   */
  public TriangleSolitaireModelImpl(int dimensions) {
    if (dimensions <= 0) {
      throw new IllegalArgumentException("Dimension is non-positive.");
    }
    this.initGrid(dimensions, 0, 0);
  }

  /**
   * Constructor that creates a grid of size 5 and starting space at (row, col).
   * @param row starting space row
   * @param col starting space col
   */
  public TriangleSolitaireModelImpl(int row, int col) {
    if (!((row < 5 && col < 5)
        && (col <= row) && (row >= 0 && col >= 0))) {
      throw new IllegalArgumentException("row, col pair is invalid.");
    }
    this.initGrid(5, row, col);
  }

  /**
   * Constructor that creates a grid of size (dimensions) and starting space at (row, col).
   * @param dimensions the size of the grid
   * @param row starting space row
   * @param col starting space col
   */
  public TriangleSolitaireModelImpl(int dimensions, int row, int col) {
    if (dimensions <= 0) {
      throw new IllegalArgumentException("Dimension is non-positive.");
    }
    if (!((row < dimensions && col < dimensions)
        && (col <= row) && (row >= 0 && col >= 0))) {
      throw new IllegalArgumentException("row, col pair is invalid.");
    }
    this.initGrid(dimensions, row, col);
  }

  /**
   * creates the grid of Marbles with the given size and the given(startRow, startCol) being Space.
   * @param size size of the triangle board
   * @param startRow space row
   * @param startCol space col
   */
  private void initGrid(int size, int startRow, int startCol) {

    for (int i = 0; i < size; i++) {
      grid.add(new SolitairePiece[i + 1]);
      for (int j = 0; j <= i; j++) {
        if (i == startRow && j == startCol) {
          SolitairePiece s = new Space(i, j);
          grid.get(i)[j] = s;
          pieces.add(s);
        }
        else {
          SolitairePiece m = new Marble(i, j);
          grid.get(i)[j] = m;
          pieces.add(m);
        }
      }
    }
  }

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
    if (!valid(fromRow, fromCol)) {
      throw new IllegalArgumentException("from coordinates not valid");
    }
    if (!valid(toRow, toCol)) {
      throw new IllegalArgumentException("to coordinates not valid");
    }
    if (this.grid.get(fromRow)[fromCol].isSpace()) {
      throw new IllegalArgumentException("from coordinate not a marble");
    }
    if (this.grid.get(toRow)[toCol].isMarble()) {
      throw new IllegalArgumentException("to coordinate not a space");
    }
    if (Math.abs(toRow - fromRow) == 2 && fromCol == toCol) {
      if (this.grid.get((fromRow + (toRow - fromRow) / 2))[toCol].isMarble()) {
        this.grid.get(fromRow)[fromCol].move(toRow, toCol, this.grid.get(toRow)[toCol]);
        this.pieces.remove(this.grid.get((fromRow + (toRow - fromRow) / 2))[toCol]);
        this.pieces.add(new Space(fromRow + (toRow - fromRow) / 2, toCol));
        this.updateGrid();
      }
      else {
        throw new IllegalArgumentException(
            "Either not moving over Marble or not moving 2 places away");
      }
    }
    else if (Math.abs(toCol - fromCol) == 2 && fromRow == toRow) {
      if (this.grid.get(toRow)[fromCol + (toCol - fromCol) / 2].isMarble()) {
        this.grid.get(fromRow)[fromCol].move(toRow, toCol, this.grid.get(toRow)[toCol]);
        this.pieces.remove(this.grid.get(toRow)[fromCol + (toCol - fromCol) / 2]);
        this.pieces.add(new Space(toRow, fromCol + (toCol - fromCol) / 2));
        this.updateGrid();
      }
      else {
        throw new IllegalArgumentException(
            "Either not moving over Marble or not moving 2 places away");
      }
    }
    else if (toRow - fromRow == 2 && toCol - fromCol == 2) {
      if (this.grid.get(fromRow + 1)[fromCol + 1].isMarble()) {
        this.grid.get(fromRow)[fromCol].move(toRow, toCol, this.grid.get(toRow)[toCol]);
        this.pieces.remove(this.grid.get(fromRow + 1)[fromCol + 1]);
        this.pieces.add(new Space(fromRow + 1,fromCol + 1));
        this.updateGrid();
      }
      else {
        throw new IllegalArgumentException(
            "Either not moving over Marble or not moving in correct diagonal");
      }
    }
    else if (fromRow - toRow == 2 && fromCol - toCol == 2) {
      if (this.grid.get(fromRow - 1)[fromCol - 1].isMarble()) {
        this.grid.get(fromRow)[fromCol].move(toRow, toCol, this.grid.get(toRow)[toCol]);
        this.pieces.remove(this.grid.get(fromRow - 1)[fromCol - 1]);
        this.pieces.add(new Space(fromRow - 1,fromCol - 1));
        this.updateGrid();
      }
      else {
        throw new IllegalArgumentException(
            "Either not moving over Marble or not moving in correct diagonal");
      }
    }
    else {
      throw new IllegalArgumentException(
          "Either not moving over Marble, not moving 2 places away, "
              + "or not moving in correct diagonal");
    }
  }

  /**
   * returns whether or not the specified coordinates can create a move.
   * @param fromRow the row number of the position to be moved from (starts at 0)
   * @param fromCol the column number of the position to be moved from(starts at 0)
   * @param toRow the row number of the position to be moved to(starts at 0)
   * @param toCol the column number of the position to be moved to (starts at 0)
   * @return whether or not the move can be made.
   */
  private boolean canMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (!valid(fromRow, fromCol)) {
      return false;
    }
    if (!valid(toRow, toCol)) {
      return false;
    }
    if (this.grid.get(fromRow)[fromCol].isSpace()) {
      return false;
    }
    if (this.grid.get(toRow)[toCol].isMarble()) {
      return false;
    }
    if (Math.abs(toRow - fromRow) == 2 && fromCol == toCol) {
      if (this.grid.get((fromRow + (toRow - fromRow) / 2))[toCol].isMarble()) {
        return true;
      }
    }
    else if (Math.abs(toCol - fromCol) == 2 && fromRow == toRow) {
      if (this.grid.get(toRow)[fromCol + (toCol - fromCol) / 2].isMarble()) {
        return true;
      }
    }
    else if (toRow - fromRow == 2 && toCol - fromCol == 2) {
      if (this.grid.get(fromRow + 1)[fromCol + 1].isMarble()) {
        return true;
      }
    }
    else if (fromRow - toRow == 2 && fromCol - toCol == 2) {
      if (this.grid.get(fromRow - 1)[fromCol - 1].isMarble()) {
        return true;
      }
    }
    return false;
  }

  /**
   * updates the grid with the correct space and marble placements.
   */
  private void updateGrid() {
    for (SolitairePiece p: this.pieces) {
      this.grid.get(p.getRow())[p.getCol()] = p;
    }

  }

  @Override
  public boolean isGameOver() {

    for (SolitairePiece p: this.pieces) {
      if (p.isMarble()) {
        if (this.canMove(p.getRow(), p.getCol(), p.getRow() - 2, p.getCol())) {
          return false;
        }
        if (this.canMove(p.getRow(), p.getCol(), p.getRow(), p.getCol() + 2)) {
          return false;
        }
        if (this.canMove(p.getRow(), p.getCol(), p.getRow(), p.getCol() - 2)) {
          return false;
        }
        if (this.canMove(p.getRow(), p.getCol(), p.getRow() + 2, p.getCol())) {
          return false;
        }
        if (this.canMove(p.getRow(), p.getCol(), p.getRow() - 2, p.getCol() - 2)) {
          return false;
        }
        if (this.canMove(p.getRow(), p.getCol(), p.getRow() + 2, p.getCol() + 2)) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public String getGameState() {
    String state = "";

    for (int i = 0; i < this.grid.size(); i++) {
      for (int j = this.grid.size() - 1; j > i; j--) {
        state += " ";
      }
      for (int k = 0; k < this.grid.get(i).length; k++) {
        if (this.grid.get(i)[k].isMarble()) {
          state += "O";
        }
        if (this.grid.get(i)[k].isSpace()) {
          state += "X";
        }
        if (k + 1 < this.grid.get(i).length) {
          state += " ";
        }
      }
      if (i + 1 < this.grid.size()) {
        state += "\n";
      }
    }

    return state;
  }

  @Override
  public int getScore() {

    int countScore = 0;

    for (SolitairePiece p : pieces) {
      if (p.isMarble()) {
        countScore++;
      }
    }

    return countScore;
  }

  /**
   * determines whether the given coordinates are valid or not.
   * @param row given row
   * @param col given col
   * @return valid or not valid coordinate
   */
  private boolean valid(int row, int col) {

    return (row < this.grid.size() && col < this.grid.size())
        && (col <= row) && (row >= 0 && col >= 0);
  }
}
