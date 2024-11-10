package uk.ac.soton.app.exceptions;

import javafx.scene.Scene;
import javafx.scene.control.Alert;

public class ApplicationException extends Exception {
    public ApplicationException(String string)
    {
        super(string);
    }

    /**
     * Creates a pop-up warning for the associated error
     */
    public void displayError()
    {
        String error = this.getLocalizedMessage();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }
}
