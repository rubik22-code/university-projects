package uk.ac.soton.app.model.CSVDataUnits;

import uk.ac.soton.app.model.TimeUnits.ModelDate;

/**
 * Class used to represent a click
 * Where a click is an instance where a user clicked on an ad shown to them, i.e. a row in a click log
 */
public class Click implements Comparable<Click> {

    //Date & time of click
    private final ModelDate date;
    //ID of user that clicked on the ad shown to them
    private final long ID;
    //Cost of this click
    private final double cost;
    //Whether this click has been filtered in or out
    private boolean visible;

    /**
     * Creates a click
     * @param date date and time of this click
     * @param ID ID of user that clicked on the ad shown to them
     * @param cost cost of this click
     */
    public Click(String date, String ID, String cost) {
        //Checks if the given date is in a valid format
        //If so, sets this click's date to it, otherwise, throws an IllegalArgumentException
        try {
            String[] array = date.split(" ");
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

            if (validDateValues && validDateFormat && validTimeFormat) {this.date = new ModelDate(year, month, day, hour, minute, second);}
            else {throw new IllegalArgumentException("Illegal date argument.");}
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {throw new IllegalArgumentException("Illegal date argument.");}

        //Checks if the given ID is in a valid format
        //If so, sets this click's ID to it, otherwise, throws an IllegalArgumentException
        try {
            long longID = Long.parseLong(ID);
            if(0 < longID) {this.ID = longID;}
            else {throw new IllegalArgumentException("Illegal ID argument.");}
        } catch (NumberFormatException e) {throw new IllegalArgumentException("Illegal ID argument.");}

        //Checks if the given cost is in a valid format
        //If so, sets this click's cost to it, otherwise, throws an IllegalArgumentException
        try {
            double doubleCost = Double.parseDouble(cost);
            if(0 <= doubleCost) {this.cost = doubleCost;}
            else {throw new IllegalArgumentException("Illegal cost argument.");}
        } catch (NumberFormatException e) {throw new IllegalArgumentException("Illegal cost argument.");}

        //Sets this click to be filtered in
        this.visible = true;
    }

    /**
     * Compares this click to another click using their dates
     * @param click another click
     * @return -1 if less than, 0 if equal to and 1 if greater than
     */
    public int compareTo(Click click) {
        if (this.date.isLessThan(click.getDate())) return -1;
        if (this.date.isGreaterThan(click.getDate())) return 1;
        return 0;
    }

    /**
     * Gets the date and time of this click
     * @return date
     */
    public ModelDate getDate() {
        return this.date;
    }

    /**
     * Gets the ID of the user that clicked on the ad shown to them
     * @return ID
     */
    public long getID() {
        return this.ID;
    }

    /**
     * Gets the cost of this click
     * @return cost
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Gets whether this click has been filtered in or out
     * @return true if in and false if out
     */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * Sets this click to be filtered in or out
     * @param visible true if in and false if out
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
