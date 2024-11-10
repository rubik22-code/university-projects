package uk.ac.soton.comp1206.scene;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.component.ScoresList;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class ScoresScene extends BaseScene {

  private int scores;

  GridPane gridPane;

  boolean flag;

  String otherPlayerName;
  String myPlayerName;
  int otherPlayerScore;

  // Add a localScores SimpleListProperty to hold the current list of scores in the Scene
  private SimpleListProperty<Pair<String, Integer>> localScores = new SimpleListProperty<>();

  private SimpleListProperty<Pair<String, Integer>> remoteScores = new SimpleListProperty<>();

  ScoresList scoresList;

  private static final String scoresFilePath = "scores.txt";

  private static final Logger logger = LogManager.getLogger(ScoresScene.class);

  /**
   * Constructor for the ScoresScene
   *
   * @param gameWindow the game window
   * @param game       the game
   * @param flag       the flag
   * @param otherPlayerName the other player name
   * @param otherPlayerScore the other player score
   * @param myPlayerName this player name
   *
   */

  public ScoresScene(GameWindow gameWindow, Game game, boolean flag, String otherPlayerName, int otherPlayerScore, String myPlayerName) {
    super(gameWindow);
    this.scores = game.getScore();
    this.flag = flag;
    this.otherPlayerName = otherPlayerName;
    this.otherPlayerScore = otherPlayerScore;
    this.myPlayerName = myPlayerName;
  }

  /**
   * Constructor for the ScoresScene
   *
   * @param gameWindow the game window
   * @param game       the game
   * @param flag       the flag
   */

  public ScoresScene(GameWindow gameWindow, Game game, boolean flag) {
    super(gameWindow);
    this.scores = game.getScore();
    this.flag = flag;
  }

  public void initialise() {
    logger.info("Initialising menu");

    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        shutdown();
      }
    });

    if (!flag) {
      getHighestScore();
    }

    if (flag) {
      loadOnlineScores();
      writeOnlineScore();
    }

  }

  public void shutdown() {
    cleanup();
    Stage stage = (Stage) getScene().getWindow();
    stage.close();
  }

  public void cleanup() {
    Multimedia.stopPlay();
  }

  /**
   * This method will build the grid pane for the scores
   */

  public void build() {
    logger.info("Building " + this.getClass().getName());

    Multimedia.stopPlay();
    Multimedia.playMusic("end.wav");

    root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());

    var scoresPane = new StackPane();
    scoresPane.getStyleClass().add("menu-background");
    root.getChildren().add(scoresPane);

    // Use FXCollections.observableArrayList to make an observable list out of that ArrayList
    ObservableList<Pair<String, Integer>> observableScoreList = FXCollections.observableArrayList();
    localScores.set(observableScoreList);

    // Wrapper around the list so that it can be used in the ScoresList
    scoresList = new ScoresList();
    scoresList.scoresProperty().bindBidirectional(localScores);

    // Online scores
    ObservableList<Pair<String, Integer>> observableRemoteScoreList = FXCollections.observableArrayList();
    remoteScores.set(observableRemoteScoreList);
    ScoresList remoteScoresList = new ScoresList();
    remoteScoresList.scoresProperty().bindBidirectional(remoteScores);

    // Load the scores from the file
    try {
      logger.info("Loading scores from " + scoresFilePath);
      scoresList.loadScores(scoresFilePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Create a GridPane to display the scores
    gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(40);
    gridPane.setVgap(10);

    // Title for the scores
    var title = new Text("Scoreboard:");
    title.getStyleClass().add("title");
    scoresPane.getChildren().add(title);
    StackPane.setAlignment(title, Pos.TOP_LEFT);

    // Button to go back to the menu
    var menu = new Button("Back to menu");
    menu.getStyleClass().add("title");
    root.getChildren().add(menu);
    StackPane.setAlignment(menu, Pos.BOTTOM_LEFT);
    menu.setOnMouseClicked(this::startMenu);

    buildGrid();

    // Add gridPane to the scoresPane
    scoresPane.getChildren().add(gridPane);
    logger.info("Scores pane built");

  }

  /**
   * Return to the main menu
   *
   * @param event the event that triggered this method
   */
  private void startMenu(MouseEvent event) {
    gameWindow.startMenu();
  }

  /**
   * This gives the user a dialog box to enter their name if they got a new high score Then it will
   * update the scores list and the UI
   */

  public void getHighestScore() {
    if (checkHighestScore()) {
      logger.info("New high score!");
      TextInputDialog dialog = new TextInputDialog("Name");
      dialog.setTitle("New high score!");
      dialog.setHeaderText("You got a new high score!");
      dialog.setContentText("Please enter your name:");
      Optional<String> result = dialog.showAndWait();
      result.ifPresent(name -> {
        logger.info("New high score: " + name + " " + scores);
        localScores.add(new Pair<>(name, scores));
        scoresList.writeScores(scoresFilePath, localScores);

        // Update UI
        gridPane.getChildren().clear();
        buildGrid();
      });
    }
  }

  /**
   * This builds the gridPane with the scores
   */
  public void buildGrid() {
    // Headers for the name and score columns
    Text nameHeaders = new Text("Name");
    nameHeaders.getStyleClass().add("score");
    gridPane.add(nameHeaders, 0, 0);

    Text scoreHeaders = new Text("Score");
    scoreHeaders.getStyleClass().add("score");
    gridPane.add(scoreHeaders, 1, 0);

    int row = 1;
    for (Pair<String, Integer> score : localScores) {
      Text name = new Text(score.getKey());
      name.getStyleClass().add("score");
      gridPane.add(name, 0, row);

      // Animation for the scores
      scoresList.reveal();

      Text scoreValue = new Text(score.getValue().toString());
      scoreValue.getStyleClass().add("score");
      gridPane.add(scoreValue, 1, row);

      row++;
    }
  }

  /**
   * Check if the current score is higher than the highest score
   *
   * @return true if the current score is higher than the highest score
   */
  public boolean checkHighestScore() {
    try {
      scoresList.loadScores(scoresFilePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    boolean flag = false;

    for (Pair<String, Integer> pair : localScores) {
      if (pair.getValue() < scores) {
        flag = true;
      } else {
        flag = false;
      }
    }
    return flag;
  }

  /**
   * This method will load the online scores
   */

  public void loadOnlineScores() {
    gameWindow.getCommunicator().addListener(message -> {
      if (!message.startsWith("HISCORES"))
        return;
      {
        Platform.runLater(() -> this.receiveScore(message));
      }
    });

    gameWindow.getCommunicator().send("HISCORES");
  }

  /**
   * This method will receive the online scores
   */

  public void receiveScore(String message) {
    message = message.substring(9);
    String[] scores = message.split("\n");
    for (String score : scores) {
      String[] parts = score.split(":");
      String name = parts[0];
      int value = Integer.parseInt(parts[1]);
      remoteScores.sort((firstPair, secondPair) -> secondPair.getValue().compareTo(firstPair.getValue()));
      remoteScores.add(new Pair<>(name, value));
    }
    // Update UI
    gridPane.getChildren().clear();
    buildOnlineGrid();
  }

  /**
   * This method will write the online scores
   */

  public void writeOnlineScore() {
    if (checkHighestScore()) {
      if (checkHighestOnlineScore()) {
        gameWindow.getCommunicator().send("HISCORES:" + remoteScores.get(0).getKey() + ":" + scores);
      }
    }
  }

  /**
   * This method will check if the current score is higher than the highest online score
   */

  public boolean checkHighestOnlineScore() {
    boolean flag = false;

    for (Pair<String, Integer> pair : remoteScores) {
      if (pair.getValue() < scores) {
        flag = true;
      } else {
        flag = false;
      }
    }
    return flag;
  }

  /**
   * This builds the gridPane with the online scores
   */

  public void buildOnlineGrid() {
    // Headers for the name and score columns
    Text nameHeaders = new Text("Name:");
    nameHeaders.getStyleClass().add("score");
    gridPane.add(nameHeaders, 0, 0);

    Text scoreHeaders = new Text("Score:");
    scoreHeaders.getStyleClass().add("score");
    gridPane.add(scoreHeaders, 1, 0);

    for (int i = 0; i < 4; i++) {
      Pair<String, Integer> score = remoteScores.get(i);
      Text name = new Text(score.getKey());
      name.getStyleClass().add("score");
      gridPane.add(name, 0, i + 1);

      // Animation for the scores
      scoresList.reveal();

      Text scoreValue = new Text(score.getValue().toString());
      scoreValue.getStyleClass().add("score");
      gridPane.add(scoreValue, 1, i + 1);

    }

    Text thisGame = new Text("This game:");
    thisGame.getStyleClass().add("score");
    gridPane.add(thisGame, 0, 5);
    thisGame.setLineSpacing(10);

    Text thisNameHeaders = new Text("Name:");
    thisNameHeaders.getStyleClass().add("score");
    gridPane.add(thisNameHeaders, 0, 6);

    Text thisScoreHeaders = new Text("Score:");
    thisScoreHeaders.getStyleClass().add("score");
    gridPane.add(thisScoreHeaders, 1, 6);

    Text otherNameHeaders = new Text(otherPlayerName);
    otherNameHeaders.getStyleClass().add("score");
    gridPane.add(otherNameHeaders, 0, 7);

    Text otherPlayerHeaders = new Text(String.valueOf(otherPlayerScore));
    otherPlayerHeaders.getStyleClass().add("score");
    gridPane.add(otherPlayerHeaders, 1, 7);

    Text myPlayerHeaders = new Text(myPlayerName);
    myPlayerHeaders.getStyleClass().add("score");
    gridPane.add(myPlayerHeaders, 0, 8);

    Text myScoreHeaders = new Text(String.valueOf(scores));
    myScoreHeaders.getStyleClass().add("score");
    gridPane.add(myScoreHeaders, 1, 8);
  }
}

