package uk.ac.soton.comp1206.scene;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.component.ScoresList;
import uk.ac.soton.comp1206.event.BlockHoveredListener;
import uk.ac.soton.comp1206.event.SwapPieceListener;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Game game;

    PieceBoard pieceBoardCurrent;
    PieceBoard pieceBoardFollowing;

    GameBoard board;

    StackPane challengePane;

    private SwapPieceListener swapPieceListener;
    private BlockHoveredListener blockHoveredListener;

    Rectangle timerBar;
    Timeline timeline;

    int highScore = 0;

    ScoresList scoresList = new ScoresList();
    List<Pair<String, Integer>> defaultScores;

    /**
     * Create a new Single Player challenge scene
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Challenge Scene");
    }

    /**
     * Build the Challenge window
     */
    @Override
    public void build() {
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

        var mainPane = new BorderPane();
        challengePane.getChildren().add(mainPane);

        board = new GameBoard(
            game.getGrid(),gameWindow.getWidth()/2,gameWindow.getWidth()/2);
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
        score.textProperty().bind(Bindings.concat("Score: ", game.scoreProperty()));
        challengePane.getChildren().add(score);
        StackPane.setAlignment(score, Pos.TOP_LEFT);

        var level = new Label();
        level.getStyleClass().add("level");
        level.textProperty().bind(Bindings.concat("Level: ", game.levelProperty()));
        challengePane.getChildren().add(level);
        StackPane.setAlignment(level, Pos.TOP_RIGHT);

        var lives = new Label();
        lives.getStyleClass().add("lives");
        lives.textProperty().bind(Bindings.concat("Lives: ", game.livesProperty()));
        challengePane.getChildren().add(lives);
        StackPane.setAlignment(lives, Pos.TOP_CENTER);

        var multiplier = new Label();
        multiplier.getStyleClass().add("multiplier");
        multiplier.textProperty().bind(Bindings.concat("Multiplier: ", game.multiplierProperty()));
        challengePane.getChildren().add(multiplier);
        StackPane.setAlignment(multiplier, Pos.BOTTOM_RIGHT);

        //Handle block on gameboard grid being clicked
        board.setOnBlockClick(this::blockClicked);

        board.setOnRightClicked(this::rightClicked);

        game.setNextPieceListener(this::nextPieceListener);

        pieceBoardCurrent.setOnRightClicked(this::rightClicked);

        pieceBoardCurrent.setOnBlockClick(event -> this.rightClicked(null));

        // pieceBoardCurrent.setOnSwapPiece(event -> this.swapPiece());

        game.setLineClearedListener(this::clearLine);

        game.setOnGameLoop(this::gameLoop);

        game.setOnGameOver(this::gameOver);

        // Timer bar for the game
        timerBar = new Rectangle(0, 0, gameWindow.getWidth(), 10);
        challengePane.getChildren().add(timerBar);
        StackPane.setAlignment(timerBar, Pos.BOTTOM_CENTER);

        // Get the high score and display it in the UI
        var highScoreLabel = new Label();
        highScoreLabel.getStyleClass().add("score");
        highScoreLabel.textProperty().bind(Bindings.concat("High Score: ", getInitialHighScore(String.valueOf(Path.of("scores.txt")))));
        challengePane.getChildren().add(highScoreLabel);
        StackPane.setAlignment(highScoreLabel, Pos.CENTER_RIGHT);

        // Add listener to scoreProperty to update high score
        game.scoreProperty().addListener((observable, oldValue, newValue) -> {
            int newHighScore = newValue.intValue();
            if (newHighScore > getHighScore()) {
                setHighScore(newHighScore);
                highScoreLabel.textProperty().unbind();
                highScoreLabel.textProperty().setValue("High Score: " + getHighScore());

            }
        });

    }

    /**
     * Handle when a block is clicked
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClicked(GameBlock gameBlock) {
        game.blockClicked(gameBlock);
    }

    /**
     * Handle when a block is right clicked
     * @param event not used in this method
     */

    private void rightClicked(MouseEvent event) {
        game.rotateCurrentPiece();
        pieceBoardCurrent.displayPiece(game.getCurrentPiece());
        pieceBoardCurrent.getBlock(1,1).createCircle();
    }

    /**
     * Setup the game object and model
     */
    public void setupGame() {
        logger.info("Starting a new challenge");

        //Start new game
        game = new Game(5, 5);
    }

    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Challenge");
        game.start();

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
                    game.rotatePieceAnticlockwise();
                    pieceBoardCurrent.displayPiece(game.getCurrentPiece());
                    pieceBoardCurrent.getBlock(1,1).createCircle();
                    break;
                case E:
                case C:
                case CLOSE_BRACKET:
                    game.rotateCurrentPiece();
                    pieceBoardCurrent.displayPiece(game.getCurrentPiece());
                    pieceBoardCurrent.getBlock(1,1).createCircle();
                    break;
                case X:
                case ENTER:
                    game.blockClicked(board.getX(), board.getY());
                    pieceBoardCurrent.displayPiece(game.getCurrentPiece());
                    pieceBoardCurrent.getBlock(1,1).createCircle();
                    break;
                case SPACE:
                case R:
                    game.swapCurrentPiece();
                    pieceBoardCurrent.displayPiece(game.getCurrentPiece());
                    pieceBoardCurrent.getBlock(1,1).createCircle();
                    break;
                case ESCAPE:
                    game.stopTimer();
                    shutdown();
                    break;
            }
        });
    }

    /**
     * Shutdown the scene and return to the menu
     */
    public void shutdown() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new MenuScene(gameWindow).getScene());
        stage.show();
        gameWindow.startMenu();
    }

    /**
     * For when the game is over
     */

    public void gameOver() {
        logger.info("Game over");
        game.stopTimer();
        Multimedia.stopPlay();
        Platform.runLater(() -> gameWindow.startScores(game, false));
    }

    /**
     * Listener for the next piece
     * @param nextPiece the next piece
     */

    public void nextPieceListener(GamePiece nextPiece, GamePiece followingPiece) {
        logger.info("Next piece listener called");
        pieceBoardCurrent.displayPiece(nextPiece);
        pieceBoardFollowing.displayPiece(followingPiece);
        pieceBoardCurrent.getBlock(1,1).createCircle();
    }

    /**
     * Clear a line of blocks on the board
     * @param blocks the set of blocks to clear
     */
    public void clearLine(Set<GameBlockCoordinate> blocks) {
        board.fadeOut(blocks);
    }

    /**
     * Handle the game loop
     * @param duration the duration of the game loop
     */
    public void gameLoop(int duration) {

        startAnimation(game.getTimerDelay());
    }

    /**
     * Start the timer animation
     * @param duration the duration of the animation
     */

    public void startAnimation(double duration) {

        var scaleProperty = timerBar.widthProperty();

        timerBar.setWidth(gameWindow.getWidth());
        timerBar.setFill(Color.GREEN);

        this.timeline = new Timeline(
                new KeyFrame(Duration.millis(duration), new KeyValue(scaleProperty, 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(timerBar.fillProperty(), Color.YELLOW)),
                new KeyFrame(Duration.seconds(8), new KeyValue(timerBar.fillProperty(), Color.RED))
        );
        this.timeline.play();
    }

    /**
     * Gets the initial high score from the scores file
     */
    public int getInitialHighScore(String filePath) {
        if (highScore == 0) {
            File scoresFile = new File(filePath);
            if (!scoresFile.exists()) {
                defaultScores = scoresList.getDefaultScores();
                scoresList.writeScores(filePath, defaultScores);
                return 666; // Default value
            }
            else {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(filePath));
                    String firstLine = reader.readLine();
                    reader.close();
                    String[] split = firstLine.split(":");
                    highScore = Integer.parseInt(split[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return 10000; // Default value
                }
            }
        }
        return highScore;
    }

    /**
     * Gets the high score
     * @return the high score
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Sets the high score
     * @param newHighScore the new high score
     */

    public void setHighScore(int newHighScore) {
        highScore = newHighScore;
    }
}
