package uk.ac.soton.app.exceptions;

/**
 * Class used to represent an exception
 * Where this exception is thrown when a csv log file can't be read
 */
public class InvalidLogFile extends ApplicationException {

    /**
     * Creates a CSVFileInvalid Exception
     * @param csvFile csv log file that can't be read
     */
    public InvalidLogFile(String csvFile) {
        super(csvFile + " CSV log file can't be read.");
    }

}
