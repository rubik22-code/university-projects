package uk.ac.soton.comp1206.scene;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class InstructionsScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(InstructionsScene.class);

  /**
   * Create a new scene, passing in the GameWindow the scene will be displayed in
   *
   * @param gameWindow the game window
   */

  public InstructionsScene(GameWindow gameWindow) {
    super(gameWindow);
    logger.info("Creating Instructions Scene");
  }

  /**
   * Initialise this scene. Called after creation
   */
  public void initialise() {
    logger.info("Initialising Instructions");

    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        shutdown();
      }
    });
  }

  /**
   * Build the layout of the scene
   */
  public void build() {
    logger.info("Building " + this.getClass().getName());

    VBox instructions = new VBox();
    instructions.getChildren().add(new Text("Instructions"));
    String imageToView = InstructionsScene.class.getResource("/images/Instructions.png").toExternalForm();
    ImageView imageView = new ImageView(imageToView);
    imageView.setFitWidth(gameWindow.getWidth() - 150);
    imageView.setFitHeight(gameWindow.getHeight() - 150);
    imageView.setPreserveRatio(true);

    Multimedia.stopPlay();
    Multimedia.playMusic("game_start.mp3");

    root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

    var menuPane = new StackPane();
    menuPane.setMaxWidth(gameWindow.getWidth());
    menuPane.setMaxHeight(gameWindow.getHeight());
    menuPane.getStyleClass().add("menu-background");
    root.getChildren().add(menuPane);


    menuPane.getChildren().add(imageView);
    menuPane.setAlignment(imageView, Pos.BOTTOM_CENTER);

    // Gridpane for dynamic content

    GridPane piecesGrid = new GridPane();
    piecesGrid.setAlignment(Pos.TOP_CENTER);
    menuPane.getChildren().add(piecesGrid);

    for (int i = 0; i < 15; i++) {
      PieceBoard pieceBoard = new PieceBoard(3, 3, 100, 100);
      pieceBoard.displayPiece(GamePiece.createPiece(i));

      if (i > 7) {
        piecesGrid.add(pieceBoard, i - 8, 1);
      }
      else {
        piecesGrid.add(pieceBoard, i, 0);
      }
    }

    var mainPane = new BorderPane();
    menuPane.getChildren().add(mainPane);

    var gamePieceHeading = new Text("Game Pieces:");
    gamePieceHeading.getStyleClass().add("title");
    mainPane.setTop(gamePieceHeading);

    var instructionsHeading = new Text("Instructions:");
    instructionsHeading.getStyleClass().add("title");
    mainPane.setBottom(instructionsHeading);

    var menu = new Button("Back to Menu");
    mainPane.setBottom(menu);

    // Bind the button action to the startGame method in the menu
    menu.setOnAction(this::startMenu);
  }

  /**
   * Return to the main menu
   * @param event the event that triggered this method
   */
  private void startMenu(ActionEvent event) {
    Multimedia.stopPlay();
    gameWindow.startMenu();
  }

  /**
   * Shutdown the instructions scene
   */
  public void shutdown() {
    Stage stage = (Stage) getScene().getWindow();
    stage.setScene(new MenuScene(gameWindow).getScene());
    stage.show();
    gameWindow.startMenu();
  }
}
