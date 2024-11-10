package uk.ac.soton.app.model.CSVData;

import uk.ac.soton.app.exceptions.InvalidLogFile;
import uk.ac.soton.app.exceptions.InvalidLogLine;
import uk.ac.soton.app.model.CSVDataUnits.Impression;
import uk.ac.soton.app.model.CSVDataUnits.Interaction;
import uk.ac.soton.app.model.CSVDataUnits.Click;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class used to extract data from an impression log csv file, a click log csv file and a server log csv file
 */
public class CSVData {

    //List of impressions
    private final List<Impression> impressions;
    //List of clicks
    private final List<Click> clicks;
    //List of interactions
    private final List<Interaction> interactions;
    //Date of first data logged
    private ModelDate firstDate;
    //Date of last data logged
    private ModelDate lastDate;
    //Set of the user IDs of the clicks and interactions
    private final Set<Long> userIDs;


    /**
     * Creates an object containing data extracted from an impression log, click log and a server log
     * Where the data extracted from each csv file is stored as a list of its rows
     * @param impressionLog impression log
     * @param clickLog click log
     * @param serverLog server log
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     */
    public CSVData(File impressionLog, File clickLog, File serverLog) throws InvalidLogFile, InvalidLogLine {
        //Creates 3 lists and a set to store the data extracted from each CSV file
        this.impressions = new ArrayList<>();
        this.clicks = new ArrayList<>();
        this.interactions = new ArrayList<>();
        this.userIDs = new HashSet<>();

        //Extracts data from each CSV file and stores it as a list of its rows
        readImpressionLog(impressionLog);
        readClickLog(clickLog);
        readServerLog(serverLog);

        //Finds and stores the dates of the first and last data logged
        storeFirstDate();
        storeLastDate();
    }

    /**
     * Extracts data from an impression log into a list of impressions
     * @param file impression log
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     */
    private void readImpressionLog(File file) throws InvalidLogFile, InvalidLogLine {
        //Reads through the impression log and saves each valid row as an object called impression
        //Where each value in the row is a property of that impression
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            String line = reader.readLine();
            int lineNumber = 2;
            while (line != null) {
                //If the row is in a valid format and contains no value in an invalid format
                //Saves it as an impression within the list of impressions
                //Otherwise, throws an InvalidLogFile exception
                String[] array = line.split(",");
                try {
                    if (7 < array.length) throw new InvalidLogLine("Impression", lineNumber);
                    Impression impression = new Impression(array[0], array[1], array[2], array[3], array[4], array[5], array[6]);
                    this.impressions.add(impression);
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {throw new InvalidLogLine("Impression", lineNumber);}
                line = reader.readLine();
                lineNumber++;
            }
            reader.close();
        } catch (IOException e) {throw new InvalidLogFile("Impression");}

        //Sorts the list of impressions
        Collections.sort(this.impressions);
    }

    /**
     * Extracts data from a click log into a list of clicks
     * @param file click log
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     */
    private void readClickLog(File file) throws InvalidLogFile, InvalidLogLine {
        //Reads through the click log and saves each valid row as an object called click
        //Where each value in the row is a property of that click
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            String line = reader.readLine();
            int lineNumber = 2;
            while (line != null) {
                //If the row is in a valid format and contains no value in an invalid format
                //Saves it as a click within the list of clicks
                //Otherwise, throws an InvalidLogFile exception
                String[] array = line.split(",");
                try {
                    if (3 < array.length) throw new InvalidLogLine("Click", lineNumber);
                    Click click = new Click(array[0], array[1], array[2]);
                    this.userIDs.add(click.getID());
                    this.clicks.add(click);
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {throw new InvalidLogLine("Click", lineNumber);}
                line = reader.readLine();
                lineNumber++;
            }
            reader.close();
        } catch (IOException e) {throw new InvalidLogFile("Click");}

        //Sorts the list of clicks
        Collections.sort(this.clicks);
    }

    /**
     * Extracts data from a server log into a list of interactions
     * @param file server log
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     */
    private void readServerLog(File file) throws InvalidLogFile, InvalidLogLine {
        //Reads through the server log and saves each valid row as an object called interaction
        //Where each value in the row is a property of that interaction
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            String line = reader.readLine();
            int lineNumber = 2;
            while (line != null) {
                //If the row is in a valid format and contains no value in an invalid format
                //Saves it as an interaction within the list of interactions
                //Otherwise, throws an InvalidLogFile exception
                String[] array = line.split(",");
                try {
                    if (5 < array.length) throw new InvalidLogLine("Server", lineNumber);
                    Interaction interaction = new Interaction(array[0], array[1], array[2], array[3], array[4]);
                    this.userIDs.add(interaction.getID());
                    this.interactions.add(interaction);
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {throw new InvalidLogLine("Server", lineNumber);}
                line = reader.readLine();
                lineNumber++;
            }
            reader.close();
        } catch (IOException e) {throw new InvalidLogFile("Server");}

        //Sorts the list of interactions
        Collections.sort(this.interactions);
    }

    /**
     * Finds and stores the date of the first data logged
     */
    private void storeFirstDate() {
        //Stores the first impression's date, first click's date and first interaction's date
        List<ModelDate> firstDates = new ArrayList<>();
        if (!this.impressions.isEmpty()) firstDates.add(this.impressions.get(0).getDate());
        if (!this.clicks.isEmpty()) firstDates.add(this.clicks.get(0).getDate());
        if (!this.interactions.isEmpty()) firstDates.add(this.interactions.get(0).getEntryDate());

        //Stores the earliest date out of these dates
        Collections.sort(firstDates);
        if (firstDates.isEmpty()) {this.firstDate = null;}
        else {this.firstDate = firstDates.get(0).createIdenticalDate();}
    }

    /**
     * Finds and stores the date of the last data logged
     */
    private void storeLastDate() {
        //Stores the last impression's date, last click's date and last interaction's date
        List<ModelDate> lastDates = new ArrayList<>();
        if (!this.impressions.isEmpty()) lastDates.add(this.impressions.get(this.impressions.size() - 1).getDate());
        if (!this.clicks.isEmpty()) lastDates.add(this.clicks.get(this.clicks.size() - 1).getDate());
        if (!this.interactions.isEmpty()) lastDates.add(this.interactions.get(this.interactions.size() - 1).getEntryDate());

        //Stores the latest date out of these dates
        Collections.sort(lastDates);
        if (lastDates.isEmpty()) {this.lastDate = null;}
        else {this.lastDate = lastDates.get(lastDates.size() - 1).createIdenticalDate();}
    }

    /**
     * Gets the list of impressions
     * @return list of impressions
     */
    public List<Impression> getImpressions() {
        return this.impressions;
    }

    /**
     * Gets the list of clicks
     * @return list of clicks
     */
    public List<Click> getClicks() {
        return this.clicks;
    }

    /**
     * Gets the list of interactions
     * @return list of interactions
     */
    public List<Interaction> getInteractions() {
        return this.interactions;
    }

    /**
     * Gets the date of the first data logged
     * @return date
     */
    public ModelDate getFirstDate() {
        return this.firstDate;
    }

    /**
     * Gets the date of the last data logged
     * @return date
     */
    public ModelDate getLastDate() {
        return this.lastDate;
    }

    /**
     * Gets the set of the user IDs of the clicks and interactions
     * @return set of user IDs
     */
    public Set<Long> getUserIDs() {
        return this.userIDs;
    }
}
