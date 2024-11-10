package uk.ac.soton.app.model.TimeUnits;

import uk.ac.soton.app.enums.TimeInterval;

/**
 * Record used to represent a date or an amount of years, months, days, hours, minutes and seconds
 * In the format YYYY-MM-DD HH:MM:SS
 * @param year year of this date
 * @param month month of this date
 * @param day day of this date
 * @param hour hour of this date
 * @param minute minute of this date
 * @param second second of this date
 */
public record ModelDate(int year, int month, int day, int hour, int minute, int second) implements Comparable<ModelDate> {

    /**
     * Creates a date
     * @param year year of this date
     * @param month month of this date
     * @param day day of this date
     * @param hour hour of this date
     * @param minute minute of this date
     * @param second second of this date
     */
    public ModelDate(int year, int month, int day, int hour, int minute, int second) {
        //Checks if the given year is in a valid format
        //If so, sets this date's year to it, otherwise, throws an IllegalArgumentException
        if (-1 < year) {this.year = year;}
        else {throw new IllegalArgumentException("Illegal year argument.");}

        //Checks if the given month is in a valid format
        //If so, sets this date's month to it, otherwise, throws an IllegalArgumentException
        if (-1 < month && month < 13) {this.month = month;}
        else {throw new IllegalArgumentException("Illegal month argument.");}

        //Checks if the given day is in a valid format
        //If so, sets this date's day to it, otherwise, throws an IllegalArgumentException
        if (checkDay(day, month, year)) {this.day = day;}
        else {throw new IllegalArgumentException("Illegal day argument.");}

        //Checks if the given hour is in a valid format
        //If so, sets this date's hour to it, otherwise, throws an IllegalArgumentException
        if (-1 < hour && hour < 24) {this.hour = hour;}
        else {throw new IllegalArgumentException("Illegal hour argument.");}

        //Checks if the given minute is in a valid format
        //If so, sets this date's minute to it, otherwise, throws an IllegalArgumentException
        if (-1 < minute && minute < 60) {this.minute = minute;}
        else {throw new IllegalArgumentException("Illegal minute argument.");}

        //Checks if the given second is a valid format
        //If so, sets this date's second to it, otherwise, throws an IllegalArgumentException
        if (-1 < second && second < 60) {this.second = second;}
        else {throw new IllegalArgumentException("Illegal second argument.");}
    }

