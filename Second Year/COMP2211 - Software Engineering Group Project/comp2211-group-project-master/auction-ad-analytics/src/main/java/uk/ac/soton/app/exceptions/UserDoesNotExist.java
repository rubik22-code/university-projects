package uk.ac.soton.app.exceptions;

/**
 * Class used to represent an exception
 * Where this exception is thrown when a user tries to delete a user that doesn't exist
 */
public class UserDoesNotExist extends ApplicationException {

    /**
     * Creates an UserDoesNotExist Exception
     */
    public UserDoesNotExist() {
        super("User doesn't exist.");
    }
}
