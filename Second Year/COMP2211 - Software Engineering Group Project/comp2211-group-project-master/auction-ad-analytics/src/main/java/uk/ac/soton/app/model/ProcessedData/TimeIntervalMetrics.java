package uk.ac.soton.app.model.ProcessedData;

import uk.ac.soton.app.enums.TimeInterval;
import uk.ac.soton.app.model.CSVData.CSVData;
import uk.ac.soton.app.model.CSVDataUnits.Click;
import uk.ac.soton.app.model.CSVDataUnits.Impression;
import uk.ac.soton.app.model.CSVDataUnits.Interaction;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.util.*;

/**
 * Class used to calculate sets of metrics
 * Where each set is calculated over a time interval
 */
public class TimeIntervalMetrics {

    //List of each time interval's lower bound
    private final List<ModelDate> timeIntervals;

    //List of each time interval's set of metrics
    private final List<Metrics> metrics;

    /**
     * Creates an object containing a list of sets of metrics
     * Where each set of metrics is calculated over a time interval
     * @param csvData object containing the data needed to calculate each set of metrics
     * @param timeIntervalLength each time interval's length
     * @param pagesViewed minimum number of pages viewed for a successful interaction
     * @param timeSpent minimum amount of time for a successful interaction
     */
    public TimeIntervalMetrics(CSVData csvData, TimeInterval timeIntervalLength, int pagesViewed, ModelDate timeSpent) {
        //Stores dates of the first and last data logged
        ModelDate firstDate = getFirstDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        ModelDate lastDate = getLastDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());

        //Creates 2 lists to store each time interval's lower bound and each time interval's set of metrics
        this.timeIntervals = new ArrayList<>();
        this.metrics = new ArrayList<>();

        //If there are no impressions, clicks and interactions, calculate no sets of metrics
        if (firstDate == null) return;

        //Calculates and stores each time interval's lower bound
        calculateTimeIntervals(firstDate, lastDate, timeIntervalLength);

        //Stores each time interval's lower bound, in the form of a long, in a map
        //Where this map is used to map each time interval's lower bound, in the form of a long, to their chronological position
        Map<Long, Integer> timeIntervalToIndex = new HashMap<>();
        int position = 0;
        for (ModelDate givenTimeInterval : this.timeIntervals) {
            timeIntervalToIndex.put(givenTimeInterval.asLong(), position);
            position++;
        }

        //Creates a list of lists of impressions, a list of lists of clicks and a list of lists of interactions
        //Where the impressions, clicks and interactions in each list happen during the same time interval
        List<List<Impression>> impressionsByTimeInterval = new ArrayList<>();
        List<List<Click>> clicksByTimeInterval = new ArrayList<>();
        List<List<Interaction>> interactionsByTimeInterval = new ArrayList<>();

        //Fills each of the 3 lists of lists with as many lists as there are time intervals
        for (int i = 0; i < this.timeIntervals.size(); i++) {
            impressionsByTimeInterval.add(new ArrayList<>());
            clicksByTimeInterval.add(new ArrayList<>());
            interactionsByTimeInterval.add(new ArrayList<>());
        }

        //Splits lists of impressions, clicks and interactions into the correct lists within the lists of lists
        splitUpImpressions(impressionsByTimeInterval, csvData.getImpressions(), timeIntervalToIndex, timeIntervalLength);
        splitUpClicks(clicksByTimeInterval, csvData.getClicks(), timeIntervalToIndex, timeIntervalLength);
        splitUpInteractions(interactionsByTimeInterval,  csvData.getInteractions(), timeIntervalToIndex, timeIntervalLength);

