package uk.ac.soton.comp1206.event;

/**
 * Interface for listening to block hover events
 */

public interface GameLoopListener {

  /**
   * Called when the game loop is updated
   * @param duration The duration of the last loop
   */

  void gameLoop(int duration);

}
