package uk.ac.soton.app.exceptions;

/**
 * Class used to represent an exception
 * Where this exception is thrown when a line in a csv log file is in an invalid format
 */
public class InvalidLogLine extends ApplicationException {

    /**
     * Creates a CSVLineInvalidException
     * @param csvFile csv log file that has a line in an invalid format
     * @param lineNumber number of the line in an invalid format
     */
    public InvalidLogLine(String csvFile, int lineNumber) {
        super("Line " + lineNumber + ", in the " + csvFile + " CSV log file, is in an invalid format.");
    }
}
