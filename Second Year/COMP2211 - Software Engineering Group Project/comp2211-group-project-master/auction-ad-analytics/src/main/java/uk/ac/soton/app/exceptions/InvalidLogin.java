package uk.ac.soton.app.exceptions;

public class InvalidLogin extends ApplicationException {
    public InvalidLogin() {super("Invalid username or password, please retry.");}
}