    /**
     * Checks if the day of this date is valid
     * @param day day of this date
     * @param month month of this date
     * @param year year of this date
     * @return whether the day of this date is valid or not
     */
    public static boolean checkDay(int day, int month, int year) {
        if ((month == 0 || month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && -1 < day && day < 32) return true;
        if ((month == 4 || month == 6 || month == 9 || month == 11) && -1 < day && day < 31) return true;
        if (month == 2 && (year % 4 == 0) && -1 < day && day < 30) return true;
        return month == 2 && (year % 4 != 0) && -1 < day && day < 29;
    }

    /**
     * Converts this date into a string
     * @return this date as a string
     */
    public String asString() {
        return String.format("%s-%02d-%02d %02d:%02d:%02d", this.year, this.month, this.day, this.hour, this.minute, this.second);
    }

    /**
     * Converts a time interval's lower bound, in the form of a date, into a lower bound in the form of a string
     * @param timeIntervalLength time interval's length
     * @return time interval's lower bound, in the form of a string
     */
    public String asString(TimeInterval timeIntervalLength) {
        if (timeIntervalLength == TimeInterval.MONTH) return String.format("%s-%02d-%02d", this.year, this.month, 1);
        if (timeIntervalLength == TimeInterval.WEEK) return String.format("%s-%02d-%02d", this.year, this.month, this.day);
        if (timeIntervalLength == TimeInterval.DAY) return String.format("%s-%02d-%02d", this.year, this.month, this.day);
        if (timeIntervalLength == TimeInterval.HOUR) return String.format("%s-%02d-%02d %02d:%02d:%02d", this.year, this.month, this.day, this.hour, 0, 0);
        return (this.asString());
    }

    /**
     * Converts this date into a long
     * @return this date as a long
     */
    public Long asLong() {
        //Converts this date into a string
        String monthAsString = String.valueOf(this.month);
        if (monthAsString.length() == 1) monthAsString = "0" + monthAsString;

        String dayAsString = String.valueOf(this.day);
        if (dayAsString.length() == 1) dayAsString = "0" + dayAsString;

        String hourAsString = String.valueOf(this.hour);
        if (hourAsString.length() == 1) hourAsString = "0" + hourAsString;

        String minuteAsString = String.valueOf(this.minute);
        if (minuteAsString.length() == 1) minuteAsString = "0" + minuteAsString;

        String secondAsString = String.valueOf(this.second);
        if (secondAsString.length() == 1) secondAsString = "0" + secondAsString;

        //Converts this date, in the form of a string, to a date, in the form of a long, and returns it
        return Long.parseLong(this.year + monthAsString + dayAsString + hourAsString + minuteAsString + secondAsString);
    }

    /**
     * Checks if this date is equal to another date
     * @param date another date
     * @return whether this date is equal to the other date or not
     */
    public boolean isEqualTo(ModelDate date) {
        if (this.year != date.year()) return false;
        if (this.month != date.month()) return false;
        if (this.day != date.day()) return false;
        if (this.hour != date.hour()) return false;
        if (this.minute != date.minute()) return false;
        return (this.second == date.second());
    }

    /**
     * Checks if this date is less than another date
     * @param date another date
     * @return whether this date is less than the other date or not
     */
    public boolean isLessThan(ModelDate date) {
        if (this.year < date.year()) return true;
        if (this.year > date.year()) return false;

        if (this.month < date.month()) return true;
        if (this.month > date.month()) return false;

        if (this.day < date.day()) return true;
        if (this.day > date.day()) return false;

        if (this.hour < date.hour()) return true;
        if (this.hour > date.hour()) return false;

        if (this.minute < date.minute()) return true;
        if (this.minute > date.minute()) return false;

        return this.second < date.second();
    }

    /**
     * Checks if this date is greater than another date
     * @param date another date
     * @return whether this date is greater than the other date or not
     */
    public boolean isGreaterThan(ModelDate date) {
        if (this.year > date.year()) return true;
        if (this.year < date.year()) return false;

        if (this.month > date.month()) return true;
        if (this.month < date.month()) return false;

        if (this.day > date.day()) return true;
        if (this.day < date.day()) return false;

        if (this.hour > date.hour()) return true;
        if (this.hour < date.hour()) return false;

        if (this.minute > date.minute()) return true;
        if (this.minute < date.minute()) return false;

        return this.second > date.second();
    }

    /**
     * Compares this date to another date
     * @param date another date
     * @return -1 if less than, 0 if equal to and 1 if greater than
     */
    public int compareTo(ModelDate date) {
        if (this.isLessThan(date)) return -1;
        if (this.isGreaterThan(date)) return 1;
        return 0;
    }

    /**
     * Adds another date to this date
     * @param date another date
     * @return date resulted from this addition
     */
    public ModelDate addDates(ModelDate date) {
        //Stores the year, month, day, hour and minute of this date
        int d1Year = this.year;
        int d1Month = this.month;
        int d1Day = this.day;
        int d1Hour = this.hour;
        int d1Minute = this.minute;

        //Adds seconds
        int second = this.second + date.second();
        if (59 < second) {
            second -= 60;
            d1Minute++;
        }

        //Adds minutes
        int minute = d1Minute + date.minute();
        if (59 < minute) {
            minute -= 60;
            d1Hour++;
        }

        //Adds hours
        int hour = d1Hour + date.hour();
        if (23 < hour) {
            hour -= 24;
            d1Day++;
        }

        //Adds days
        int day = d1Day + date.day();
        if ((d1Month == 1 || d1Month == 3 || d1Month == 5 || d1Month == 7 || d1Month == 8 || d1Month == 10 || d1Month == 12) && 31 < day) {
            day -= 31;
            d1Month++;
        } else if ((d1Month == 4 || d1Month == 6 || d1Month == 9 || d1Month == 11) && 30 < day) {
            day -= 30;
            d1Month++;
        } else if (d1Month == 2 && (d1Year % 4 == 0) && 29 < day) {
            day -= 29;
            d1Month++;
        } else if (d1Month == 2 && (d1Year % 4 != 0) && 28 < day) {
            day -= 28;
            d1Month++;
        }

        //Adds months
        int month = d1Month + date.month();
        if (12 < month) {
            month -= 12;
            d1Year++;
        }

        //Adds years
        int year = d1Year + date.year();

        //Returns date resulted from this addition
        return new ModelDate(year, month, day, hour, minute, second);
    }

    /**
     * Subtracts another date from this date
     * @param date another date
     * @return date resulted from this subtraction
     */
    public ModelDate subtractDates(ModelDate date) {
        //Checks if the other date is greater than this date
        //If so returns a date of 0 years, 0 months, 0 days, 0 hours, 0 minutes and 0 seconds
        if (!this.isGreaterThan(date)) return new ModelDate(0, 0, 0, 0, 0, 0);

        //Stores the year, month, day, hour and minute of this date
        int d1Year = this.year;
        int d1Month = this.month;
        int d1Day = this.day;
        int d1Hour = this.hour;
        int d1Minute = this.minute;

        //Subtracts seconds
        int second = this.second - date.second();
        if (second < 0) {
            second += 60;
            d1Minute--;
        }

        //Subtracts minutes
        int minute = d1Minute - date.minute();
        if (minute < 0) {
            minute += 60;
            d1Hour--;
        }

        //Subtracts hours
        int hour = d1Hour - date.hour();
        if (hour < 0) {
            hour += 24;
            d1Day--;
        }

        //Subtracts days
        int day = d1Day - date.day();
        if (day < 0) {
            if ((d1Month == 1 | d1Month == 2 || d1Month == 4 || d1Month == 6 || d1Month == 8 || d1Month == 9 || d1Month == 11)) {
                day += 31;
            } else if (d1Month == 5 || d1Month == 7 || d1Month == 10 || d1Month == 12) {
                day += 30;
            } else if (d1Month == 3 && (d1Year % 4 == 0)) {
                day += 29;
            } else if (d1Month == 3) {
                day += 28;
            }
            d1Month--;
        }

        //Subtracts days
        int month = d1Month - date.month();
        if (month < 0) {
            month += 12;
            d1Year--;
        }

        //Subtracts years
        int year = d1Year - date.year();

        //Returns date resulted from this subtraction
        return new ModelDate(year, month, day, hour, minute, second);
    }

    /**
     * Creates a date identical to this date
     * @return identical date
     */
    public ModelDate createIdenticalDate() {
        return new ModelDate(this.year, this.month, this.day, this.hour, this.minute, this.second);
    }

    /**
     * Converts this date into the lower bound of the time interval that it happens during
     * @param timeIntervalLength time interval's length
     * @return time interval's lower bound
     */
    public ModelDate convertToTimeInterval(TimeInterval timeIntervalLength) {
        if (timeIntervalLength == TimeInterval.HOUR) {return new ModelDate(this.year, this.month, this.day, this.hour, 0, 0);
        } else if (timeIntervalLength == TimeInterval.DAY) {return new ModelDate(this.year, this.month, this.day, 0, 0, 0);
        } else if (timeIntervalLength == TimeInterval.WEEK) {return new ModelDate(this.year, this.month, this.day, 0, 0, 0);
        } else {return new ModelDate(this.year, this.month, 1, 0, 0, 0);}
    }

    /**
     * Given the existence of previous time intervals of length week
     * Converts this date into the lower bound of the time interval, of length week, that it happens during
     * @param date date that happens on the same day as the first time interval's lower bound
     * @return time interval's lower bound
     */
    public ModelDate convertToWeekTimeInterval(ModelDate date) {
        //If this date happens before the first time interval
        //Converts this date into the lower bound of the time interval, of length day, that it happens during and returns it
        if (this.isLessThan(date.convertToTimeInterval(TimeInterval.WEEK))) return this.convertToTimeInterval(TimeInterval.WEEK);

        //Date used to increment a lower bound to the next lower bound
        ModelDate increment = new ModelDate(0, 0, 7, 0, 0, 0);

        //Current time interval's lower bound
        ModelDate currentTimeInterval = date.convertToTimeInterval(TimeInterval.WEEK);

        //Loops through each time interval's lower bound
        while (true) {
            //Checks if the date happens within the current time interval
            //If so, returns the current time interval's lower bound
            //Otherwise, moves onto next the current time interval
            if (this.isLessThan(currentTimeInterval.addDates(increment))) {
                return currentTimeInterval;
            } else {
                currentTimeInterval = currentTimeInterval.addDates(increment);
            }
        }
    }
}
