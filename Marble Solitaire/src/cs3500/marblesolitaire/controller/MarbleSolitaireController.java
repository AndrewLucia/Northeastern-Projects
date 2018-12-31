package cs3500.marblesolitaire.controller;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;

/**
 * represents the controller for the marble solitaire game.
 */
public interface MarbleSolitaireController {

  /**
   * starts the game with the given model.
   * @param model represents the model for the game
   */
  void playGame(MarbleSolitaireModel model);
}
