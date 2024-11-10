package uk.ac.soton.app.model.ProcessedData;

import uk.ac.soton.app.model.CSVData.CSVData;
import uk.ac.soton.app.model.CSVDataUnits.Click;
import uk.ac.soton.app.model.CSVDataUnits.Impression;
import uk.ac.soton.app.model.CSVDataUnits.Interaction;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class used to calculate a set of metrics
 * Using data extracted from an impression log csv file, a click log csv file and a server log csv file
 */
public class Metrics {

    //Number of times an ad was shown to a user
    private int impressions;
    //Number of times a user clicked on an ad shown to them
    private int clicks;
    //Number of unique users that clicked on an ad shown to them
    private int uniques;
    //Number of users that clicked on an ad shown to them and failed to interact with the website
    private int bounces;
    //Number of users that clicked on an ad shown to them and acted on the ad
    private int conversions;
    //Total cost of impressions and clicks
    private double totalCost;
    //Number of clicks per impression
    private float CTR;
    //Cost per acquisition
    private float CPA;
    //Cost per click
    private float CPC;
    //Cost per a thousand impressions
    private float CPM;
    //Number of bounces per click
    private float bounceRate;

    /**
     * Creates an object containing the set of metrics calculated using the data extracted
     * @param csvData data extracted from an impression log, a click log and a server log
     * @param pagesViewed minimum number of pages viewed for a successful interaction
     * @param timeSpent minimum amount of time for a successful interaction
     */
    public Metrics(CSVData csvData, int pagesViewed, ModelDate timeSpent) {
        //Calculates and stores the metrics
        calculateUniquesAndTotalCost(csvData.getImpressions(), csvData.getClicks());
        calculateBouncesAndConversions(csvData.getInteractions(), pagesViewed, timeSpent);
        calculateRemainingMetrics();
    }

    /**
     * Creates an object containing the set of metrics calculated using the data extracted
     * @param impressions data extracted from an impression log
     * @param clicks data extracted from a click log
     * @param interactions data extracted from a server log
     * @param pagesViewed minimum number of pages viewed for a successful interaction
     * @param timeSpent minimum amount of time for a successful interaction
     */
    public Metrics(List<Impression> impressions, List<Click> clicks, List<Interaction> interactions, int pagesViewed, ModelDate timeSpent) {
        //Calculates and stores the metrics
        calculateUniquesAndTotalCost(impressions, clicks);
        calculateBouncesAndConversions(interactions, pagesViewed, timeSpent);
        calculateRemainingMetrics();
    }

    /**
     * Calculates and stores the number of impressions, clicks, uniques and the total cost
     * @param impressions data extracted from an impression log
     * @param clicks data extracted from a click log
     */
    private void calculateUniquesAndTotalCost(List<Impression> impressions, List<Click> clicks) {
        //Sets the number of impressions, clicks and the total cost to 0
        this.impressions = 0;
        this.clicks = 0;
        this.totalCost = 0;

        //Creates a set to store the user IDs of the clicks
        Set<Long> uniqueIDs = new HashSet<>();

        //Loops through each impression stored in the extracted data
        //To get the number of impressions and the total cost of the impressions
        for(Impression impression : impressions) {
            //Checks if the impression has been filtered in or out, true if in and false if out
            if (impression.getVisible()) {
                this.impressions++;
                this.totalCost += impression.getCost();
            }
        }

        //Loops through each click stored in the extracted data
        //To get the number of clicks, the user ID of each click and the total cost of the clicks
        for(Click click : clicks) {
            //Checks if the click has been filtered in or out, true if in and false if out
            if (click.getVisible()) {
                this.clicks++;
                this.totalCost += click.getCost();
                uniqueIDs.add(click.getID());
            }
        }

        //Sets the number of uniques to the size of the set of the user IDs of the clicks
        this.uniques = uniqueIDs.size();

        //Converts the total cost from pence to pounds
        this.totalCost /= 100;
    }

