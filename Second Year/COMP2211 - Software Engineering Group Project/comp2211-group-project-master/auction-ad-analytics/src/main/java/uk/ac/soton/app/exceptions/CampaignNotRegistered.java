package uk.ac.soton.app.exceptions;

public class CampaignNotRegistered extends ApplicationException
{
    public CampaignNotRegistered()
    {
        super("Selected campaign is not registered to the application!");
    }
}
