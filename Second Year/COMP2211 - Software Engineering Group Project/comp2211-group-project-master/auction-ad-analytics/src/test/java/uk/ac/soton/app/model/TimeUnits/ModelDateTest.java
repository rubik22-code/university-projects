package uk.ac.soton.app.model.TimeUnits;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.enums.TimeInterval;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the ModelDate record
 */
public class ModelDateTest {

    //List of ModelDate objects used to test some methods of the ModelDate record
    List<ModelDate> modelDateObjects;

    /**
     * Sets up everything needed to test the ModelDate record
     */
    @BeforeEach
    void setUp() {
        //Sets up a list of ModelDate objects to be used to test some methods of the ModelDate record
        this.modelDateObjects = new ArrayList<>();
        this.modelDateObjects.add(new ModelDate(2015, 3, 2 , 12,5, 13));
        this.modelDateObjects.add(new ModelDate(0, 0, 0, 0, 0, 0));
        this.modelDateObjects.add(new ModelDate(2015, 12, 31, 23, 59, 59));
        this.modelDateObjects.add(new ModelDate(2015, 11, 30, 23, 59, 59));
        this.modelDateObjects.add(new ModelDate(2016, 2, 29, 23, 59, 59));
        this.modelDateObjects.add(new ModelDate(2015, 2, 28, 23, 59, 59));
        this.modelDateObjects.add(new ModelDate(2015, 1, 9, 12, 5, 13));
    }

    /**
     * Tests the ModelDate() constructor
     */
    @Test
    void testModelDate() {
        //TEST #1
        //Tests if an IllegalArgumentException is thrown
        //When a year with a value below 0 is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(-1, 1, 1, 12, 0, 6));

        //TEST #2
        //Tests if an IllegalArgumentException is thrown
        //When a month with a value below 0 is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(2015, -1, 1, 12, 0, 6));

        //TEST #3
        //Tests if an IllegalArgumentException is thrown
        //When a month with a value above 12 is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(2015, 13, 1, 12, 0, 6));

        //TEST #4
        //Tests if an IllegalArgumentException is thrown
        //When an invalid day is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(2015, 1, -1, 12, 0, 6));

        //TEST #5
        //Tests if an IllegalArgumentException is thrown
        //When an hour with a value below 0 is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(2015, 1, 1, -1, 0, 6));

        //TEST #6
        //Tests if an IllegalArgumentException is thrown
        //When an hour with a value above 23 is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(2015, 1, 1, 24, 0, 6));

        //TEST #7
        //Tests if an IllegalArgumentException is thrown
        //When a minute with a value below 0 is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(2015, 1, 1, 12, -1, 6));

        //TEST #8
        //Tests if an IllegalArgumentException is thrown
        //When a minute with a value above 59 is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(2015, 1, 1, 12, 60, 6));

        //TEST #9
        //Tests if an IllegalArgumentException is thrown
        //When a second with a value below 0 is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(2015, 1, 1, 12, 0, -1));

        //TEST #10
        //Tests if an IllegalArgumentException is thrown
        //When a minute with a value above 59 is given
        assertThrows(IllegalArgumentException.class, () -> new ModelDate(2015, 1, 1, 12, 0, 60));
    }