    /**
     * Calculates and stores the number of bounces and conversions
     * @param interactions data extracted from a server log
     * @param pagesViewed minimum number of pages viewed for a successful interaction
     * @param timeSpent minimum amount of time for a successful interaction
     */
    private void calculateBouncesAndConversions(List<Interaction> interactions, int pagesViewed, ModelDate timeSpent) {
        //Sets the number of bounces and conversions to 0
        this.bounces = 0;
        this.conversions = 0;

        //Loops through each interaction stored in the extracted data
        //To check if each interaction was a bounce and if there was a conversion for that interaction
        for(Interaction interaction : interactions) {
            //Checks if the interaction has been filtered in or out, true if in and false if out
            if (interaction.getVisible()) {
                ModelDate entryDate = interaction.getEntryDate();
                ModelDate exitDate = interaction.getExitDate();
                if ((interaction.getPagesViewed() < pagesViewed) || (checkIfBounce(entryDate, exitDate, timeSpent))) bounces++;
                if(interaction.getConversion()) this.conversions++;
            }
        }
    }

    /**
     * Calculates and stores the CTR, the CPA, the CPC, the CPM and the bounce rate
     */
    private void calculateRemainingMetrics() {
        if (this.impressions == 0) {this.CTR = 0;} else {this.CTR = (float) this.clicks / this.impressions;}
        if (this.conversions == 0) {this.CPA = 0;} else {this.CPA = (float) (this.totalCost / this.conversions);}
        if (this.clicks == 0){this.CPC = 0;} else {this.CPC = (float) (this.totalCost / this.clicks);}
        if (this.impressions < 1000) {this.CPM = 0;} else {this.CPM = (float) (this.totalCost / (this.impressions / 1000));}
        if (this.clicks == 0){this.bounceRate = 0;} else {this.bounceRate = (float) this.bounces / this.clicks;}
    }

    /**
     * Checks if the time spent on the website was a bounce
     * @param entryDate date & time of arriving at the website
     * @param exitDate date & time of navigating away from the website
     * @param timeSpent minimum amount of time for a successful interaction
     * @return whether the time spent on the website was a bounce or not
     */
    public boolean checkIfBounce(ModelDate entryDate, ModelDate exitDate, ModelDate timeSpent) {
        //If no exit date available, return false if there is also no minimum amount of time for a successful interaction and true if there is
        if (exitDate == null) return !timeSpent.isEqualTo(new ModelDate(0, 0, 0, 0, 0, 0));

        //If interaction was a bounce, return true
        ModelDate calculatedTimeSpent = exitDate.subtractDates(entryDate);
        return calculatedTimeSpent.isLessThan(timeSpent);
    }

    /**
     * Gets the number of times an ad was shown to a user
     * @return impressions
     */
    public int getImpressions() {
        return this.impressions;
    }

    /**
     * Gets the number of times a user clicked on an ad shown to them
     * @return clicks
     */
    public int getClicks() {
        return this.clicks;
    }

    /**
     * Gets the number of unique users that clicked on an ad shown to them
     * @return uniques
     */
    public int getUniques() {
        return this.uniques;
    }

    /**
     * Gets the number of users that clicked on an ad shown to them and failed to interact with the website
     * @return bounces
     */
    public int getBounces() {
        return this.bounces;
    }

    /**
     * Gets the number of users that clicked on an ad shown to them and acted on the ad
     * @return conversions
     */
    public int getConversions() {
        return this.conversions;
    }

    /**
     * Gets the total cost of impressions and clicks
     * @return total cost
     */
    public double getTotalCost() {
        return this.totalCost;
    }

    /**
     * Gets the number of clicks per impression
     * @return CTR
     */
    public float getCTR() {
        return this.CTR;
    }

    /**
     * Gets the cost per acquisition
     * @return CPA
     */
    public float getCPA() {
        return this.CPA;
    }

    /**
     * Gets the cost per click
     * @return CPC
     */
    public float getCPC() {
        return this.CPC;
    }

    /**
     * Gets the cost per a thousand impressions
     * @return CPM
     */
    public float getCPM() {
        return this.CPM;
    }

    /**
     * Gets the number of bounces per click
     * @return bounce rate
     */
    public float getBounceRate() {
        return this.bounceRate;
    }
}

