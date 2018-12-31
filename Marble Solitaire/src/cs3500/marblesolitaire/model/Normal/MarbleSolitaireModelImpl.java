package cs3500.marblesolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * represents the implementation of the Marble Solitaire game.
 */
public class MarbleSolitaireModelImpl implements MarbleSolitaireModel {

  private SolitairePiece[][] grid = new SolitairePiece[7][7];
  private List<SolitairePiece> pieces = new ArrayList<SolitairePiece>();

  public MarbleSolitaireModelImpl() {

    this.initGrid(3, 3);
  }

  /**
   * Constructor taking in row and col for starting space.
   * @param row row coordinate for starting space
   * @param col column coordinate for starting space
   */
  public MarbleSolitaireModelImpl(int row, int col) {
    if (!this.valid(row, col)) {
      throw new IllegalArgumentException("row, col coordinate out of bounds");
    }
    else {
      this.initGrid(row, col);
    }
  }

  /**
   * creates the grid with the given (row, col) being a Space and the rest Marbles.
   * @param row space row
   * @param col space col
   */
  private void initGrid(int row, int col) {

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (i == 0 || i == 1 || i == 5 || i == 6) {
          if (j == 0 || j == 1 || j == 5 || j == 6) {
            this.grid[i][j] = null;
          }
          else {
            SolitairePiece m = new Marble(i, j);
            this.grid[i][j] = m;
            this.pieces.add(m);
          }
        }
        else {
          SolitairePiece m = new Marble(i, j);
          this.grid[i][j] = m;
          this.pieces.add(m);
        }
      }
    }

    SolitairePiece s = new Space(row, col);
    this.grid[row][col] = s;
    this.pieces.add(s);
  }

  /**
   * updates the grid with the correct space and marble placements.
   */
  private void updateGrid() {
    for (SolitairePiece p: this.pieces) {
      this.grid[p.getRow()][p.getCol()] = p;
    }

  }

  @Override
  public String getGameState() {
    String state = "";
    int countRow = 0;
    int countCol = 0;

    for (SolitairePiece[] array: this.grid) {
      for (SolitairePiece p: array) {
        if (p == null && countCol < 5) {
          state += "  ";
        }
        else if (p.isMarble()) {
          state += "O";
          if (countCol + 1 < 7) {
            if (this.grid[countRow][countCol + 1] == null) {
              break;
            }
          }
          if (countCol != 6) {
            state += " ";
          }
        }
        else if (p.isSpace()) {
          state += "X";
          if (countCol + 1 < 7) {
            if (this.grid[countRow][countCol + 1] == null) {
              break;
            }
          }
          if (countCol != 6) {
            state += " ";
          }
        }
        countCol += 1;
      }
      if (countRow != 6) {
        state += "\n";
      }
      countRow += 1;
      countCol = 0;
    }
    return state;
  }

  @Override
  public boolean isGameOver() {
    boolean over = false;
    boolean mid = false;
    int countSpace = 0;

    for (int i = 0; i < this.grid.length; i++) {
      for (int j = 0; j < this.grid[i].length; j++) {
        if (i == 3 && j == 3 && this.grid[i][j].isMarble()) {
          mid = true;
        }
        if (this.grid[i][j] != null && this.grid[i][j].isSpace()) {
          countSpace += 1;
        }
      }
    }

    if (countSpace == 32 && mid) {
      over = true;
    }

    return over;
  }

  @Override
  public int getScore() {

    int countScore = 0;

    for (int i = 0; i < this.grid.length; i++) {
      for (int j = 0; j < this.grid[i].length; j++) {
        if (this.grid[i][j] != null) {
          if (this.grid[i][j].isMarble()) {
            countScore += 1;
          }
        }
      }
    }
    return  countScore;
  }

  @Override
  public void move(int fromRow,int fromCol,int toRow,int toCol) {
    if (!valid(fromRow, fromCol)) {
      throw new IllegalArgumentException("from coordinates not valid");
    }
    if (!valid(toRow, toCol)) {
      throw new IllegalArgumentException("to coordinates not valid");
    }
    if (this.grid[fromRow][fromCol].isSpace()) {
      throw new IllegalArgumentException("from coordinate not a marble");
    }
    if (this.grid[toRow][toCol].isMarble()) {
      throw new IllegalArgumentException("to coordinate not a space");
    }
    if (Math.abs(toRow - fromRow) == 2 && fromCol == toCol) {
      if (this.grid[(fromRow + (toRow - fromRow) / 2)][toCol].isMarble()) {
        this.grid[fromRow][fromCol].move(toRow, toCol, this.grid[toRow][toCol]);
        this.pieces.remove(this.grid[(fromRow + (toRow - fromRow) / 2)][toCol]);
        this.pieces.add(new Space(fromRow + (toRow - fromRow) / 2, toCol));
        this.updateGrid();
      }
      else {
        throw new IllegalArgumentException(
            "Either not moving over Marble or not moving 2 places away");
      }
    }
    else if (Math.abs(toCol - fromCol) == 2 && fromRow == toRow) {
      if (this.grid[toRow][fromCol + (toCol - fromCol) / 2].isMarble()) {
        this.grid[fromRow][fromCol].move(toRow, toCol, this.grid[toRow][toCol]);
        this.pieces.remove(this.grid[toRow][fromCol + (toCol - fromCol) / 2]);
        this.pieces.add(new Space(toRow, fromCol + (toCol - fromCol) / 2));
        this.updateGrid();
      }
      else {
        throw new IllegalArgumentException(
            "Either not moving over Marble or not moving 2 places away");
      }
    }
    else {
      throw new IllegalArgumentException(
          "Either not moving over Marble or not moving 2 places away");
    }

  }

  /**
   * determines whether the given coordinates are valid or not.
   * @param row given row
   * @param col given col
   * @return valid or not valid coordinate
   */
  private boolean valid(int row, int col) {

    return ((row > 1 && row < 5) && (col > -1 && col < 7))
        || ((col > 1 && col < 5) && (row > -1 && row < 7));
  }

}
