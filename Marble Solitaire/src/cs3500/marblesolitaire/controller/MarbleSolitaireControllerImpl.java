package cs3500.marblesolitaire.controller;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import java.io.IOException;
import java.util.Scanner;

/**
 * An implementation for the Marle Solitaire game controller.
 */
public class MarbleSolitaireControllerImpl implements MarbleSolitaireController {

  //the model
  private MarbleSolitaireModel model;
  //the scanner
  private Scanner scanner;
  //the appendable
  private Appendable appendable;
  //whether or not the game should be quit
  private boolean quit;
  //the input as a String
  private String input;
  //the current index of the coordinates array
  private int index;
  //array of the valid positive integers the use has entered so far. Used to make a move.
  private int[] coordinates;


  /**
   * Constructor for a marble solitaire controller.
   *
   * @param rd A Readable for the controller to read from
   * @param ap An Appendable for the controller to append to
   */
  public MarbleSolitaireControllerImpl(Readable rd, Appendable ap) {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Readable or Appendable are null");
    }

    this.appendable = ap;
    scanner = new Scanner(rd);
  }

  @Override
  public void playGame(MarbleSolitaireModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    this.model = model;
    quit = false;
    input = "";
    index = 0;
    coordinates = new int[4];

    this.append(model.getGameState() + "\n");
    this.append("Score:" + model.getScore() + "\n");
    this.makeMove();
  }

  /**
   * handles the loop functionality for interacting with the user's move choices.
   */
  private void makeMove() {

    MOVE:
    //while there is input
    while (this.next()) {

      input = scanner.next();
      //if q or Q, quit
      if (input.equals("q") || input.equals("Q")) {
        this.quit();
        break MOVE;
      }
      try {
        //try parsing the input as an int and adding it to coordinates array. increase index.
        int inputInt = Integer.parseInt(input);
        coordinates[index] = inputInt;
        index++;
        //if you've filled the array with 4 valid ints for a coordinate. Make the move and
        //reset the array and index.
        if (index == 4) {
          try {
            this.model.move(coordinates[0] - 1, coordinates[1] - 1,
                coordinates[2] - 1, coordinates[3] - 1);
            this.append(model.getGameState() + "\n");
            this.append("Score:" + model.getScore() + "\n");
            coordinates = new int[4];
            index = 0;
            if (this.model.isGameOver()) {
              this.append("Game over!\n" + this.model.getGameState()
                  + "\nScore: " + this.model.getScore() + "\n");
              break MOVE;
            }
          }
          //if the move didn't work, reset the array and index and play the move again.
          catch (IllegalArgumentException e) {
            if (quit) {
              break MOVE;
            } else {
              this.append("Invalid move. Play again." + e.getMessage() + "\n");
              coordinates = new int[4];
              index = 0;
            }
          }
        }
      }
      //if the user didn't enter a valid int or q, then determine which coordinate is wrong
      //and the next valid input will be taken as that coordinate.
      catch (NumberFormatException e) {
        if (index == 0) {
          this.append("fromRow is not a valid int. Input a valid fromRow.\n");
        } else if (index == 1) {
          this.append("fromCol is not a valid int. Input a valid fromCol.\n");
        } else if (index == 2) {
          this.append("toRow is not a valid int. Input a valid toRow.\n");
        } else if (index == 3) {
          this.append("toCol is not a valid int. Input a valid toCol.\n");
        }
      }
    }

  }

  /**
   * handles appending strings to the Appendable.
   *
   * @param message the String to be appended to the Appendable
   */
  private void append(String message) {
    try {
      appendable.append(message);
    } catch (IOException e1) {
      throw new IllegalStateException("Unable to transmit output");
    }
  }

  /**
   * handles scanning for the nextline and keeping the state of the readable intact.
   *
   * @return the next line read by the Scanner as a String
   * @throws IllegalStateException when the readable can't read anything
   */
  private boolean next() {
    if (this.scanner.hasNext()) {
      return true;
    }
    throw new IllegalStateException("No more lines to read from");
  }

  /**
   * Quits the game if "q" or "Q" are input.
   */
  private void quit() {
    this.append("Game quit!\nState of game when quit:\n" + this.model.getGameState()
        + "\nScore: " + this.model.getScore() + "\n");
    this.quit = true;
  }

}
