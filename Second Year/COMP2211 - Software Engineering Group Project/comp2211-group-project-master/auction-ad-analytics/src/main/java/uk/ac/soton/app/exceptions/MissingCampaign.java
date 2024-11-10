package uk.ac.soton.app.exceptions;

public class MissingCampaign extends ApplicationException
{
    public MissingCampaign()
    {
        super("No campaign directory was selected!");
    }
}
