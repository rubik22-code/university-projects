package uk.ac.soton.app.exceptions;

public class ExportFailure extends ApplicationException
{
    public ExportFailure()
    {
        super("Failed to export campaign data!");
    }
}
