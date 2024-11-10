package uk.ac.soton.app.model.CSVDataUnits;

import uk.ac.soton.app.model.TimeUnits.ModelDate;

/**
 * Class used to represent an interaction
 * Where an interaction is an interaction between a user that clicked on an ad shown to them and the website, i.e. a row in a server log
 */
public class Interaction implements Comparable<Interaction> {

    //Date & time of arriving at the website
    private final ModelDate entryDate;
    //ID of user that arrived at the website
    private final long ID;
    //Date & time of navigating away from the website
    private final ModelDate exitDate;
    //Number of pages viewed by user
    private final int pagesViewed;
    //Whether the user clicked on the ad and acted on it or not
    private final boolean conversion;
    //Whether this click has been filtered in or out
    private boolean visible;

    /**
     * Creates an interaction
     * @param entryDate date and time of arriving at the website
     * @param ID ID of user that arrived at the website
     * @param exitDate date and time of navigating away from the website
     * @param pagesViewed number of pages viewed by user
     * @param conversion whether the user clicked on the ad and acted on it or not
     */
    public Interaction(String entryDate, String ID, String exitDate, String pagesViewed, String conversion) {
        //Checks if the given entry date is in a valid format
        //If so, sets this interaction's entry date to it, otherwise, throws an IllegalArgumentException
        try {
            String[] array = entryDate.split(" ");
            String[] splitDate = array[0].split("-");
            String[] splitTime = array[1].split(":");

            int year = Integer.parseInt(splitDate[0]);
            int month = Integer.parseInt(splitDate[1]);
            int day = Integer.parseInt(splitDate[2]);

            int hour = Integer.parseInt(splitTime[0]);
            int minute = Integer.parseInt(splitTime[1]);
            int second = Integer.parseInt(splitTime[2]);

            boolean validDateValues = 0 < year && 0 < month && 0 < day;
            boolean validDateFormat = (splitDate[0].length() == 4) && (splitDate[1].length() == 2) && (splitDate[2].length() == 2);
            boolean validTimeFormat =(splitTime[0].length() == 2) && (splitTime[1].length() == 2) && (splitTime[2].length() == 2);

            if (validDateValues && validDateFormat && validTimeFormat) {this.entryDate = new ModelDate(year, month, day, hour, minute, second);}
            else {throw new IllegalArgumentException("Illegal entry date argument.");}
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {throw new IllegalArgumentException("Illegal entry date argument.");}

        //Checks if the given ID is in a valid format
        //If so, sets this interaction's ID to it, otherwise, throws an IllegalArgumentException
        try {
            long longID = Long.parseLong(ID);
            if(0 < longID) {this.ID = longID;}
            else {throw new IllegalArgumentException("Illegal ID argument.");}
        } catch (NumberFormatException e) {throw new IllegalArgumentException("Illegal ID argument.");}

        //Checks if the given exit date is in a valid format
        //If so, sets this interaction's exit date to it, otherwise, throws an IllegalArgumentException
        if (exitDate.equals("n/a")) {this.exitDate = null;}
        else {
            try {
                String[] array = exitDate.split(" ");
                String[] splitDate = array[0].split("-");
                String[] splitTime = array[1].split(":");

                int year = Integer.parseInt(splitDate[0]);
                int month = Integer.parseInt(splitDate[1]);
                int day = Integer.parseInt(splitDate[2]);

                int hour = Integer.parseInt(splitTime[0]);
                int minute = Integer.parseInt(splitTime[1]);
                int second = Integer.parseInt(splitTime[2]);

                boolean validDateValues = 0 < year && 0 < month && 0 < day;
                boolean validDateFormat = (splitDate[0].length() == 4) && (splitDate[1].length() == 2) && (splitDate[2].length() == 2);
                boolean validTimeFormat =(splitTime[0].length() == 2) && (splitTime[1].length() == 2) && (splitTime[2].length() == 2);

                if (validDateValues && validDateFormat && validTimeFormat) {this.exitDate = new ModelDate(year, month, day, hour, minute, second);}
                else {throw new IllegalArgumentException("Illegal exit date argument.");}
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {throw new IllegalArgumentException("Illegal exit date argument.");}
        }

        //Checks if the interaction's entry date is greater than the interaction's exit date
        //If so, throws an IllegalArgumentException
        if ((this.exitDate != null) && this.entryDate.isGreaterThan(this.exitDate)) throw new IllegalArgumentException("Illegal entry and exit dates argument.");

        //Checks if the given pages viewed is in a valid format
        //If so, sets this interaction's pages viewed to it, otherwise, throws an IllegalArgumentException
        try {
            int intPagesViewed = Integer.parseInt(pagesViewed);
            if (0 < intPagesViewed) {this.pagesViewed = intPagesViewed;}
            else {throw new IllegalArgumentException("Illegal pages viewed argument.");}
        } catch (NumberFormatException e) {throw new IllegalArgumentException("Illegal pages viewed argument.");}

        //Checks if the given conversion is in a valid format
        //If so, sets this interaction's conversion to it, otherwise, throws an IllegalArgumentException
        if (conversion.equals("Yes") || conversion.equals("No")) {this.conversion = conversion.equals("Yes");}
        else {throw new IllegalArgumentException("Illegal conversion argument.");}

        //Sets this interaction to be filtered in
        this.visible = true;
    }


    /**
     * Compares this interaction to another interaction using their dates
     * @param interaction another interaction
     * @return -1 if less than, 0 if equal to and 1 if greater than
     */
    public int compareTo(Interaction interaction) {
        if (this.entryDate.isLessThan(interaction.getEntryDate())) return -1;
        if (this.entryDate.isGreaterThan(interaction.getEntryDate())) return 1;
        return 0;
    }

    /**
     *Gets the date and time of arriving at the website
     * @return entry date
     */
    public ModelDate getEntryDate() {
        return this.entryDate;
    }

    /**
     * Gets the ID of the user that arrived at the website
     * @return ID
     */
    public long getID() {
        return this.ID;
    }

    /**
     * Gets the date and time of navigating away from the website
     * @return exit date
     */
    public ModelDate getExitDate() {
        return this.exitDate;
    }

    /**
     * Gets the number of pages viewed by user
     * @return pages viewed
     */
    public int getPagesViewed() {
        return this.pagesViewed;
    }

    /**
     * Gets whether the user clicked on the ad and acted on it or not
     * @return true if yes and false if not
     */
    public boolean getConversion() {
        return this.conversion;
    }

    /**
     * Gets whether this interaction has been filtered in or out
     * @return true if in and false if out
     */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * Sets this interaction to be filtered in or out
     * @param visible true if in and false if out
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
