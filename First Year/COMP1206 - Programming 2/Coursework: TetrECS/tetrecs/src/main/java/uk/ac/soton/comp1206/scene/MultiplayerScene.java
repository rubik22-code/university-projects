package uk.ac.soton.comp1206.scene;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.MultiplayerGame;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class MultiplayerScene extends ChallengeScene {

  private static final Logger logger = LogManager.getLogger(MultiplayerScene.class);

  MultiplayerGame multiplayerGame;

  TextArea chatArea;

  VBox chatBoxContainer;

  TextField chatInput;

  Label sendChat;

  Label quitChat;

  String otherPlayerName;
  String myPlayerName;
  int otherPlayerScore;

  String otherPlayerLives;

  Label otherPlayerScoreLabel;

  /**
   * Create a new Multi player challenge scene
   *
   * @param gameWindow the Game Window
   */
  public MultiplayerScene(GameWindow gameWindow) {
    super(gameWindow);
  }

  /**
   * Set up the game
   */
  public void setupGame() {
    logger.info("Starting a new challenge");

    //Start new game
    multiplayerGame = new MultiplayerGame(5, 5, gameWindow);
  }



    public void initialise () {
      logger.info("Initialising Multiplayer Challenge");

      for (int i = 0; i < 2; i++) {
        gameWindow.getCommunicator().send("PIECE");
      }

      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      multiplayerGame.start();

      // Key presses

      scene.setOnKeyPressed(keyPressed -> {
        switch (keyPressed.getCode()) {
          case LEFT:
          case A:
            board.movePieceLeft();
            break;
          case RIGHT:
          case D:
            board.movePieceRight();
            break;
          case DOWN:
          case S:
            board.movePieceDown();
            break;
          case UP:
          case W:
            board.movePieceUp();
            break;
          case Q:
          case Z:
          case OPEN_BRACKET:
            multiplayerGame.rotatePieceAnticlockwise();
            pieceBoardCurrent.displayPiece(multiplayerGame.getCurrentPiece());
            pieceBoardCurrent.getBlock(1, 1).createCircle();
            break;
          case E:
          case C:
          case CLOSE_BRACKET:
            multiplayerGame.rotateCurrentPiece();
            pieceBoardCurrent.displayPiece(multiplayerGame.getCurrentPiece());
            pieceBoardCurrent.getBlock(1, 1).createCircle();
            break;
          case X:
          case ENTER:
            multiplayerGame.blockClicked(board.getX(), board.getY());
            pieceBoardCurrent.displayPiece(multiplayerGame.getCurrentPiece());
            pieceBoardCurrent.getBlock(1, 1).createCircle();
            break;
          case SPACE:
          case R:
            multiplayerGame.swapCurrentPiece();
            pieceBoardCurrent.displayPiece(multiplayerGame.getCurrentPiece());
            pieceBoardCurrent.getBlock(1, 1).createCircle();
            break;
          case ESCAPE:
            multiplayerGame.stopTimer();
            shutdown();
            break;
          case T:
            chatBoxContainer.getChildren().addAll(chatArea, chatInput, sendChat, quitChat);
            root.getChildren().add(chatBoxContainer);
            break;
          case O:
            chatBoxContainer.getChildren().removeAll(chatArea, chatInput, sendChat, quitChat);
            root.getChildren().remove(chatBoxContainer);
            break;
        }
      });
    }

    /**
     * Builds the scene
     */

    public void build () {

      if (multiplayerGame != null) {
        var otherScore = new Label();
        otherScore.getStyleClass().add("lives");
        otherScore.textProperty().bind(Bindings.concat(
            multiplayerGame.getOtherPlayer() + ": " + multiplayerGame.getOtherScore()));
        challengePane.getChildren().add(otherScore);
        StackPane.setAlignment(otherScore, Pos.BOTTOM_CENTER);
      }

      logger.info("Building " + this.getClass().getName());

      setupGame();

      Multimedia.stopPlay();
      Multimedia.playMusic("game.mp3");

      root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

      challengePane = new StackPane();
      challengePane.setMaxWidth(gameWindow.getWidth());
      challengePane.setMaxHeight(gameWindow.getHeight());
      challengePane.getStyleClass().add("menu-background");
      root.getChildren().add(challengePane);

      fetchScores();
      incomingMessages();

      var mainPane = new BorderPane();
      challengePane.getChildren().add(mainPane);

      board = new GameBoard(
          multiplayerGame.getGrid(),gameWindow.getWidth()/2,gameWindow.getWidth()/2);
      mainPane.setCenter(board);

      // For the current piece
      pieceBoardCurrent = new PieceBoard(3,3, gameWindow.getWidth()/4,gameWindow.getWidth()/4);
      mainPane.setRight(pieceBoardCurrent);

      // For the following piece
      pieceBoardFollowing = new PieceBoard(3,3, gameWindow.getWidth()/6,gameWindow.getWidth()/6);
      mainPane.setBottom(pieceBoardFollowing);

      // Create a pane to hold the score, level, lives and multiplier, binding them to the game properties

      var score = new Label();
      score.getStyleClass().add("score");
      score.textProperty().bind(Bindings.concat("Score: ", multiplayerGame.scoreProperty()));
      challengePane.getChildren().add(score);
      StackPane.setAlignment(score, Pos.TOP_LEFT);

      var level = new Label();
      level.getStyleClass().add("level");
      level.textProperty().bind(Bindings.concat("Level: ", multiplayerGame.levelProperty()));
      challengePane.getChildren().add(level);
      StackPane.setAlignment(level, Pos.TOP_RIGHT);

      var lives = new Label();
      lives.getStyleClass().add("lives");
      lives.textProperty().bind(Bindings.concat("Lives: ", multiplayerGame.livesProperty()));
      challengePane.getChildren().add(lives);
      StackPane.setAlignment(lives, Pos.TOP_CENTER);

      var multiplier = new Label();
      multiplier.getStyleClass().add("multiplier");
      multiplier.textProperty().bind(Bindings.concat("Multiplier: ", multiplayerGame.multiplierProperty()));
      challengePane.getChildren().add(multiplier);
      StackPane.setAlignment(multiplier, Pos.BOTTOM_RIGHT);

      //Handle block on gameboard grid being clicked
      board.setOnBlockClick(this::blockClicked);

      board.setOnRightClicked(this::rightClicked);

      multiplayerGame.setNextPieceListener(this::nextPieceListener);

      pieceBoardCurrent.setOnRightClicked(this::rightClicked);

      pieceBoardCurrent.setOnBlockClick(event -> this.rightClicked(null));

      multiplayerGame.setLineClearedListener(this::clearLine);

      multiplayerGame.setOnGameLoop(this::gameLoop);

      multiplayerGame.setOnGameOver(this::gameOver);

      // Timer bar for the game
      timerBar = new Rectangle(0, 0, gameWindow.getWidth(), 10);
      challengePane.getChildren().add(timerBar);
      StackPane.setAlignment(timerBar, Pos.BOTTOM_CENTER);

      // Get the high score and display it in the UI
      otherPlayerScoreLabel = new Label();
      otherPlayerScoreLabel.getStyleClass().add("score");
      otherPlayerScoreLabel.textProperty().bind(Bindings.concat(getOtherPlayerName() + ": " + getOtherPlayerScore()));
      challengePane.getChildren().add(otherPlayerScoreLabel);
      StackPane.setAlignment(otherPlayerScoreLabel, Pos.CENTER_RIGHT);

      chatBoxContainer = new VBox();
      chatWindow();
    }

    /**
     * When the game is over, stop the timer and display the scores
     */
    public void gameOver () {
      logger.info("Game over");
      multiplayerGame.stopTimer();
      Multimedia.stopPlay();
      gameWindow.getCommunicator().send("PART");
      gameWindow.getCommunicator().send("DIE");
      Platform.runLater(() -> gameWindow.startScoresMulti(multiplayerGame, true, otherPlayerName, otherPlayerScore, myPlayerName));
    }

    public void shutdown () {
      super.shutdown();
      gameWindow.getCommunicator().send("DIE");
    }

    /**
     * Chat window for multiplayer from the lobby
     */

    public void chatWindow () {
      chatArea = new TextArea();
      chatArea.setPrefHeight(50);
      chatArea.setPrefWidth(100);
      chatArea.setEditable(false);

      chatInput = new TextField();
      chatInput.setPromptText("Enter message");


      sendChat = new Label("Send");
      sendChat.getStyleClass().add("buttons");


      quitChat = new Label("Quit");
      quitChat.getStyleClass().add("buttons");


      chatBoxContainer.setAlignment(Pos.BOTTOM_CENTER);
      chatBoxContainer.setPadding(new Insets(0, 0, 30, 0));

      sendChat.setOnMouseClicked(event -> {
        gameWindow.getCommunicator().send("MSG " + chatInput.getText());
        chatInput.clear();
      });

      quitChat.setOnMouseClicked(event -> {
        chatBoxContainer.getChildren().removeAll(chatArea, chatInput, sendChat, quitChat);
      });

      gameWindow.getCommunicator().addListener(message -> {
        if (!message.startsWith("MSG"))
          return;
        {
          Platform.runLater(() -> this.receiveChat(message));
        }
      });
    }

    /**
     * Receive chat messages from other players
     * @param message The message received
     */

  public void receiveChat(String message) {
    message = message.substring(4);
    String[] filter = message.split(":");
    String sender = filter[0];
    String content = filter[1];

    chatArea.appendText(sender + ": " + content + "\n");
  }

    public void gameLoop ( int duration) {

      startAnimation(multiplayerGame.getTimerDelay());
    }

  private void rightClicked(MouseEvent event) {
    multiplayerGame.rotateCurrentPiece();
    pieceBoardCurrent.displayPiece(multiplayerGame.getCurrentPiece());
    pieceBoardCurrent.getBlock(1,1).createCircle();
  }

  private void blockClicked(GameBlock gameBlock) {
    multiplayerGame.blockClicked(gameBlock);
  }

  /**
   * Ask the other player for their score
   */

  public void fetchScores() {

    // Request scores from other players using the communicator
    TimerTask requestChannelsTaskScores = new TimerTask() {
      @Override
      public void run() {
        gameWindow.getCommunicator().send("SCORES");
      }
    };

    // Repeat the request every 5 seconds
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(requestChannelsTaskScores, 0, 5000);
  }

  public String getOtherPlayerName() {
    return otherPlayerName;
  }

  public int getOtherPlayerScore() {
    return otherPlayerScore;
  }

  /**
   * Receive messages from other players
   */

  public void incomingMessages() {
    gameWindow.getCommunicator().addListener(message -> {
      if (!message.startsWith("SCORES"))
        return;
      {
        Platform.runLater(() -> this.receiveScores(message));
      }
    });
  }

  /**
   * Receive scores from other players
   * @param message The message received
   */

  public void receiveScores(String message) {
    message = message.substring(6);
    String[] filter = message.split("\n");

    if (filter.length == 1) {
      for (String line : filter) {
        String[] parts = line.split(":");
        myPlayerName = parts[0];
        otherPlayerName = parts[0];
        multiplayerGame.setScore(Integer.parseInt(parts[1]));
        otherPlayerScore = Integer.parseInt(parts[1]);
        // multiplayerGame.setLives(Integer.parseInt(parts[2]));
        otherPlayerLives = parts[2];
      }
    }

    if (filter.length == 2) {

      String[] firstPart = filter[0].split(":");
      myPlayerName = firstPart[0];
      multiplayerGame.setScore(Integer.parseInt(firstPart[1]));

      String[] secondPart = filter[1].split(":");
      otherPlayerName = secondPart[0];
      otherPlayerScore = Integer.parseInt(secondPart[1]);
      otherPlayerLives = secondPart[2];
    }

    System.out.println("The other player name is" + otherPlayerName);
    System.out.println("The other player score is" + otherPlayerScore);
    System.out.println("The other player lives is" + otherPlayerLives);

    if (otherPlayerName != null) {
      otherPlayerScoreLabel.textProperty().unbind();
      otherPlayerScoreLabel.textProperty().setValue(getOtherPlayerName() + ": " + getOtherPlayerScore());
    }

    if (otherPlayerLives != null) {
      if (otherPlayerLives.equals("DEAD")) {
        gameOver();
      }
    }
  }
}
