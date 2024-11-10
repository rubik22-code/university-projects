package uk.ac.soton.comp1206.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.game.Grid;

public class PieceBoard extends GameBoard {

  private static final Logger logger = LogManager.getLogger(PieceBoard.class);

  /**
   * Create a new GameBoard, based off a given grid, with a visual width and height.
   *
   * @param grid   linked grid
   * @param width  the visual width
   * @param height the visual height
   */

  public PieceBoard(Grid grid, double width, double height) {
    super(grid, width, height);
  }

  /**
   * Create a new GameBoard with it's own internal grid, specifying the number of columns and rows,
   * along with the visual width and height.
   *
   * @param width  the visual width
   * @param height the visual height
   */

  public PieceBoard(int cols, int rows, double width, double height) {
    super(cols, rows, width, height);
  }

  /**
   * Display a given piece on the board for the 3x3 grid
   *
   * @param piece the piece to display
   */
  public void displayPiece(GamePiece piece) {
    logger.info("Playing next upcoming piece {}", piece);

    for (var blockX = 0; blockX < grid.getCols(); blockX++) {
      for (var blockY = 0; blockY < grid.getRows(); blockY++) {
        grid.set(blockX, blockY, 0);
      }
    }

    grid.playPiece(piece, 1, 1);
  }
}
