package uk.ac.soton.app.exceptions;

public class InvalidDateCombination extends ApplicationException
{
    public InvalidDateCombination()
    {
        super("Selected end date is before the start date!");
    }
}
