package uk.ac.soton.comp1206.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Grid is a model which holds the state of a game board. It is made up of a set of Integer values arranged in a 2D
 * arrow, with rows and columns.
 *
 * Each value inside the Grid is an IntegerProperty can be bound to enable modification and display of the contents of
 * the grid.
 *
 * The Grid contains functions related to modifying the model, for example, placing a piece inside the grid.
 *
 * The Grid should be linked to a GameBoard for it's display.
 */
public class Grid {

    private static final Logger logger = LogManager.getLogger(Grid.class);

    /**
     * The number of columns in this grid
     */
    private final int cols;

    /**
     * The number of rows in this grid
     */
    private final int rows;

    /**
     * The grid is a 2D arrow with rows and columns of SimpleIntegerProperties.
     */
    private final SimpleIntegerProperty[][] grid;

    /**
     * Create a new Grid with the specified number of columns and rows and initialise them
     * @param cols number of columns
     * @param rows number of rows
     */
    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create the grid itself
        grid = new SimpleIntegerProperty[cols][rows];

        //Add a SimpleIntegerProperty to every block in the grid
        for(var y = 0; y < rows; y++) {
            for(var x = 0; x < cols; x++) {
                grid[x][y] = new SimpleIntegerProperty(0);
            }
        }
    }

    /**
     * Get the Integer property contained inside the grid at a given row and column index. Can be used for binding.
     * @param x column
     * @param y row
     * @return the IntegerProperty at the given x and y in this grid
     */
    public IntegerProperty getGridProperty(int x, int y) {
        return grid[x][y];
    }

    /**
     * Update the value at the given x and y index within the grid
     * @param x column
     * @param y row
     * @param value the new value
     */
    public void set(int x, int y, int value) {
        grid[x][y].set(value);
    }

    /**
     * Validates whether the piece can be placed in the x,y position
     * @param piece the piece to be played
     * @param placeX the x coordinate
     * @param placeY the y coordinate
     * @return if it can be played or not
     */

    public boolean canPlayPiece(GamePiece piece, int placeX, int placeY) {
        logger.info("Checking if we can play piece {} at {}, {}", piece, placeX, placeY);

        // Offsetting by using the center rather than the top left corner of the piece
        int topX = placeX - 1;
        int topY = placeY - 1;

        int[][] blocks = piece.getBlocks();

        for (var blockX = 0; blockX < blocks.length; blockX++) {
            for (var blockY = 0; blockY < blocks.length; blockY++) {
                // blockX and blockY coordinate for the 3x3 array
                var blockValue = blocks[blockX][blockY];
                if (blockValue > 0) {
                    // Check if we can place the block onto the grid
                    var gridValue = get(topX + blockX, topY + blockY);
                    if (gridValue != 0) {
                        logger.info("Can't play piece because of block at {}, {}", placeX + blockX, placeY + blockY);
                        return false;
                    }
                }
            }
        }
        // No blocks are interfering with the grid.
        return true;
    }

    /**
     * The piece is played by updating the piece on the grid
     * The topx and topy are used to offset the piece to the correct position
     * @param piece the piece to be played
     * @param placeX the x coordinate
     * @param placeY the y coordinate
     */

    public void playPiece(GamePiece piece, int placeX, int placeY) {
        logger.info("Playing piece {} at {}, {}", piece,placeX,placeY);
        int value = piece.getValue();
        int[][] blocks = piece.getBlocks();

        // Same as canPlayPiece

        int topX = placeX - 1;
        int topY = placeY - 1;

        // Return if the piece can't be played
        if(!canPlayPiece(piece,placeX,placeY)) return;

        // If it can be played, update the grid

        for (var blockX = 0; blockX < blocks.length; blockX++) {
            for (var blockY = 0; blockY < blocks.length; blockY++) {
                // blockX and blockY coordinate for the 3x3 array
                var blockValue = blocks[blockX][blockY];
                if (blockValue > 0) {
                    set(topX + blockX, topY + blockY, value);
                }
            }
        }
    }

    /**
     * Get the value represented at the given x and y index within the grid
     * @param x column
     * @param y row
     * @return the value
     */
    public int get(int x, int y) {
        try {
            //Get the value held in the property at the x and y index provided
            return grid[x][y].get();
        } catch (ArrayIndexOutOfBoundsException e) {
            //No such index
            return -1;
        }
    }

    /**
     * Get the number of columns in this game
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }
}
