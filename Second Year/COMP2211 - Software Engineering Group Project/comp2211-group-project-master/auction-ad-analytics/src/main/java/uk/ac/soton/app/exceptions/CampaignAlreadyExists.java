package uk.ac.soton.app.exceptions;

public class CampaignAlreadyExists extends ApplicationException
{
    public CampaignAlreadyExists()
    {
        super("Selected campaign is already loaded into application!");
    }
}
