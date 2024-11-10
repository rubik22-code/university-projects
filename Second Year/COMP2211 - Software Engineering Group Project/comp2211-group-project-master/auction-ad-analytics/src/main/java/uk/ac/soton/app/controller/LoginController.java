package uk.ac.soton.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uk.ac.soton.app.exceptions.ApplicationException;
import uk.ac.soton.app.exceptions.ConnectionError;
import uk.ac.soton.app.exceptions.InvalidLogin;
import uk.ac.soton.app.model.DataBase.DatabaseManager;
import uk.ac.soton.app.model.State;
import uk.ac.soton.app.enums.Privilege;

/**
 * Login-specific controller that acts as a middleman between the login page and the database
 * -    Reads in username and password from login page
 * -    Talks to database manager to see if username / password combination is valid
 * -    Create user object to store username and priority levels so user information can be accessed during app use
 * -    Switch to dashboard scene
 */
public class LoginController extends Controller
{
    @FXML
    private TextField username_box;
    @FXML
    private PasswordField password_box;
    @FXML
    private Button login_button;

    /**
     * Resets the login button
     */
    @Override
    public void initStage()
    {
        login_button.setDisable(false);
    }

    /**
     * Checks whether the tab key has been pressed and the password box should instead be selected
     * @param key the key that's been pressed
     */
    public void usernameKeyPress(KeyEvent key)
    {
        if ( key.getCode() != KeyCode.TAB ) return;
        password_box.requestFocus();
    }

    /**
     * Checks whether the enter key has been pressed, if so it should attempt to log in
     * @param key the key that's been pressed
     */
    public void passwordKeyPress(KeyEvent key)
    {
        if ( key.getCode() == KeyCode.TAB ) username_box.requestFocus();
        if ( key.getCode() != KeyCode.ENTER ) return;
        attemptLogin();
    }

    /**
     * Attempts to log in the user based on the given credentials, happens when the login button is pressed
     */
    public void attemptLogin() {
        // Connect to the database manager
        State state = getState();
        login_button.setDisable(true);
        DatabaseManager databaseManager = state.getDatabaseManager();
        if ( databaseManager == null ) {
            try {
                databaseManager = new DatabaseManager();
                state.setDatabaseManager(databaseManager);
            } catch ( ConnectionError e) {
                e.displayError();
                login_button.setDisable(false);
                return;
            }
        }

        // Attempt login
        String username = username_box.getText();
        String password = password_box.getText();
        logger.info("Attempting to login user {}", username);

        Privilege privilege;
        try {
            privilege = databaseManager.authenticateLogin(username, password);
        } catch ( ApplicationException e ) {
            logger.info("Error while interacting with the database");
            e.displayError();
            login_button.setDisable(false);
            return;
        }
        if ( privilege == null ) {
            logger.info("Invalid username / password combination used");
            new InvalidLogin().displayError();
            login_button.setDisable(false);
            return;
        }
        state.setUser(username, privilege);
        logger.info("Successfully logged in as user {}", username);

        // Switch scene
        login_button.setDisable(false);
        setScene("dashboard");
    }
}