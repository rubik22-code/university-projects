package uk.ac.soton.app.model.CSVData;

import javafx.util.Pair;
import uk.ac.soton.app.enums.TimeInterval;
import uk.ac.soton.app.model.CSVDataUnits.Click;
import uk.ac.soton.app.model.CSVDataUnits.Impression;
import uk.ac.soton.app.model.CSVDataUnits.Interaction;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.util.*;

/**
 * Class used to filter a list of impressions, a list of clicks and a list of interactions
 * Using date range, gender, age, income and context
 */
public class CSVFilter {

    /**
     * Filters a list of impressions, a list of clicks and a list of interactions using date range, gender, age, income and context
     * @param csvData object containing the list of impressions, the list of clicks and the list of interactions
     * @param dateRange date range containing the dates of impressions, clicks and interactions that we want to keep
     * @param genderFilters genders of impressions that we want to keep
     * @param ageFilters age groups of impressions that we want to keep
     * @param incomeFilters incomes of impressions that we want to keep
     * @param contextFilters contexts of impressions that we want to keep
     */
    public static void filter(CSVData csvData, Pair<ModelDate, ModelDate> dateRange, List<String> genderFilters, List<String> ageFilters, List<String> incomeFilters, List<String> contextFilters) {
        //Set used to store the user IDs of the clicks and interactions that we want to keep based off their gender, age and income
        //Two maps used to map from the userIDs of the clicks and interactions that we want to keep, based off their context, to their dates
        Set<Long> audienceSegmentUserIDs = new HashSet<>();
        Map<Long, List<ModelDate>> contextUserIDs = new HashMap<>();
        Map<Long, List<ModelDate>> contextUserIDsCopy = new HashMap<>();

        //Loops through each impression
        for (Impression impression : csvData.getImpressions()) {
            //Checks if we want to keep the impression based off its date
            boolean dateAboveLowerBound = (dateRange.getKey() == null && dateRange.getValue() == null) || !impression.getDate().isLessThan(dateRange.getKey());
            boolean dateBelowUpperBound = (dateRange.getKey() == null && dateRange.getValue() == null) || !dateRange.getValue().isLessThan(impression.getDate().convertToTimeInterval(TimeInterval.DAY));
            boolean dateMatches = dateAboveLowerBound && dateBelowUpperBound;

            //Checks if we want to keep the impression based off its gender, age and income
            boolean genderMatches = genderFilters.isEmpty() || genderFilters.contains(impression.getGender());
            boolean ageMatches = ageFilters.isEmpty() || ageFilters.contains(impression.getAge());
            boolean incomeMatches = incomeFilters.isEmpty() || incomeFilters.contains(impression.getIncome());
            boolean audienceSegmentsMatch = genderMatches && ageMatches && incomeMatches;
            if (audienceSegmentsMatch && csvData.getUserIDs().contains(impression.getID())) audienceSegmentUserIDs.add(impression.getID());

            //Checks if we want to keep the impression based off its context
            boolean contextMatches = contextFilters.isEmpty() || contextFilters.contains(impression.getContext());
            if (contextMatches && csvData.getUserIDs().contains(impression.getID()) && !contextUserIDs.containsKey(impression.getID())) {
                contextUserIDs.put(impression.getID(), new ArrayList<>());
                contextUserIDsCopy.put(impression.getID(), new ArrayList<>());
            }
            if (contextMatches && csvData.getUserIDs().contains(impression.getID())) {
                contextUserIDs.get(impression.getID()).add(impression.getDate());
                contextUserIDsCopy.get(impression.getID()).add(impression.getDate());
            }

            //If each of these is true then we keep the impression, otherwise we filter it out
            impression.setVisible(dateMatches && audienceSegmentsMatch && contextMatches);
        }

        //Filters clicks and interactions using the date range and the user IDs of the clicks and interactions that we want to keep
        filterRespectiveClicks(csvData, dateRange, audienceSegmentUserIDs, contextUserIDs);
        filterRespectiveInteractions(csvData, dateRange, audienceSegmentUserIDs, contextUserIDsCopy);
    }

