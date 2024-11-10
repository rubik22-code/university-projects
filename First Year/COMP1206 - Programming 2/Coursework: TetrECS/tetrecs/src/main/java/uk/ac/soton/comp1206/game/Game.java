package uk.ac.soton.comp1206.game;

import java.util.HashSet;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;
import uk.ac.soton.comp1206.event.GameLoopListener;
import uk.ac.soton.comp1206.event.GameOverListener;
import uk.ac.soton.comp1206.event.LineClearedListener;
import uk.ac.soton.comp1206.event.NextPieceListener;

/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);

    private Random random = new Random();

    protected GamePiece followingPiece;

    protected GamePiece currentPiece;

    protected Timer timer;

    protected GameLoopListener gameLoopListener;
    protected GameOverListener gameOverListener;

    /**
     * Bindable properties for the score, level, lives and multiplier.
     */

    protected IntegerProperty score = new SimpleIntegerProperty(0);
    protected IntegerProperty level = new SimpleIntegerProperty(0);
    protected IntegerProperty lives = new SimpleIntegerProperty(3);
    protected IntegerProperty multiplier = new SimpleIntegerProperty(1);


    // Accessor methods for score
    public int getScore() {
        return score.get();
    }

    // Mutator methods for score

    public void setScore(int newScore) {
        score.set(newScore);
    }

    // Listen for changes and update UI elements automatically

    public IntegerProperty scoreProperty() {
        return score;
    }

    // Accessor methods for level
    public int getLevel() {
        return level.get();
    }

    // Mutator methods for level

    public void setLevel(int newLevel) {
        level.set(newLevel);
    }

    // Listen for changes and update UI elements automatically

    public IntegerProperty levelProperty() {
        return level;
    }

    // Accessor methods for lives
    public int getLives() {
        return lives.get();
    }

    // Mutator methods for lives

    public void setLives(int newLives) {
        lives.set(newLives);
    }

    // Listen for changes and update UI elements automatically

    public IntegerProperty livesProperty() {
        return lives;
    }

    // Accessor methods for multiplier
    public int getMultiplier() {
        return multiplier.get();
    }

    // Mutator methods for multiplier

    public void setMultiplier(int newMultiplier) {
        multiplier.set(newMultiplier);
    }

    // Listen for changes and update UI elements automatically

    public IntegerProperty multiplierProperty() {
        return multiplier;
    }

    NextPieceListener nextPieceListener;

    LineClearedListener lineClearedListener;

    /**
     * Number of rows
     */
    protected final int rows;

    /**
     * Number of columns
     */
    protected final int cols;

    /**
     * The grid model linked to the game
     */
    protected final Grid grid;

    protected Integer timeLeft;

    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     * @param cols number of columns
     * @param rows number of rows
     */
    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create a new grid model to represent the game state
        this.grid = new Grid(cols,rows);
    }

    /**
     * Start the game
     */
    public void start() {
        logger.info("Starting game");
        startTimer();
        initialiseGame();
    }

    /**
     * Get the next piece to be played from spawnPiece() and set it as the current piece.
     * @return next piece
     */

    public GamePiece nextPiece() {

        if (followingPiece == null) {
            followingPiece = spawnPiece();
        }
        currentPiece = followingPiece;
        followingPiece = spawnPiece();
        nextPieceListener.nextPiece(currentPiece, followingPiece);
        logger.info("Next piece is {}", currentPiece);
        return currentPiece;
    }


    /**
     * Spawn a random piece by picking a random number between 0 and the number of pieces.
     * @return random piece
     */

    public GamePiece spawnPiece() {;
        var maxPieces = GamePiece.PIECES;
        var randomPiece = random.nextInt(maxPieces);
        logger.info("Picking random piece {}", randomPiece);
        var piece = GamePiece.createPiece(randomPiece);
        return piece;
    }

    /**
     * To handle the clearance of lines after a piece has been played.
     * Keeping track of the blocks and the counters themselves.
     * Will check horizontally and vertically, and then clear the lines.
     */

    public void afterPiece() {
        logger.info("Checking if lines have been cleared");

        var lineToBeCleared = 0;

        HashSet<GameBlockCoordinate> blocks = new HashSet<>();

        // Checking for horizontal lines

        for (var x = 0; x < cols; x++) {
            var counterX = 0;
            for (var y = 0; y < rows; y++) {
                if (grid.get(x, y) == 0) {
                    // If the block is empty, break the loop
                    break;
                }

                counterX++;

                if (counterX == rows) {
                    lineToBeCleared++;
                    for (int i = 0; i < rows; i++) {
                        // Adding the blocks to be cleared at the end
                        blocks.add(new GameBlockCoordinate(x, i));
                    }
                    logger.info("Horizontal line cleared");
                }
            }
        }

        // Checking for vertical lines
        // The same logic as above, but with the x and y for loops swapped

        for (var y = 0; y < rows; y++) {
            var counterY = 0;
            for (var x = 0; x < cols; x++) {
                if (grid.get(x, y) == 0) {
                    break;
                }

                counterY++;

                if (counterY == cols) {
                    lineToBeCleared++;
                    for (int i = 0; i < cols; i++) {
                        blocks.add(new GameBlockCoordinate(i, y));
                    }
                    logger.info("Vertical line cleared");
                }
            }
        }

        clearLine(blocks);
        // Update the score
        score(lineToBeCleared, blocks);
    }

    /**
     * To clear the line of blocks in the HashSet
     * @param blocks the blocks to be cleared
     * The blocks are added to the HashSet in the afterPiece() method
     * Setting to 0 means that the block is empty
     */
    private void clearLine(HashSet<GameBlockCoordinate> blocks) {
        logger.info("Clearing line");

        for (var block : blocks) {
            grid.set(block.getX(), block.getY(), 0);
        }

        lineClearedListener.clearLine(blocks);
    }

    /**
     * Initialise a new game and set up anything that needs to be done at the start
     */
    public void initialiseGame() {
        logger.info("Initialising game");

        nextPiece();
    }

    /**
     * Handle what should happen when a particular block is clicked
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        logger.info("Block clicked at {}, {}", gameBlock.getX(), gameBlock.getY());

        //Get the position of this block
        int x = gameBlock.getX();
        int y = gameBlock.getY();

        Multimedia.playAudio("pling.wav");

        if (grid.canPlayPiece(currentPiece, x, y)) {
            // Play the piece
            grid.playPiece(currentPiece, x, y);
            nextPiece();
            resetTimer();
        } else {
            // Can't play the piece
        }

        // Check for any lines that needs to be cleared.
        afterPiece();
    }

    /**
     * Handle what should happen when a particular block is clicked
     * @param x x coordinate of the block
     * @param y y coordinate of the block
     */

    public void blockClicked(int x, int y) {
        logger.info("Block clicked at {}, {}", x, y);

        Multimedia.playAudio("pling.wav");

        if (grid.canPlayPiece(currentPiece, x, y)) {
            // Play the piece
            grid.playPiece(currentPiece, x, y);
            nextPiece();
            resetTimer();
        } else {
            // Can't play the piece
        }

        // Check for any lines that needs to be cleared.
        afterPiece();
    }

    /**
     * Get the grid model inside this game representing the game state of the board
     * @return game grid model
     */
    public Grid getGrid() {
        return grid;
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

    /**
     * To manage the scoring of the game
     * @param numberOfLines the number of lines cleared
     * @param blocks the blocks that were cleared
     */

    public void score (int numberOfLines, HashSet<GameBlockCoordinate> blocks) {

        var score = numberOfLines * blocks.size() * getMultiplier() * 10;

        // Makes sure that current score is updated with previous score
        setScore(getScore() + score);

        if (numberOfLines > 0) {
            setMultiplier(getMultiplier() + 1);
        } else {
            setMultiplier(1);
        }

        level();
    }

    /**
     * To manage the level of the game
     * The level is based on the score
     */

    public void level() {
        setLevel(getScore() / 1000);
    }

    /**
     * Listener for when the next piece is spawned
     */

    public void setNextPieceListener(NextPieceListener nextPieceListener) {
        this.nextPieceListener = nextPieceListener;
    }

    /**
     * Listener for when the line is cleared
     */

    public void setLineClearedListener(LineClearedListener lineClearedListener) {
        this.lineClearedListener = lineClearedListener;
    }

    /**
     * Rotate the current piece
     */

    public void rotateCurrentPiece() {
        Multimedia.playAudio("rotate.wav");
        currentPiece.rotate();
    }

    /**
     * Rotate the current piece anticlockwise
     */

    public void rotatePieceAnticlockwise() {
        Multimedia.playAudio("rotate.wav");
        currentPiece.rotate(3);
    }

    /**
     * Swap the current piece with the following piece
     */

    public void swapCurrentPiece() {
        Multimedia.playAudio("transition.wav");
        logger.info("Using the swap piece method");
        var temp = currentPiece;
        currentPiece = followingPiece;
        followingPiece = temp;
        nextPieceListener.nextPiece(currentPiece, followingPiece);
    }

    /**
     * Get the current piece
     * @return current piece
     */

    public GamePiece getCurrentPiece() {
        return currentPiece;
    }

    /**
     * A timer for the piece to be played until it drops
     */
    public int getTimerDelay() {

        int maxDelay = 12000 - 500 * level.get();
        logger.info("Getting timer delay {}", maxDelay);
        return Math.max(2500, maxDelay);
    }

    /**
     * Start the timer by doing it as a fixed rate and changes property when finishes
     */
    public void startTimer() {

        // For when the game is over
        Platform.runLater(() -> {
            if (getLives() == -1) {
                logger.info("Game over");
                gameOverListener.gameOver();
            }
        });

        logger.info("Starting timer");
        timer = new Timer();
        timeLeft = getTimerDelay();
        gameLoopListener.gameLoop(timeLeft);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timeLeft -= 1000;
                if (timeLeft.equals(0)) {
                    Platform.runLater(() -> {
                        lives.set(lives.get() - 1);
                        Multimedia.playAudio("lifelose.wav");
                        multiplier.set(1);
                    });
                    nextPiece();
                    stopTimer();
                    startTimer();
                }
            }
        }, 0, 1000);
    }

    /**
     * Reset the timer
     */
    public void resetTimer() {
        logger.info("Resetting timer");
        timer.cancel();
        startTimer();
    }

    /**
     * Stop the timer
     */
    public void stopTimer() {
        logger.info("Stopping timer");
        timer.cancel();
    }

    /**
     * Listener for the game loop
     */
    public void setOnGameLoop(GameLoopListener listener) {
        this.gameLoopListener = listener;
    }

    /**
     * Listener for when the game is over
     */
    public void setOnGameOver(GameOverListener listener) {
        this.gameOverListener = listener;
    }
}
