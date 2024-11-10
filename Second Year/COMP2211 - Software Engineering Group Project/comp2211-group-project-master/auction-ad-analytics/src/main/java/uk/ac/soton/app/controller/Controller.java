package uk.ac.soton.app.controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uk.ac.soton.app.Application;
import uk.ac.soton.app.model.State;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.apache.logging.log4j.Logger;

/**
 * Simple representation of a view controller object
 * -    Used to interact with .fxml files
 * -    Provides default implementations that should be common across controllers to ease implementation
 */
public class Controller
{
    protected Logger logger;

    protected Stage stage;
    protected HostServices hostServices;

    @FXML
    protected StackPane container;

    /**
     * Default implementation for initStage() method
     */
    public void initStage() {}

    /**
     * Sets the stage being managed by the controller
     * @param stage the window holding the application's scenes
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    /**
     * Sets the logger for the application
     * @param logger log wood chuck
     */
    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * Sets the host services for the controller
     * @param hostServices the service hosting the application
     */
    public void setHostServices(HostServices hostServices)
    {
        this.hostServices = hostServices;
    }

    /**
     * Gets the global state
     * @return state object
     */
    public State getState()
    {
        return ( (State) stage.getUserData() );
    }

    /**
     * Opens the help PDF
     */
    public void openHelpDoc()
    {
        logger.info("Attempting to open help document");
        File tempFile;
        try {
            InputStream in = Controller.class.getResource("user_guide.pdf").openStream();
            if (in == null) return;

            tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".pdf");
            tempFile.deleteOnExit();

            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
        } catch ( Exception e ) {
            logger.info("Failed to open help document");
            return;
        }
        hostServices.showDocument(tempFile.getAbsolutePath());
        logger.info("Opened help document successfully");
    }

    /**
     * Switches to a different scene under the same window (stage)
     * @param sceneName the name of the scene to be switched to
     */
    protected void setScene(String sceneName)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(sceneName + ".fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            logger.info("Failed to load {}.fxml", sceneName);
            stage.close();
        }
        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setLogger(logger);
        controller.setHostServices(hostServices);
        controller.initStage();

        // If no file is selected when logging in, keep the user on the login screen
        State state = (State) stage.getUserData();
        if ( Objects.equals(sceneName, "dashboard") && state.getCampaignCount() == 0 ) return;

        Scene scene = new Scene(root);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(state.getStyle().asPath());

        // Set stage title to fxml document's name with first character capitalised
        stage.setTitle(sceneName.substring(0, 1).toUpperCase() + sceneName.substring(1));
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
    }
}
