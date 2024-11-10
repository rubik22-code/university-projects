package uk.ac.soton.app.model;

import uk.ac.soton.app.enums.Privilege;
import uk.ac.soton.app.enums.Style;
import uk.ac.soton.app.exceptions.CampaignAlreadyExists;
import uk.ac.soton.app.exceptions.CampaignNotRegistered;
import uk.ac.soton.app.model.DataBase.DatabaseManager;
import uk.ac.soton.app.model.DataBase.User;

import java.util.HashMap;

/**
 * Global, singleton state object that is shared between all scenes
 * -    Holds information for the logged-in user
 * -    Holds a list of all campaigns that have been uploaded to the system by the user
 */
public class State
{
    private Style style = Style.DEFAULT;
    private User user;
    private HashMap<String, Campaign> campaigns = new HashMap<>();
    private DatabaseManager databaseManager;

    /**
     * Sets the global DBM
     * @param databaseManager the DBM object
     */
    public void setDatabaseManager(DatabaseManager databaseManager)
    {
        this.databaseManager = databaseManager;
    }

    /**
     * Fetches the global DBM
     * @return the DBM object
     */
    public DatabaseManager getDatabaseManager()
    {
        return ( databaseManager );
    }

    /**
     * Registers the logged-in user to the state
     * @param username the username of the user
     * @param accessLevel the privilege level of the user
     */
    public void setUser(String username, Privilege accessLevel)
    {
        user = new User(username, accessLevel);
    }

    /**
     * Getter function for the logged-in user
     * @return the user object
     */
    public User getUser()
    {
        return ( user );
    }

    /**
     * Gets the number of campaigns uploaded by the user
     * @return number of campaigns in the system
     */
    public int getCampaignCount()
    {
        return ( campaigns.size() );
    }

    /**
     * Gets a campaign from the campaign list using its name
     * @param campaignName the name of the campaign
     * @return the associated campaign object
     * @throws CampaignNotRegistered specified campaign isn't registered to the system state
     */
    public Campaign getCampaign(String campaignName) throws CampaignNotRegistered
    {
        if ( !campaigns.containsKey(campaignName) ) throw new CampaignNotRegistered();
        return ( campaigns.get(campaignName) );
    }

    public HashMap<String, Campaign> getCampaigns() { return ( campaigns ); }

    /**
     * Registers a campaign to the system state so that it can be accessed when needed
     * @param campaign campaign to be stored
     * @throws CampaignAlreadyExists campaign with same name found in the state
     */
    public void registerCampaign(Campaign campaign) throws CampaignAlreadyExists
    {
        if ( campaigns.containsKey(campaign.getCampaignName()) ) throw new CampaignAlreadyExists();
        campaigns.put(campaign.getCampaignName(), campaign);
    }

    /**
     * Resets the list of campaigns
     */
    public void clearCampaigns() {
        campaigns.clear();
    }

    /**
     * Gets the current theme in use by the application
     * @return the name of the stylesheet
     */
    public Style getStyle()
    {
        return ( style );
    }
}
