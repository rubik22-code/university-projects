package uk.ac.soton.comp1206.event;

/**
 * Interface for listening to block hover events
 */

public interface BlockHoveredListener {

  /**
   * Called when a block is hovered over
   * @param x the x coordinate of the block
   * @param y the y coordinate of the block
   */
  void blockHovered(int x, int y);

}
