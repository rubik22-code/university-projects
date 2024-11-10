package uk.ac.soton.comp1206.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;
import uk.ac.soton.comp1206.network.Communicator;
import uk.ac.soton.comp1206.ui.GameWindow;

public class MultiplayerGame extends Game {

  Communicator communicator;

  public String otherPlayer;

  public int otherScore;

  GameWindow gameWindow;

  private static final Logger logger = LogManager.getLogger(MultiplayerGame.class);

  Queue<Integer> pieces = new LinkedList<>();

  /**
   * Create a new game with the specified rows and columns. Creates a corresponding grid model.
   *
   * @param cols number of columns
   * @param rows number of rows
   */

  public MultiplayerGame(int cols, int rows, GameWindow gameWindow, Game game) {
    super(cols, rows);
    this.gameWindow = gameWindow;
  }

  /**
   * Create a new game with the specified rows and columns. Creates a corresponding grid model.
   *
   * @param cols number of columns
   * @param rows number of rows
   */

  public MultiplayerGame(int cols, int rows, GameWindow gameWindow) {
    super(cols, rows);
    this.gameWindow = gameWindow;

  }

  /**
   * Initialise the game
   */

  public void initialiseGame() {

    // Add a listener to the communicator to receive messages such as PIECE

    gameWindow.getCommunicator().addListener(message -> {
      if(message.startsWith("PIECE")) {
        receiveQueue(message);
      }
    });

    // Add a listener to the communicator to receive messages such as SCORE

    gameWindow.getCommunicator().addListener(message -> {
          if ("SCORE".equals(message)) {
            Platform.runLater(() -> receiveScore(message));
          }
    });

    nextPiece();
  }

  /**
   * Start the timer by doing it as a fixed rate and changes property when finishes
   */
  public void startTimer() {

    logger.info("The lives is " + getLives());

    // Run the game over listener if the lives are -1
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
   * Calls the next piece method
   * @return the next piece
   */
  @Override
  public GamePiece nextPiece() {
    logger.info("Next piece requested");

    // If the queue is empty, send a message to the server to request a new piece

    while (pieces.size() < 3) {
      gameWindow.getCommunicator().send("PIECE");
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    if (followingPiece == null) {
      followingPiece = spawnPiece(pieces.remove());
    }

    currentPiece = followingPiece;
    followingPiece = spawnPiece(pieces.remove());
    nextPieceListener.nextPiece(currentPiece, followingPiece);
    logger.info("Next piece is {}", currentPiece);
    return currentPiece;
  }

  /**
   * Receive the queue from the server
   */

  public void receiveQueue(String message) {
    message = message.substring(5);
    pieces.add(Integer.parseInt(message.trim()));
    logger.debug("QUEUE {}", pieces);
    System.out.println("The queue has been filled");
  }

  /**
   * Receive the score from the server
   */

  public void receiveScore(String message) {
    message = message.substring(5);
    String[] parts = message.split(":");
    otherPlayer = parts[0];
    otherScore = Integer.parseInt(parts[1]);
  }

  /**
   * Get the other player's score
   */

  public int getOtherScore() {
    return otherScore;
  }

  /**
   * Get the other player's name
   */

  public String getOtherPlayer() {
    return otherPlayer;
  }

  /**
   * Score the game
   *
   * @param numberOfLines the number of lines cleared
   * @param blocks the blocks that were cleared
   */

  public void score (int numberOfLines, HashSet<GameBlockCoordinate> blocks) {
    super.score(numberOfLines, blocks);
    gameWindow.getCommunicator().send("SCORE " + (getScore()));
  }

  /**
   * Spawn a new piece
   *
   * @param newPiece the piece to spawn
   * @return the new piece
   */

  public GamePiece spawnPiece(int newPiece) {
    var piece = GamePiece.createPiece(newPiece);
    return piece;
  }

  /**
   * Called when a block is clicked
   *
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
   * Start the game
   */

  public void start() {
    logger.info("Starting game");
    startTimer();
    initialiseGame();
  }

  /**
   * Setting the score
   * @param newScore the new score
   */
  public void setScore(int newScore) {
    score.set(newScore);
  }

  /**
   * Setting the lives
   * @param newLives the new lives
   */
  public void setLives(int newLives) {
    lives.set(newLives);
  }

}