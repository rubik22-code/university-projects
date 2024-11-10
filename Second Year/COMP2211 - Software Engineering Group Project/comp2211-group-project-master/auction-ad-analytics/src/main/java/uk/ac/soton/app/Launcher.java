package uk.ac.soton.app;

/**
 * Launcher class used to avoid dependencies when running .jar file
 * -    Allows application class to be run as an executable .jar file for submission
 */
public class Launcher {
    public static void main(String[] args)
    {
        Application.main(args);
    }
}
