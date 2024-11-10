package uk.ac.soton.comp1206.event;

import javafx.scene.input.MouseEvent;

/**
 * Interface for a listener that listens for right clicks
 */

public interface RightClickedListener {

  /**
   * Called when the mouse is right clicked
   * @param event the mouse event
   */

  void rightClicked(MouseEvent event);

}
