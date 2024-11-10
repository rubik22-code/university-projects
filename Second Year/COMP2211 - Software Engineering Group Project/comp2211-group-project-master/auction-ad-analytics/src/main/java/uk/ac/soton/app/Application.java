package uk.ac.soton.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.ac.soton.app.controller.Controller;
import uk.ac.soton.app.model.State;

import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Bootstraps the application as a whole
 * -    Sets up JavaFX stage and scenes
 */
public class Application extends javafx.application.Application
{
    private final static String DEFAULT_SCENE = "login";
    private final Logger logger = LogManager.getLogger(Application.class.getName());

    /**
     * Starts the application on the window
     * @param stage the stage (window) used by the application
     * @throws IOException default scene failed to load
     */
    @Override
    public void start(Stage stage) throws IOException
    {
        State state = new State();
        stage.setUserData(state);

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(DEFAULT_SCENE + ".fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setHostServices(getHostServices());
        controller.setLogger(logger);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(state.getStyle().asPath());

        // Set stage title to fxml document's name with first character capitalised
        stage.setTitle(DEFAULT_SCENE.substring(0, 1).toUpperCase() + DEFAULT_SCENE.substring(1));
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());

        logger.info("Application started");
    }

    public static void main(String[] args)
    {
        launch();
    }
}