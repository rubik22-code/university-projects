package uk.ac.soton.app.exceptions;

/**
 * Class used to represent an exception
 * Where this exception is thrown when a user tries to create a user that already exists
 */
public class UserAlreadyExists extends ApplicationException {

    /**
     * Creates an UserAlreadyExists Exception
     */
    public UserAlreadyExists() {
        super("User already exists.");
    }
}
