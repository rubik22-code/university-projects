package uk.ac.soton.comp1206.scene;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The main menu of the game. Provides a gateway to the rest of the game.
 */
public class MenuScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);

    /**
     * Create a new menu scene
     * @param gameWindow the Game Window this will be displayed in
     */
    public MenuScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Menu Scene");
    }

    /**
     * Build the menu layout
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        Multimedia.playMusic("game_start.mp3");

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var menuPane = new StackPane();
        menuPane.setMaxWidth(gameWindow.getWidth());
        menuPane.setMaxHeight(gameWindow.getHeight());
        menuPane.getStyleClass().add("menu-background");
        root.getChildren().add(menuPane);

        var mainPane = new BorderPane();
        menuPane.getChildren().add(mainPane);

        String imageToView = InstructionsScene.class.getResource("/images/TetrECS.png").toExternalForm();
        ImageView imageView = new ImageView(imageToView);
        imageView.setFitWidth(gameWindow.getWidth() - 200);
        imageView.setFitHeight(gameWindow.getHeight() - 200);
        imageView.setPreserveRatio(true);

        var imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_CENTER);
        menuPane.getChildren().add(imageContainer);
        imageContainer.setPadding(new Insets(150, 0, 0, 0));

        // Create ScaleTransition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), imageView);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);

        scaleTransition.play();

        Label playButton = new Label("Play");


        Label multiplayer = new Label("Multiplayer");


        Label instructions = new Label("Instructions");


        Label quitGame = new Label("Quit");

        var vboxMenuButtons = new VBox(20, playButton, multiplayer, instructions, quitGame);

        vboxMenuButtons.setAlignment(Pos.BOTTOM_CENTER);

        menuPane.getChildren().add(vboxMenuButtons);
        vboxMenuButtons.setPadding(new Insets(0, 0, 25, 0));
        vboxMenuButtons.getStyleClass().add("menuItemEnhanced");



        // Bind the button action to the startInstructions method in the menu
        instructions.setOnMouseClicked(this::startInstructions);

        //Bind the button action to the startGame method in the menu
        playButton.setOnMouseClicked(this::startGame);

        // Bind the button action to the startLobby method in the menu
        multiplayer.setOnMouseClicked(this::startLobby);

        quitGame.setOnMouseClicked(event -> {
            shutdown();
        });
    }

    /**
     * Initialise the menu
     */
    @Override
    public void initialise() {
        logger.info("Initialising menu");

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                shutdown();
            }
        });
    }

    /**
     * Handle when the Start Game button is pressed
     * @param event event
     */
    private void startGame(MouseEvent event) {
        gameWindow.startChallenge();
    }

    /**
     * Handle when the Instructions button is pressed
     * @param event event
     */

    private void startInstructions(MouseEvent event) {
        gameWindow.startInstructions();
    }

    /**
     * Handle when the Multiplayer button is pressed
     * @param event event
     */

    private void startLobby(MouseEvent event) {
        Multimedia.stopPlay();
        gameWindow.startLobby();
    }

    /**
     * Handle when the Quit button is pressed
     */

    public void shutdown() {
        cleanup();
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }

    /**
     * Handle when the Quit button is pressed
     */

    public void cleanup() {
        Multimedia.stopPlay();
    }
}
