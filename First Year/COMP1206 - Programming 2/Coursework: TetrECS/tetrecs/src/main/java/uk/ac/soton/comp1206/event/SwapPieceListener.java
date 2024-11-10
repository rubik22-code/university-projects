package uk.ac.soton.comp1206.event;

import javafx.scene.input.KeyEvent;

/**
 * Interface for swapping pieces
 */

public interface SwapPieceListener {

  /**
   * Swap the piece
   * @param event the key event
   */

  void swapPiece(KeyEvent event);

}
