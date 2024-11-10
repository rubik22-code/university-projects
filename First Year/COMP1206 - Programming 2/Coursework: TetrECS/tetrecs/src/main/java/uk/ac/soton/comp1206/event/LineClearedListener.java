package uk.ac.soton.comp1206.event;

import java.util.Set;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;

/**
 * The listener interface for receiving lineCleared events.
 */

public interface LineClearedListener {

  /**
   * Invoked when a line is cleared
   * @param blockCoordinates the coordinates of the blocks that were cleared
   */

  void clearLine(Set<GameBlockCoordinate> blockCoordinates);

}
