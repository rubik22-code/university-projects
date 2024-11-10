package uk.ac.soton.comp1206.component;

import java.util.Set;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.event.BlockClickedListener;
import uk.ac.soton.comp1206.event.RightClickedListener;
import uk.ac.soton.comp1206.game.Grid;

/**
 * A GameBoard is a visual component to represent the visual GameBoard.
 * It extends a GridPane to hold a grid of GameBlocks.
 *
 * The GameBoard can hold an internal grid of it's own, for example, for displaying an upcoming block. It also be
 * linked to an external grid, for the main game board.
 *
 * The GameBoard is only a visual representation and should not contain game logic or model logic in it, which should
 * take place in the Grid.
 */
public class GameBoard extends GridPane {

    private static final Logger logger = LogManager.getLogger(GameBoard.class);

    /**
     * Number of columns in the board
     */
    private final int cols;

    /**
     * Number of rows in the board
     */
    private final int rows;

    /**
     * The visual width of the board - has to be specified due to being a Canvas
     */
    private final double width;

    /**
     * The visual height of the board - has to be specified due to being a Canvas
     */
    private final double height;

    /**
     * The grid this GameBoard represents
     */
    final Grid grid;

    /**
     * The blocks inside the grid
     */
    GameBlock[][] blocks;

    /**
     * The listener to call when a specific block is clicked
     */
    private BlockClickedListener blockClickedListener;
    private RightClickedListener rightClickedListener;

    /**
     * The coordinates of the block that is currently being dragged
     */
    private int x = 2;
    private int y = 2;


    /**
     * Create a new GameBoard, based off a given grid, with a visual width and height.
     * @param grid linked grid
     * @param width the visual width
     * @param height the visual height
     */
    public GameBoard(Grid grid, double width, double height) {
        this.cols = grid.getCols();
        this.rows = grid.getRows();
        this.width = width;
        this.height = height;
        this.grid = grid;

        //Build the GameBoard
        build();
    }

    /**
     * Create a new GameBoard with it's own internal grid, specifying the number of columns and rows, along with the
     * visual width and height.
     *
     * @param cols number of columns for internal grid
     * @param rows number of rows for internal grid
     * @param width the visual width
     * @param height the visual height
     */
    public GameBoard(int cols, int rows, double width, double height) {
        this.cols = cols;
        this.rows = rows;
        this.width = width;
        this.height = height;
        this.grid = new Grid(cols,rows);

        //Build the GameBoard
        build();
    }

    /**
     * Get a specific block from the GameBoard, specified by it's row and column
     * @param x column
     * @param y row
     * @return game block at the given column and row
     */
    public GameBlock getBlock(int x, int y) {
        return blocks[x][y];
    }

    /**
     * Build the GameBoard by creating a block at every x and y column and row
     */
    protected void build() {
        logger.info("Building grid: {} x {}",cols,rows);

        setMaxWidth(width);
        setMaxHeight(height);

        setGridLinesVisible(true);

        blocks = new GameBlock[cols][rows];

        for(var y = 0; y < rows; y++) {
            for (var x = 0; x < cols; x++) {
                createBlock(x,y);
            }
        }
    }

    /**
     * Create a block at the given x and y position in the GameBoard
     * @param x column
     * @param y row
     */
    protected GameBlock createBlock(int x, int y) {
        var blockWidth = width / cols;
        var blockHeight = height / rows;

        //Create a new GameBlock UI component
        GameBlock block = new GameBlock(this, x, y, blockWidth, blockHeight);

        //Add to the GridPane
        add(block,x,y);

        //Add to our block directory
        blocks[x][y] = block;

        //Link the GameBlock component to the corresponding value in the Grid
        block.bind(grid.getGridProperty(x,y));

        //Add a mouse click handler to the block to trigger GameBoard blockClicked method
        block.setOnMouseClicked((event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                blockClicked(event, block);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                rightClicked(event);
            }
        });

        // Highlight the block when the mouse enters it
        block.setOnMouseEntered((event) -> {
            block.createHighlight();
        });

        // Remove the highlight when the mouse leaves the block
        block.setOnMouseExited((event) -> {
            block.cleanHighlight();
        });

        return block;
    }

    /**
     * Set the listener to handle an event when a block is clicked
     * @param listener listener to add
     */
    public void setOnBlockClick(BlockClickedListener listener) {
        this.blockClickedListener = listener;
    }

    /**
     * Set the listener to handle an event when a block is right clicked
     * @param listener listener to add
     */

    public void setOnRightClicked(RightClickedListener listener) {
        logger.info("Right clicked listener added");
        this.rightClickedListener = listener;
    }


    /**
     * Triggered when a block is clicked. Call the attached listener.
     * @param event mouse event
     * @param block block clicked on
     */
    private void blockClicked(MouseEvent event, GameBlock block) {
        logger.info("Block clicked: {}", block);

        if(blockClickedListener != null) {
            blockClickedListener.blockClicked(block);
        }
    }

    /**
     * Triggered when a block is right clicked. Call the attached listener.
     * @param event mouse event
     */

    private void rightClicked(MouseEvent event) {
        logger.info("Right clicked on mouse");

        if(event.getButton() == MouseButton.SECONDARY && rightClickedListener != null) {
            rightClickedListener.rightClicked(event);
        }
    }

    /**
     * Move the piece up
     */
    public void movePieceUp() {
        Multimedia.playAudio("transition.wav");
        logger.info("Using the move piece method");
        this.togglePointer(false);
        this.rePaint();
        y--;
        this.togglePointer(true);
    }

    /**
     * Move the piece down
     */

    public void movePieceDown() {
        Multimedia.playAudio("transition.wav");
        logger.info("Using the move piece method");
        this.togglePointer(false);
        this.rePaint();
        y++;
        this.togglePointer(true);

    }

    /**
     * Move the piece to the left
     */

    public void movePieceLeft() {
        Multimedia.playAudio("transition.wav");
        logger.info("Using the move piece method");
        this.togglePointer(false);
        this.rePaint();
        x--;
        this.togglePointer(true);
    }

    /**
     * Move the piece to the right
     */

    public void movePieceRight() {
        Multimedia.playAudio("transition.wav");
        logger.info("Using the move piece method");
        this.togglePointer(false);
        this.rePaint();
        x++;
        this.togglePointer(true);
    }

    /**
     * Toggle the pointer on or off between keyboard presses and mouse clicks
     * @param toggle true to turn on, false to turn off
     */
    public void togglePointer(boolean toggle) {
        logger.info("Toggling pointer at {},{}", x, y);
        if(this.blocks[x][y].getValue() == 0) {
            this.blocks[x][y].setToggle(toggle);
        }
        else {
            this.blocks[x][y].setToggle(false);
        }
    }

    /**
     * Repaint the GameBoard when moving around the pointer
     */
    public void rePaint() {
        this.blocks[x][y].paint();
    }

    /**
     * Takes a Set of GameBlockCoordinates and triggers the fadeOut method for each block.
     * @param blockCoordinates Set of GameBlockCoordinates to fade out
     */
    public void fadeOut(Set<GameBlockCoordinate> blockCoordinates) {
        logger.info("Fading out blocks: {}", blockCoordinates);
        for (GameBlockCoordinate blockCoordinate : blockCoordinates) {
            GameBlock block = getBlock(blockCoordinate.getX(), blockCoordinate.getY());
            block.fadeOut();
        }
    }

    /**
     * Gets the x coordinate of the pointer
     * @return x coordinate of the pointer
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the pointer
     * @return y coordinate of the pointer
     */
    public int getY() {
        return y;
    }
}