        //Calculates sets of metrics using the lists of lists and stores them
        //Each set of metrics is calculated using a list of impressions, a list of clicks and a list of interactions that happen during the same time interval
        calculateSetsOfMetrics(impressionsByTimeInterval, clicksByTimeInterval, interactionsByTimeInterval, pagesViewed, timeSpent);
    }

    /**
     * Gets the date of the first data logged that is filtered in
     * @param impressions list of impressions
     * @param clicks list of clicks
     * @param interactions list of interactions
     * @return date
     */
    public ModelDate getFirstDate(List<Impression> impressions, List<Click> clicks, List<Interaction> interactions) {
        //List used to store the first filtered in impression's date, first filtered in click's date and first filtered in interaction's date
        List<ModelDate> firstDates = new ArrayList<>();

        //Loops through each impression to get the date of the first filtered in impression
        for (Impression impression : impressions) {
            if (impression.getVisible()) {
                firstDates.add(impression.getDate());
                break;
            }
        }

        //Loops through each click to get the date of the first filtered in click
        for (Click click : clicks) {
            if (click.getVisible()) {
                firstDates.add(click.getDate());
                break;
            }
        }

        //Loops through each interaction to get the date of the first filtered in interaction
        for (Interaction interaction : interactions) {
            if (interaction.getVisible()) {
                firstDates.add(interaction.getEntryDate());
                break;
            }
        }

        //Returns the earliest date out of these dates
        Collections.sort(firstDates);
        if (firstDates.isEmpty()) {return null;}
        else {return firstDates.get(0).createIdenticalDate();}
    }

    /**
     * Gets the date of the last data logged that is filtered in
     * @param impressions list of impressions
     * @param clicks list of clicks
     * @param interactions list of interactions
     * @return date
     */
    public ModelDate getLastDate(List<Impression> impressions, List<Click> clicks, List<Interaction> interactions) {
        //List used to store the last filtered in impression's date, last filtered in click's date and last filtered in interaction's date
        List<ModelDate> lastDates = new ArrayList<>();

        //Loops through each impression to get the date of the last filtered in impression
        for (int i = impressions.size() - 1; i >= 0; i--) {
            if (impressions.get(i).getVisible()) {
                lastDates.add(impressions.get(i).getDate());
                break;
            }
        }

        //Loops through each click to get the date of the last filtered in click
        for (int i = clicks.size() - 1; i >= 0; i--) {
            if (clicks.get(i).getVisible()) {
                lastDates.add(clicks.get(i).getDate());
                break;
            }
        }

        //Loops through each interaction to get the date of the last filtered in interaction
        for (int i = interactions.size() - 1; i >= 0; i--) {
            if (interactions.get(i).getVisible()) {
                lastDates.add(interactions.get(i).getEntryDate());
                break;
            }
        }

        //Returns the latest date out of these dates
        Collections.sort(lastDates);
        if (lastDates.isEmpty()) {return null;}
        else {return lastDates.get(lastDates.size() - 1).createIdenticalDate();}
    }

    /**
     * Calculates each time interval's lower bound
     * @param firstDate date of first data logged
     * @param lastDate date of last data logged
     * @param timeIntervalLength each time interval's length
     */
    private void calculateTimeIntervals(ModelDate firstDate, ModelDate lastDate, TimeInterval timeIntervalLength) {
        //Date representing the length of each time interval
        //Used to increment a lower bound to the next lower bound
        ModelDate timeIntervalIncrementer;
        if (timeIntervalLength == TimeInterval.HOUR) {
            timeIntervalIncrementer = new ModelDate(0, 0, 0, 1, 0, 0);
        } else if (timeIntervalLength == TimeInterval.DAY) {
            timeIntervalIncrementer = new ModelDate(0, 0, 1, 0, 0, 0);
        } else if (timeIntervalLength == TimeInterval.WEEK) {
            timeIntervalIncrementer = new ModelDate(0, 0, 7, 0, 0, 0);
        } else {
            timeIntervalIncrementer = new ModelDate(0, 1, 0, 0, 0, 0);
        }

        //Loops until we've created each time interval's lower bound
        ModelDate currentLowerBound = firstDate.convertToTimeInterval(timeIntervalLength);
        while (!lastDate.convertToTimeInterval(timeIntervalLength).isLessThan(currentLowerBound)) {
            //Stores the current lower bound and increments the current lower bound
            this.timeIntervals.add(currentLowerBound.convertToTimeInterval(timeIntervalLength));
            currentLowerBound = currentLowerBound.addDates(timeIntervalIncrementer);
        }
    }

    /**
     * Gets the lower bound of the time interval, of length week, that a date happens during
     * @param date date
     * @return time interval's lower bound
     */
    private ModelDate getWeekTimeInterval(ModelDate date) {
        //Previous time interval's lower bound
        ModelDate previousTimeInterval = this.timeIntervals.get(0);

        //Loops through each time interval's lower bound
        for (ModelDate timeInterval : this.timeIntervals) {
            //Checks if the time interval's lower bound is greater than the date
            //If true, returns the previous time interval's lower bound
            if (date.isLessThan(timeInterval)) {
                return previousTimeInterval;
            } else {
                previousTimeInterval = timeInterval;
            }
        }

        //Returns the last time interval's lower bound, if no time interval's lower bound is greater than the date
        return previousTimeInterval;
    }


    /**
     * Splits a list of impressions into multiple lists
     * Where the impressions in each list happen during the same time interval
     * @param impressionsByTimeInterval list of lists that we are splitting a list of impressions into
     * @param impressions list of impressions
     * @param timeIntervalToIndex map from each time interval's lower bound to the index of the list of impressions, in the list of lists, that happen during its time interval
     * @param timeIntervalLength each time interval's length
     */
    private void splitUpImpressions(List<List<Impression>> impressionsByTimeInterval, List<Impression> impressions, Map<Long, Integer> timeIntervalToIndex, TimeInterval timeIntervalLength) {
        //Loops through each impression
        for(Impression impression : impressions) {
            //Checks if the impression has been filtered in or out, true if in and false if out
            if (impression.getVisible()) {
                //Uses the time interval length and the date of the impression to create the lower bound of the time interval that it happens during
                //For example, if the given time interval length is "HOUR" and the date of the impression is "2015-01-01 12:20:15" then create the lower bound "2015-01-01 12:00:00"
                long impressionTimeInterval;
                if (timeIntervalLength == TimeInterval.WEEK) {
                    impressionTimeInterval = getWeekTimeInterval(impression.getDate()).asLong();
                } else {
                    impressionTimeInterval = impression.getDate().convertToTimeInterval(timeIntervalLength).asLong();
                }
                //Adds the impression to the list in the list of lists of impressions that stores impressions that happen during its time interval
                impressionsByTimeInterval.get(timeIntervalToIndex.get(impressionTimeInterval)).add(impression);
            }
        }
    }

    /**
     * Splits a list of clicks into multiple lists
     * Where the clicks in each list happen during the same time interval
     * @param clicksByTimeInterval list of lists we are splitting a list of clicks into
     * @param clicks list of clicks
     * @param timeIntervalToIndex map from each time interval's lower bound to the index of the list of clicks, in the list of lists, that happen during its time interval
     * @param timeIntervalLength each time interval's length
     */
    private void splitUpClicks(List<List<Click>> clicksByTimeInterval, List<Click> clicks, Map<Long, Integer> timeIntervalToIndex, TimeInterval timeIntervalLength) {
        //Loops through each click
        for(Click click : clicks) {
            //Checks if the click has been filtered in or out, true if in and false if out
            if (click.getVisible()) {
                //Uses the time interval length and the date of the click to create the lower bound of the time interval that it happens during
                //For example, if the given time interval length is "HOUR" and the date of the click is "2015-01-01 12:20:15" then create the lower bound "2015-01-01 12:00:00"
                long clickTimeInterval;
                if (timeIntervalLength == TimeInterval.WEEK) {
                    clickTimeInterval = getWeekTimeInterval(click.getDate()).asLong();
                } else {
                    clickTimeInterval = click.getDate().convertToTimeInterval(timeIntervalLength).asLong();
                }
                //Adds the click to the list in the list of lists of clicks that stores clicks that happen during its time interval
                clicksByTimeInterval.get(timeIntervalToIndex.get(clickTimeInterval)).add(click);
            }
        }
    }

    /**
     * Splits a list of interactions into multiple lists
     * Where the interactions in each list happen during the same time interval
     * @param interactionsByTimeInterval list of lists we are splitting a list of interactions into
     * @param interactions list of interactions
     * @param timeIntervalToIndex map from each time interval's lower bound to the index of the list of interactions, in the list of lists, that happen during its time interval
     * @param timeIntervalLength each time interval's length
     */
    private void splitUpInteractions(List<List<Interaction>> interactionsByTimeInterval, List<Interaction> interactions, Map<Long, Integer> timeIntervalToIndex, TimeInterval timeIntervalLength) {
        //Loops through each interaction
        for(Interaction interaction : interactions) {
            //Checks if the interaction has been filtered in or out, true if in and false if out
            if (interaction.getVisible()) {
                //Uses the time interval length and the date of the interaction to create the lower bound of the time interval that it happens during
                //For example, if the given time interval length is "HOUR" and the date of the interaction is "2015-01-01 12:20:15" then create the lower bound "2015-01-01 12:00:00"
                long interactionTimeInterval;
                if (timeIntervalLength == TimeInterval.WEEK) {
                    interactionTimeInterval = getWeekTimeInterval(interaction.getEntryDate()).asLong();
                } else {
                    interactionTimeInterval = interaction.getEntryDate().convertToTimeInterval(timeIntervalLength).asLong();
                }
                //Adds the interaction to the list in the list of lists of interactions that stores interactions that happen during its time interval
                interactionsByTimeInterval.get(timeIntervalToIndex.get(interactionTimeInterval)).add(interaction);
            }
        }
    }

    /**
     * Calculates the sets of metrics
     * Where each set of metrics is calculated using a list of impressions, a list of clicks and a list of interactions that happen during the same time interval
     * @param impressionsByTimeInterval list of lists of impressions, where the impressions in each list happen during the same time interval
     * @param clicksByTimeInterval list of lists of clicks, where the clicks in each list happen during the same time interval
     * @param interactionsByTimeInterval list of lists of interactions, where the interactions in each list happen during the same time interval
     * @param pagesViewed minimum number of pages viewed for a successful interaction
     * @param timeSpent minimum number amount of time for a successful interaction
     */
    private void calculateSetsOfMetrics(List<List<Impression>> impressionsByTimeInterval, List<List<Click>> clicksByTimeInterval, List<List<Interaction>> interactionsByTimeInterval, int pagesViewed, ModelDate timeSpent ) {
        //Loops through each list of impressions, where the impressions in each list happen during the same time interval
        for (List<Impression> impressions : impressionsByTimeInterval) {
            //Gets the list of clicks and the list of interactions that happen during the same time interval as this list of impressions
            List<Click> clicksToGive = clicksByTimeInterval.get(impressionsByTimeInterval.indexOf(impressions));
            List<Interaction> interactionsToGive = interactionsByTimeInterval.get(impressionsByTimeInterval.indexOf(impressions));

            //Calculates a set of metrics using these three lists and stores them
            this.metrics.add(new Metrics(impressions, clicksToGive, interactionsToGive, pagesViewed, timeSpent));
        }
    }

    /**
     * Gets the list of time intervals
     * @return list of time intervals
     */
    public List<ModelDate> getTimeIntervals() {
        return this.timeIntervals;
    }

    /**
     * Gets the list of sets of metrics
     * @return list of sets of metrics
     */
    public List<Metrics> getMetrics() {
        return this.metrics;
    }

}

