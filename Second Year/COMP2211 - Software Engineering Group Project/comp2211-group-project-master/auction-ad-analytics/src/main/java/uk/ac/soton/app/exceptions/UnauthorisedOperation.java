package uk.ac.soton.app.exceptions;

/**
 * Class used to represent an exception
 * Where this exception is thrown when a user tries to create or delete a user that they aren't allowed to create or delete
 */
public class UnauthorisedOperation extends ApplicationException {

    /**
     * Creates an UnauthorisedOperation Exception
     */
    public UnauthorisedOperation() {
        super("Unauthorised operation.");
    }
}
