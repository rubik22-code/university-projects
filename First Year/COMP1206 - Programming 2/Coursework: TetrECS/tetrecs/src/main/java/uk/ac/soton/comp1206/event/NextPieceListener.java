package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.game.GamePiece;

/**
 * Listener for the next piece event
 */

public interface NextPieceListener {

  /**
   * Called when the next piece is changed
   * @param nextPiece The next piece
   * @param followingPiece The piece after that
   */

  void nextPiece(GamePiece nextPiece, GamePiece followingPiece);

}