    /**
     * Tests the checkDay() method
     */
    @Test
    void testCheckDay() {
        //TEST #1
        //Tests if true is returned
        //When a day with a value of 0 is given
        assertTrue(ModelDate.checkDay(0, 1, 2015));

        //TEST #2
        //Tests if true is returned
        //When a month with a value of 1 and a day with a value of 31 are given
        assertTrue(ModelDate.checkDay(31, 1, 2015));

        //TEST #3
        //Tests if true is returned
        //When a month with a value of 4 and a day with a value of 30 are given
        assertTrue(ModelDate.checkDay(30, 4, 2015));

        //TEST #4
        //Tests if true is returned
        //When a year with a value of 2016, a month with a value of 2 and a day with a value of 29 are given
        assertTrue(ModelDate.checkDay(29, 2, 2016));

        //TEST #5
        //Tests if true is returned
        //When a year with a value of 2015, a month with a value of 2 and a day with a value of 28 are given
        assertTrue(ModelDate.checkDay(28, 2, 2015));

        //TEST #6
        //Tests if false is returned
        //When a day with a value below 0 is given
        assertFalse(ModelDate.checkDay(-1, 1, 2015));

        //TEST #7
        //Tests if false is returned
        //When a month with a value of 1 and a day with a value above 31 are given
        assertFalse(ModelDate.checkDay(32, 1, 2015));

        //TEST #8
        //Tests if false is returned
        //When a month with a value of 4 and a day with a value above 30 are given
        assertFalse(ModelDate.checkDay(31, 4, 2015));

        //TEST #9
        //Tests if false is returned
        //When a year with a value of 2016, a month with a value of 2 and a day with a value above 29 are given
        assertFalse(ModelDate.checkDay(30, 2, 2016));

        //TEST #10
        //Tests if false is returned
        //When a year with a value of 2015, a month with a value of 2 and a day with a value above 28 are given
        assertFalse(ModelDate.checkDay(29, 2, 2015));
    }

    /**
     * Tests the asString() method
     */
    @Test
    void testAsString() {
        //TEST #1
        //Tests if the date, in the form of a string, is returned
        assertEquals("2015-03-02 12:05:13", this.modelDateObjects.get(0).asString());

        //TEST #2
        //Tests if the lower bound of the time interval, of length hour, that the date happens during is returned, in the form of a string
        assertEquals("2015-03-02 12:00:00", this.modelDateObjects.get(0).asString(TimeInterval.HOUR));

        //TEST #3
        //Tests if the lower bound of the time interval, of length day, that the date happens during is returned, in the form of a string
        assertEquals("2015-03-02", this.modelDateObjects.get(0).asString(TimeInterval.DAY));

        //TEST #4
        //Tests if the lower bound of the time interval, of length week, that the date happens during is returned, in the form of a string
        assertEquals("2015-03-02", this.modelDateObjects.get(0).asString(TimeInterval.WEEK));

        //TEST #5
        //Tests if the lower bound of the time interval, of length month, that the date happens during is returned, in the form of a string
        assertEquals("2015-03-01", this.modelDateObjects.get(0).asString(TimeInterval.MONTH));
    }

    /**
     * Tests the asLong() method
     */
    @Test
    void testAsLong() {
        //TEST #1
        //Tests if the date, in the form of a long, is returned
        //When the date's month, day, hour, minute and second are all below 10
        assertEquals(20150604030700L, new ModelDate(2015, 6, 4, 3, 7, 0).asLong());

        //TEST #2
        //Tests if the date, in the form of a long, is returned
        //When the date's month, day, hour, minute and second are all above 9
        assertEquals(20151121165647L, new ModelDate(2015, 11, 21, 16, 56, 47).asLong());
    }