    /**
     * Filters a list of clicks using a date range and the user IDs of the clicks that we want to keep
     * @param csvData object containing the list of clicks
     * @param dateRange date range containing the dates of the clicks that we want to keep
     * @param audienceSegmentUserIDs set of the user IDs of the clicks that we want to keep based off their gender, age and income
     * @param contextUserIDs map from the user IDs of the clicks that we want to keep based off their context to their dates
     */
    private static void filterRespectiveClicks(CSVData csvData, Pair<ModelDate, ModelDate> dateRange, Set<Long> audienceSegmentUserIDs, Map<Long, List<ModelDate>> contextUserIDs) {
        //Loops through each click
        for (Click click : csvData.getClicks()) {
            //Checks if we want to keep the click based off its date
            boolean dateAboveLowerBound = (dateRange.getKey() == null && dateRange.getValue() == null) || !click.getDate().isLessThan(dateRange.getKey());
            boolean dateBelowUpperBound = (dateRange.getKey() == null && dateRange.getValue() == null) || !dateRange.getValue().isLessThan(click.getDate().convertToTimeInterval(TimeInterval.DAY));
            boolean dateMatches = dateAboveLowerBound && dateBelowUpperBound;

            //Checks if we want to keep the click based off its gender, age and income
            boolean audienceSegmentsMatch = audienceSegmentUserIDs.contains(click.getID());

            //Checks if we want to keep the click based off its context
            boolean contextMatches = false;
            if (contextUserIDs.containsKey(click.getID()) && !contextUserIDs.get(click.getID()).isEmpty()) {
                List<ModelDate> dates = contextUserIDs.get(click.getID());
                ModelDate nextDate = null;
                if (dates.size() > 1) {nextDate = dates.get(1);}
                for (ModelDate date : dates) {
                    boolean greaterThanImpression = !click.getDate().isLessThan(date);
                    boolean lessThanNextImpression = nextDate != null && click.getDate().isLessThan(nextDate);
                    boolean secondLastDate = dates.size() == 2 || dates.indexOf(date) == (dates.size() - 2);
                    boolean lastDate = nextDate == null || dates.indexOf(date) == (dates.size() - 1);
                    if (greaterThanImpression && (lessThanNextImpression || lastDate)) {
                        contextUserIDs.get(click.getID()).remove(date);
                        contextMatches = true;
                        break;
                    } else if (!lastDate && !secondLastDate) {nextDate = dates.get(dates.indexOf(date) + 2);}}
            }

            //If each of these is true then we keep the click, otherwise we filter it out
            boolean filterClickIn = dateMatches && audienceSegmentsMatch && contextMatches;
            click.setVisible(filterClickIn);
        }
    }

    /**
     * Filters a list of interactions using a date range and the user IDs of the interactions that we want to keep
     * @param csvData object containing the list of interactions
     * @param dateRange date range containing the dates of the interactions that we want to keep
     * @param audienceSegmentUserIDs set of the user IDs of the interactions that we want to keep based off their gender, age and income
     * @param contextUserIDs map from the user IDs of the interactions that we want to keep based off their context to their dates
     */
    private static void filterRespectiveInteractions(CSVData csvData, Pair<ModelDate, ModelDate> dateRange,  Set<Long> audienceSegmentUserIDs, Map<Long, List<ModelDate>> contextUserIDs) {
        //Loops through each interaction
        for (Interaction interaction : csvData.getInteractions()) {
            //Checks if we want to keep the interaction based off its date
            boolean dateAboveLowerBound = (dateRange.getKey() == null && dateRange.getValue() == null) || !interaction.getEntryDate().isLessThan(dateRange.getKey());
            boolean dateBelowUpperBound = (dateRange.getKey() == null && dateRange.getValue() == null) || !dateRange.getValue().isLessThan(interaction.getEntryDate().convertToTimeInterval(TimeInterval.DAY));
            boolean dateMatches = dateAboveLowerBound && dateBelowUpperBound;

            //Checks if we want to keep the interaction based off its gender, age and income
            boolean IDMatches = audienceSegmentUserIDs.contains(interaction.getID());

            //Checks if we want to keep the interaction based off its context
            boolean contextMatches = false;
            if (contextUserIDs.containsKey(interaction.getID()) && !contextUserIDs.get(interaction.getID()).isEmpty()) {
                List<ModelDate> dates = contextUserIDs.get(interaction.getID());
                ModelDate nextDate = null;
                if (dates.size() > 1) {nextDate = dates.get(1);}
                for (ModelDate date : dates) {
                    boolean greaterThanImpression = !interaction.getEntryDate().isLessThan(date);
                    boolean lessThanNextImpression = nextDate != null && interaction.getEntryDate().isLessThan( nextDate);
                    boolean secondLastDate = dates.size() == 2 || dates.indexOf(date) == (dates.size() - 2);
                    boolean lastDate = nextDate == null || dates.indexOf(date) == (dates.size() - 1);
                    if (greaterThanImpression && (lessThanNextImpression || lastDate)) {
                        contextUserIDs.get(interaction.getID()).remove(date);
                        contextMatches = true;
                        break;
                    } else if (!lastDate && !secondLastDate) {nextDate = dates.get(dates.indexOf(date) + 2);}
                }
            }

            //If each of these is true then we keep the interaction, otherwise we filter it out
            boolean filterInteractionIn = dateMatches && IDMatches && contextMatches;
            interaction.setVisible(filterInteractionIn);
        }
    }

    /**
     * Resets the filters on a list of impressions, a list of clicks and a list of interactions
     * @param csvData object containing the lists
     */
    public static void clearFilters(CSVData csvData) {
        //Resets the filters on the impressions
        for (Impression impression : csvData.getImpressions()) {
            impression.setVisible(true);
        }

        //Resets the filters on the clicks
        for (Click click : csvData.getClicks()) {
            click.setVisible(true);
        }

        //Resets the filters on the interactions
        for (Interaction interaction : csvData.getInteractions()) {
            interaction.setVisible(true);
        }
    }
}
