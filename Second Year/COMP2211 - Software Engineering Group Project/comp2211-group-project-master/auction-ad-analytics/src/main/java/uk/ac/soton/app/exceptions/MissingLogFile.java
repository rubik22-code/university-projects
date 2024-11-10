package uk.ac.soton.app.exceptions;

public class MissingLogFile extends ApplicationException
{
    public MissingLogFile(String fileType)
    {
        super(String.format("No %s.csv found in selected directory!", fileType));
    }
}
