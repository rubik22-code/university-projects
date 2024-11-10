package uk.ac.soton.app.model.CSVDataUnits;

import uk.ac.soton.app.model.TimeUnits.ModelDate;

/**
 * Class used to represent an impression
 * Where an impression is an instance where an ad was shown to a user, i.e. a row in an impression log
 */
public class Impression implements Comparable<Impression> {

    //Date & time of impression
    private final ModelDate date;
    //ID of user shown an ad
    private final long ID;
    //Gender of user shown an ad
    private final String gender;
    //Age group of user shown an ad
    private final String age;
    //Income of user shown an ad
    private final String income;
    //Context of ad shown
    private final String context;
    //Cost of this impression
    private final double cost;
    //Whether this impression has been filtered in or out
    private boolean visible;

    /**
     * Creates an impression
     * @param date date and time of this impression
     * @param ID ID of user shown an ad
     * @param gender gender of user shown an ad
     * @param age age group of user shown an ad
     * @param income income of user shown an ad
     * @param context context of ad shown
     * @param cost cost of this impression
     */
    public Impression(String date, String ID, String gender, String age, String income, String context, String cost) {
        //Checks if the given date is in a valid format
        //If so, sets this impression's date to it, otherwise, throws an IllegalArgumentException
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
        //If so, sets this impression's ID to it, otherwise, throws an IllegalArgumentException
        try {
            long longID = Long.parseLong(ID);
            if(0 < longID) {this.ID = longID;}
            else {throw new IllegalArgumentException("Illegal ID argument.");}
        } catch (NumberFormatException e) {throw new IllegalArgumentException("Illegal ID argument.");}

        //Checks if the given gender is in a valid format
        //If so, sets this impression's gender to it, otherwise, throws an IllegalArgumentException
        if(gender.equals("Male") || gender.equals("Female")) {this.gender = gender;}
        else {throw new IllegalArgumentException("Illegal gender argument.");}

        //Checks if the given age is in a valid format
        //If so, sets this impression's age to it, otherwise, throws an IllegalArgumentException
        if(age.equals("<25") || age.equals("25-34") || age.equals("35-44") || age.equals("45-54") || age.equals(">54")) {this.age = age;}
        else {throw new IllegalArgumentException("Illegal age argument.");}

        //Checks if the given income is in a valid format
        //If so, sets this impression's income to it, otherwise, throws an IllegalArgumentException
        if(income.equals("Low") || income.equals("Medium") || income.equals("High")) {this.income = income;}
        else {throw new IllegalArgumentException("Illegal income argument.");}

        //Checks if the given context is in a valid format
        //If so, sets this impression's context to it, otherwise, throws an IllegalArgumentException
        if(context.equals("News") || context.equals("Shopping") || context.equals("Social Media") || context.equals("Blog") || context.equals("Hobbies") || context.equals("Travel")) {this.context = context;}
        else {throw new IllegalArgumentException("Illegal context argument.");}

        //Checks if the cost is in a valid format
        //If so, sets this impression's cost to it, otherwise, throws an IllegalArgumentException
        try {
            double doubleCost = Double.parseDouble(cost);
            if(0 <= doubleCost) {this.cost = doubleCost;}
            else {throw new IllegalArgumentException("Illegal cost argument.");}
        } catch (NumberFormatException e) {throw new IllegalArgumentException("Illegal cost argument.");}

        //Sets this impression to be filtered in
        this.visible = true;
    }

    /**
     * Compares this impression to another impression using their dates
     * @param impression another impression
     * @return -1 if less than, 0 if equal to and 1 if greater than
     */
    public int compareTo(Impression impression) {
        if (this.date.isLessThan(impression.getDate())) return -1;
        if (this.date.isGreaterThan(impression.getDate())) return 1;
        return 0;
    }

    /**
     * Gets the date and time of this impression
     * @return date
     */
    public ModelDate getDate() {
        return this.date;
    }

    /**
     * Gets the ID of the user shown an ad
     * @return ID
     */
    public long getID() {
        return this.ID;
    }

    /**
     * Gets the gender of the user shown an ad
     * @return gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Gets the age group of the user shown an ad
     * @return age
     */
    public String getAge() {
        return this.age;
    }

    /**
     * Gets the income of the user shown an ad
     * @return income
     */
    public String getIncome() {
        return this.income;
    }

    /**
     * Gets the context of the ad shown
     * @return context
     */
    public String getContext() {
        return this.context;
    }

    /**
     * Gets the cost of this impression
     * @return cost
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Gets whether this impression has been filtered in or out
     * @return true if in and false if out
     */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * Sets this impression to be filtered in or out
     * @param visible true if in and false if out
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}