    /**
     * Tests the isEqualTo() method
     */
    @Test
    void testIsEqualTo() {
        //TEST #1
        //Tests if true is returned
        //When the given date is identical
        assertTrue(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 3, 2, 12, 5, 13)));

        //TEST #2
        //Tests if false is returned
        //When the given date differs by one more year
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2016, 3, 2, 12, 5, 13)));

        //TEST #3
        //Tests if false is returned
        //When the given date differs by one less year
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2014, 3, 2, 12, 5, 13)));

        //TEST #4
        //Tests if false is returned
        //When the given date differs by one more month
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 4, 2, 12, 5, 13)));

        //TEST #5
        //Tests if false is returned
        //When the given date differs by one less month
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 2, 2, 12, 5, 13)));

        //TEST #6
        //Tests if false is returned
        //When the given date differs by one more day are given
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 3, 3, 12, 5, 13)));

        //TEST #7
        //Tests if false is returned
        //When the given date differs by one less day are given
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 3, 1, 12, 5, 13)));

        //TEST #8
        //Tests if false is returned
        //When the given date differs by one more hour are given
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 3, 2, 13, 5, 13)));

        //TEST #9
        //Tests if false is returned
        //When the given date differs by one less hour are given
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 3, 2, 11, 5, 13)));

        //TEST #10
        //Tests if false is returned
        //When the given date differs by one more minute are given
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 3, 2, 12, 6, 13)));

        //TEST #11
        //Tests if false is returned
        //When the given date differs by one less minute are given
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 3, 2, 12, 4, 13)));

        //TEST #12
        //Tests if false is returned
        //When the given date differs by one more second are given
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 3, 2, 12, 5, 14)));

        //TEST #13
        //Tests if false is returned
        //When the given date differs by one less second are given
        assertFalse(this.modelDateObjects.get(0).isEqualTo(new ModelDate(2015, 3, 2, 12, 5, 12)));
    }

    /**
     * Tests the isLessThan() method
     */
    @Test
    void testIsLessThan() {
        //TEST #1
        //Tests if false is returned
        //When the given date is equal to the date
        assertFalse(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 3, 2, 12, 5, 13)));

        //TEST #2
        //Tests if false is returned
        //When the given date is less than the date by a year
        assertFalse(this.modelDateObjects.get(0).isLessThan(new ModelDate(2014, 3, 2, 12, 5, 13)));

        //TEST #3
        //Tests if true is returned
        //When the given date is greater than the date by a year
        assertTrue(this.modelDateObjects.get(0).isLessThan(new ModelDate(2016, 3, 2, 12, 5, 13)));

        //TEST #4
        //Tests if false is returned
        //When the given date is less than the date by a month
        assertFalse(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 2, 2, 12, 5, 13)));

        //TEST #5
        //Tests if true is returned
        //When the given date is greater than the date by a month
        assertTrue(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 4, 2, 12, 5, 13)));

        //TEST #6
        //Tests if false is returned
        //When the given date is less than the date by a day
        assertFalse(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 3, 1, 12, 5, 13)));

        //TEST #7
        //Tests if true is returned
        //When the given date is greater than the date by a day
        assertTrue(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 3, 3, 12, 5, 13)));

        //TEST #8
        //Tests if false is returned
        //When the given date is less than the date by an hour
        assertFalse(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 3, 2, 11, 5, 13)));

        //TEST #9
        //Tests if true is returned
        //When the given date is greater than the date by an hour
        assertTrue(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 3, 2, 13, 5, 13)));

        //TEST #10
        //Tests if false is returned
        //When the given date is less than the date by a minute
        assertFalse(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 3, 2, 12, 4, 13)));

        //TEST #11
        //Tests if true is returned
        //When the given date is greater than the date by a minute
        assertTrue(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 3, 2, 12, 6, 13)));

        //TEST #12
        //Tests if false is returned
        //When the given date is less than the date by a second
        assertFalse(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 3, 2, 12, 5, 12)));

        //TEST #13
        //Tests if true is returned
        //When the given date is greater than the date by a second
        assertTrue(this.modelDateObjects.get(0).isLessThan(new ModelDate(2015, 3, 2, 12, 5, 14)));
    }

    /**
     * Tests the isGreaterThan() method
     */
    @Test
    void testIsGreaterThan() {
        //TEST #1
        //Tests if false is returned
        //When the given date is equal to the date
        assertFalse(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 3, 2, 12, 5, 13)));

        //TEST #2
        //Tests if true is returned
        //When the given date is less than the date by a year
        assertTrue(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2014, 3, 2, 12, 5, 13)));

        //TEST #3
        //Tests if false is returned
        //When the given date is greater than the date by a year
        assertFalse(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2016, 3, 2, 12, 5, 13)));

        //TEST #4
        //Tests if true is returned
        //When the given date is less than the date by a month
        assertTrue(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 2, 2, 12, 5, 13)));

        //TEST #5
        //Tests if false is returned
        //When the given date is greater than the date by a month
        assertFalse(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 4, 2, 12, 5, 13)));

        //TEST #6
        //Tests if true is returned
        //When the given date is less than the date by a day
        assertTrue(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 3, 1, 12, 5, 13)));

        //TEST #7
        //Tests if false is returned
        //When the given date is greater than the date by a day
        assertFalse(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 3, 3, 12, 5, 13)));

        //TEST #8
        //Tests if true is returned
        //When the given date is less than the date by an hour
        assertTrue(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 3, 2, 11, 5, 13)));

        //TEST #9
        //Tests if false is returned
        //When the given date is greater than the date by an hour
        assertFalse(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 3, 2, 13, 5, 13)));

        //TEST #10
        //Tests if true is returned
        //When the given date is less than the date by a minute
        assertTrue(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 3, 2, 12, 4, 13)));

        //TEST #11
        //Tests if false is returned
        //When the given date is greater than the date by a minute
        assertFalse(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 3, 2, 12, 6, 13)));

        //TEST #12
        //Tests if true is returned
        //When the given date is less than the date by a second
        assertTrue(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 3, 2, 12, 5, 12)));

        //TEST #13
        //Tests if false is returned
        //When the given date is greater than the date by a second
        assertFalse(this.modelDateObjects.get(0).isGreaterThan(new ModelDate(2015, 3, 2, 12, 5, 14)));
    }

    /**
     * Tests the compareTo() method
     */
    @Test
    void testCompareTo() {
        //TEST #1
        //Tests if -1 is returned
        //When the given date is greater than the date
        assertEquals(-1, modelDateObjects.get(0).compareTo(new ModelDate(2015, 3, 2, 12, 5, 14)));

        //TEST #2
        //Tests if 0 is returned
        //When the given date is equal to the date
        assertEquals(0, modelDateObjects.get(0).compareTo(new ModelDate(2015, 3, 2, 12, 5, 13)));

        //TEST #3
        //Tests if 1 is returned
        //When the given date is less than the date
        assertEquals(1, modelDateObjects.get(0).compareTo(new ModelDate(2015, 3, 2, 12, 5, 12)));
    }

    /**
     * Tests the addDates() method
     */
    @Test
    void testAddDates() {
        //TEST #1
        //Tests if the expected date is returned
        //When the given date is added to the date and the result doesn't need to be normalised
        assertTrue(this.modelDateObjects.get(0).addDates(new ModelDate(9, 3, 3, 2, 5, 5)).isEqualTo(new ModelDate(2024, 6, 5, 14, 10, 18)));

        //TEST #2
        //Tests if the expected date is returned
        //When the given date is added to the date and the month, day, hour, minute and second of the result needs to be normalised
        assertTrue(this.modelDateObjects.get(0).addDates(new ModelDate(0, 11, 30, 20, 59, 59)).isEqualTo(new ModelDate(2016, 3, 2, 9, 5, 12)));

        //TEST #3
        //Tests if the expected date is returned
        //When the given date is added to a date containing a 30-day month and the month of the result needs to be normalised
        assertTrue(new ModelDate(2015, 4, 21, 13, 2, 1).addDates(new ModelDate(0, 0, 15,2, 5, 5)).isEqualTo(new ModelDate(2015, 5, 6, 15, 7, 6)));

        //TEST #4
        //Tests if the expected date is returned
        //When the given date is added to a date containing a 29-day month and the month of the result needs to be normalised
        assertTrue(new ModelDate(2016, 2, 21, 13, 2, 1).addDates(new ModelDate(0, 0, 15,2, 5, 5)).isEqualTo(new ModelDate(2016, 3, 7, 15, 7, 6)));

        //TEST #5
        //Tests if the expected date is returned
        //When the given date is added to a date containing a 28-day month and the month of the result needs to be normalised
        assertTrue(new ModelDate(2015, 2, 21, 13, 2, 1).addDates(new ModelDate(0, 0, 15,2, 5, 5)).isEqualTo(new ModelDate(2015, 3, 8, 15, 7, 6)));
    }


    /**
     * Tests the subtractDates() method
     */
    @Test
    void testSubtractDates() {
        //TEST #1
        //Tests if the expected date is returned
        //When the given date is subtracted from the date and the result doesn't need to be normalised
        assertTrue(this.modelDateObjects.get(0).subtractDates(new ModelDate(9, 1, 1, 2, 3, 5)).isEqualTo(new ModelDate(2006, 2, 1, 10, 2, 8)));

        //TEST #2
        //Tests if the expected date is returned
        //When the given date is subtracted from the date and the month, day, hour, minute and second of the result needs to be normalised
        assertTrue(this.modelDateObjects.get(0).subtractDates(new ModelDate(0, 5, 4, 14, 7, 15)).isEqualTo(new ModelDate(2014, 9, 25, 21, 57, 58)));

        //TEST #4
        //Tests if the expected date is returned
        //When the given date is subtracted from a date containing a month that comes after a 30-day month and the month of the result needs to be normalised
        assertTrue(new ModelDate(2015, 5, 4, 12, 7, 6).subtractDates(new ModelDate(0, 0, 15,2, 5, 5)).isEqualTo(new ModelDate(2015, 4, 19, 10, 2, 1)));

        //TEST #5
        //Tests if the expected date is returned
        //When the given date is subtracted from a date containing a month that comes after a 29-day month and the month of the result needs to be normalised
        assertTrue(new ModelDate(2016, 3, 4, 12, 7, 6).subtractDates(new ModelDate(0, 0, 15,2, 5, 5)).isEqualTo(new ModelDate(2016, 2, 18, 10, 2, 1)));

        //TEST #6
        //Tests if the expected date is returned
        //When the given date is subtracted from a date containing a month that comes after a 28-day month and the month of the result needs to be normalised
        assertTrue(new ModelDate(2015, 3, 4, 12, 7, 6).subtractDates(new ModelDate(0, 0, 15,2, 5, 5)).isEqualTo(new ModelDate(2015, 2, 17, 10, 2, 1)));
    }

    /**
     * Tests the createIdenticalDate() method
     */
    @Test
    void testCreateIdenticalDate() {
        //TEST #1
        assertTrue(this.modelDateObjects.get(0).createIdenticalDate().isEqualTo(new ModelDate(2015, 3, 2 , 12,5, 13)));
    }

    /**
     * Tests the convertToTimeInterval() method
     */
    @Test
    void testConvertToTimeInterval() {
        //TEST #1
        //Tests if the lower bound of the time interval, of length hour, that the date happens during is returned, in the form of a date
        assertTrue(this.modelDateObjects.get(0).convertToTimeInterval(TimeInterval.HOUR).isEqualTo(new ModelDate(2015, 3, 2, 12, 0, 0)));

        //TEST #2
        //Tests if the lower bound of the time interval, of length day, that the date happens during is returned, in the form of a date
        assertTrue(this.modelDateObjects.get(0).convertToTimeInterval(TimeInterval.DAY).isEqualTo(new ModelDate(2015, 3, 2, 0, 0, 0)));

        //TEST #3
        //Tests if the lower bound of the time interval, of length week, that the date happens during is returned, in the form of a date
        assertTrue(this.modelDateObjects.get(0).convertToTimeInterval(TimeInterval.WEEK).isEqualTo(new ModelDate(2015, 3, 2, 0, 0, 0)));

        //TEST #4
        //Tests if the lower bound of the time interval, of length month, that the date happens during is returned, in the form of a date
        assertTrue(this.modelDateObjects.get(0).convertToTimeInterval(TimeInterval.MONTH).isEqualTo(new ModelDate(2015, 3, 1, 0, 0, 0)));
    }

    /**
     * Tests the convertToWeekTimeInterval() method
     */
    @Test
    void testConvertToWeekTimeInterval() {
        //TEST #1
        //Tests if the expected time interval lower bound is returned
        //When the date happens before the first time interval
        assertTrue(this.modelDateObjects.get(6).convertToWeekTimeInterval(new ModelDate(2015, 1, 10, 11, 8, 4)).isEqualTo(new ModelDate(2015, 1, 9, 0, 0, 0)));

        //TEST #2
        //Tests if the expected time interval lower bound is returned
        //When the date happens on the first day of the first time interval
        assertTrue(this.modelDateObjects.get(6).convertToWeekTimeInterval(new ModelDate(2015, 1, 9, 11, 8, 4)).isEqualTo(new ModelDate(2015, 1, 9, 0, 0, 0)));

        //TEST #3
        //Tests if the expected time interval lower bound is returned
        //When the date happens during the first time interval
        assertTrue(this.modelDateObjects.get(6).convertToWeekTimeInterval(new ModelDate(2015, 1, 7, 11, 8, 4)).isEqualTo(new ModelDate(2015, 1, 7, 0, 0, 0)));


        //TEST #4
        //Tests if the expected time interval lower bound is returned
        //When the date happens during a time interval a year after the first time interval
        assertTrue(this.modelDateObjects.get(6).convertToWeekTimeInterval(new ModelDate(2014, 1, 9, 11, 8, 4)).isEqualTo(new ModelDate(2015, 1, 8, 0, 0, 0)));
    }

    /**
     * Tests the year() method
     */
    @Test
    void testYear() {
        //TEST #1
        //Tests if the expected year is returned
        //When a date has a year with the lowest possible value
        assertEquals(0, modelDateObjects.get(1).year());
    }

    /**
     * Tests the month() method
     */
    @Test
    void testMonth() {
        //TEST #1
        //Tests if the expected month is returned
        //When a date has a month with the lowest possible value
        assertEquals(0, modelDateObjects.get(1).month());

        //TEST #2
        //Tests if the expected month is returned
        //When a date has a month with the highest possible value
        assertEquals(12, modelDateObjects.get(2).month());
    }

    /**
     * Tests the day() method
     */
    @Test
    void testDay() {
        //TEST #1
        //Tests if the expected day is returned
        //When a date has a day with the lowest possible value
        assertEquals(0, modelDateObjects.get(1).day());

        //TEST #2
        //Tests if the expected day is returned
        //When a date has a month with a value of 12 and a day with the highest possible value
        assertEquals(31, modelDateObjects.get(2).day());

        //TEST #3
        //Tests if the expected day is returned
        //When a date has a month with a value of 11 and a day with the highest possible value
        assertEquals(30, modelDateObjects.get(3).day());

        //TEST #4
        //Tests if the expected day is returned
        //When a date has a year with a value of 2016, a month with a value of 2 and a day with the highest possible value
        assertEquals(29, modelDateObjects.get(4).day());

        //TEST #5
        //Tests if the expected day is returned
        //When a date has a year with a value of 2015, a month with a value of 2 and a day with the highest possible value
        assertEquals(28, modelDateObjects.get(5).day());
    }

    /**
     * Tests the hour() method
     */
    @Test
    void testHour() {
        //TEST #1
        //Tests if the expected hour is returned
        //When a date has an hour with the lowest possible value
        assertEquals(0, modelDateObjects.get(1).hour());

        //TEST #2
        //Tests if the expected hour is returned
        //When a date has an hour with the highest possible value
        assertEquals(23, modelDateObjects.get(2).hour());
    }

    /**
     * Tests the minute() method
     */
    @Test
    void testMinute() {
        //TEST #1
        //Tests if the expected minute is returned
        //When a date has a minute with the lowest possible value
        assertEquals(0, modelDateObjects.get(1).minute());

        //TEST #2
        //Tests if the expected minute is returned
        //When a date has a minute with the highest possible value
        assertEquals(59, modelDateObjects.get(2).minute());
    }

    /**
     * Tests the second() method
     */
    @Test
    void testSecond() {
        //TEST #1
        //Tests if the expected second is returned
        //When a date has a second with the lowest possible value
        assertEquals(0, modelDateObjects.get(1).second());

        //TEST #2
        //Tests if the expected second is returned
        //When a date has a second with the highest possible value
        assertEquals(59, modelDateObjects.get(2).second());
    }

}
