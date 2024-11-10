package uk.ac.soton.app.model.CSVDataUnits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the Impression class
 */
public class ImpressionTest {

    //List of Impression objects used to test some methods of the Impression class
    List<Impression> impressionObjects;

    /**
     * Sets up everything needed to test the Impression class
     */
    @BeforeEach
    void setUp() {
        //Sets up a list of Impression objects to be used to test some methods of the Impression class
        this.impressionObjects = new ArrayList<>();
        this.impressionObjects.add(new Impression("1000-01-01 00:00:00", "1", "Male", "<25", "Low", "News", "0.000000"));
        this.impressionObjects.add(new Impression("2015-01-01 12:00:02", "8530398223564990464", "Female", "25-34", "Medium", "Shopping", "0.00171358"));
        this.impressionObjects.add(new Impression("2015-01-01 12:00:02", "4620864431353610000", "Male", "35-44", "High", "Social Media", "0.001632"));
        this.impressionObjects.add(new Impression("2015-01-01 12:00:02", "4620864431353610000", "Male", "45-54", "High", "Blog", "0.001632"));
        this.impressionObjects.add(new Impression("2015-01-01 12:00:02", "4620864431353610000", "Male", ">54", "High", "Hobbies", "0.001632"));
        this.impressionObjects.add(new Impression("2015-01-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Travel", "0.001632"));
    }

    /**
     * Tests the Impression() constructor
     */
    @Test
    void testImpression() {
        //TEST #1
        //Tests if an IllegalArgumentException is thrown
        //When an invalid date is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-13-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #2
        //Tests if an IllegalArgumentException is thrown
        //When a date with no space between the date and time is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-0112:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #3
        //Tests if an IllegalArgumentException is thrown
        //When a date with a missing hyphen is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-0101 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #4
        //Tests if an IllegalArgumentException is thrown
        //When a date with a missing colon is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:0002", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #5
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2O15-01-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #6
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a year with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("0000-01-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #7
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a month with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-00-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #8
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a day with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-00 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #9
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a year with less than 4 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("215-01-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #10
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a year with more than 4 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("02015-01-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #11
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a month with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-1-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #12
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a month with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-001-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #13
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a day with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-1 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #14
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a day with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-001 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #15
        //Tests if an IllegalArgumentException is thrown
        //When a date containing an hour with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 1:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #16
        //Tests if an IllegalArgumentException is thrown
        //When a date containing an hour with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 012:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #17
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a minute with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:0:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #18
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a minute with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:000:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #19
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a second with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:2", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #20
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a second with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:002", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #21
        //Tests if an IllegalArgumentException is thrown
        //When an ID containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:02", "462O864431353610000", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #22
        //Tests if an IllegalArgumentException is thrown
        //When an ID below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:02", "0", "Male", "25-34", "High", "Blog", "0.001632"));

        //TEST #23
        //Tests if an IllegalArgumentException is thrown
        //When an unknown gender is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:02", "4620864431353610000L", "Other", "25-34", "High", "Blog", "0.001632"));

        //TEST #24
        //Tests if an IllegalArgumentException is thrown
        //When an unknown age group is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:02", "4620864431353610000L", "Male", "Other", "High", "Blog", "0.001632"));

        //TEST #25
        //Tests if an IllegalArgumentException is thrown
        //When an unknown income is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:02", "4620864431353610000L", "Male", "25-34", "Other", "Blog", "0.001632"));

        //TEST #26
        //Tests if an IllegalArgumentException is thrown
        //When an unknown context is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:02", "4620864431353610000L", "Male", "25-34", "High", "Other", "0.001632"));

        //TEST #27
        //Tests if an IllegalArgumentException is thrown
        //When a cost containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:02", "4620864431353610000L", "Male", "25-34", "High", "Blog", "0.O01632"));

        //TEST #28
        //Tests if an IllegalArgumentException is thrown
        //When a cost below 0 is given
        assertThrows(IllegalArgumentException.class, () -> new Impression("2015-01-01 12:00:02", "4620864431353610000L", "Male", "25-34", "High", "Blog", "-0.O00001"));
    }

    /**
     * Tests the compareTo() method
     */
    @Test
    void testCompareTo() {
        //TEST #1
        //Tests if -1 is returned
        //When the date of the given impression is greater than the date of the impression
        assertEquals(-1, this.impressionObjects.get(1).compareTo(new Impression("2015-01-01 12:00:03", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632")));

        //TEST #2
        //Tests if -1 is returned
        //When the date of the given impression is equal to the date of the impression
        assertEquals(0, this.impressionObjects.get(1).compareTo(new Impression("2015-01-01 12:00:02", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632")));

        //TEST #3
        //Tests if -1 is returned
        //When the date of the given impression is less than the date of the impression
        assertEquals(1, this.impressionObjects.get(1).compareTo(new Impression("2015-01-01 12:00:01", "4620864431353610000", "Male", "25-34", "High", "Blog", "0.001632")));
    }

    /**
     * Tests the getDate() method
     */
    @Test
    void testGetDate() {
        //TEST #1
        //Tests if the expected date is returned
        //When the impression has a date with the lowest possible year, month, day, hour, minute and second values
        assertTrue(this.impressionObjects.get(0).getDate().isEqualTo(new ModelDate(1000,1,1,0,0,0)));
    }

    /**
     * Tests the getID() method
     */
    @Test
    void testGetID() {
        //TEST #1
        //Tests if the expected ID is returned
        //When the impression has an ID with a value of 1
        assertEquals(1, this.impressionObjects.get(0).getID());

        //TEST #2
        //Tests if the expected ID is returned
        //When the impression has an ID that can only be stored by a long
        assertEquals(8530398223564990464L, this.impressionObjects.get(1).getID());
    }

    /**
     * Tests the getGender() method
     */
    @Test
    void testGetGender() {
        //TEST #1
        //Tests if the expected gender is returned
        //When the impression has a gender with the value "Male"
        assertEquals("Male", this.impressionObjects.get(0).getGender());

        //TEST #2
        //Tests if the expected gender is returned
        //When the impression has a gender with the value "Female"
        assertEquals("Female", this.impressionObjects.get(1).getGender());
    }

    /**
     * Tests the getAge() method
     */
    @Test
    void testGetAge() {
        //TEST #1
        //Tests if the expected age is returned
        //When the impression has an age with the value "<25"
        assertEquals("<25", this.impressionObjects.get(0).getAge());

        //TEST #2
        //Tests if the expected age is returned
        //When the impression has an age with the value "25-34"
        assertEquals("25-34", this.impressionObjects.get(1).getAge());

        //TEST #3
        //Tests if the expected age is returned
        //When the impression has an age with the value "35-44"
        assertEquals("35-44", this.impressionObjects.get(2).getAge());

        //TEST #4
        //Tests if the expected age is returned
        //When the impression has an age with the value "45-54"
        assertEquals("45-54", this.impressionObjects.get(3).getAge());

        //TEST #5
        //Tests if the expected age is returned
        //When the impression has an age with the value ">54"
        assertEquals(">54", this.impressionObjects.get(4).getAge());
    }

    /**
     * Tests the getIncome() method
     */
    @Test
    void testGetIncome() {
        //TEST #1
        //Tests if the expected income is returned
        //When the impression has an income with the value "Low"
        assertEquals("Low", this.impressionObjects.get(0).getIncome());

        //TEST #2
        //Tests if the expected income is returned
        //When the impression has an income with the value "Medium"
        assertEquals("Medium", this.impressionObjects.get(1).getIncome());

        //TEST #3
        //Tests if the expected income is returned
        //When the impression has an income with the value "High"
        assertEquals("High", this.impressionObjects.get(2).getIncome());
    }

    /**
     * Tests the getContext() method
     */
    @Test
    void testGetContext() {
        //TEST #1
        //Tests if the expected context is returned
        //When the impression has a context with the value "News"
        assertEquals("News", this.impressionObjects.get(0).getContext());

        //TEST #2
        //Tests if the expected context is returned
        //When the impression has a context with the value "Shopping"
        assertEquals("Shopping", this.impressionObjects.get(1).getContext());

        //TEST #3
        //Tests if the expected context is returned
        //When the impression has a context with the value "Social Media"
        assertEquals("Social Media", this.impressionObjects.get(2).getContext());

        //TEST #4
        //Tests if the expected context is returned
        //When the impression has a context with the value "Blog"
        assertEquals("Blog", this.impressionObjects.get(3).getContext());

        //TEST #5
        //Tests if the expected context is returned
        //When the impression has a context with the value "Hobbies"
        assertEquals("Hobbies", this.impressionObjects.get(4).getContext());

        //TEST #6
        //Tests if the expected context is returned
        //When the impression has a context with the value "Travel"
        assertEquals("Travel", this.impressionObjects.get(5).getContext());
    }

    /**
     * Tests the getCost() method
     */
    @Test
    void testGetCost() {
        //TEST #1
        //Tests if the expected cost is returned
        //When the impression has a cost with a value of 0
        assertEquals(0, this.impressionObjects.get(0).getCost());

        //TEST #2
        //Tests if the expected cost is returned
        //When the impression has a cost with a value that can only be stored by a double
        assertEquals(0.00171358, this.impressionObjects.get(1).getCost());
    }

    /**
     * Tests the getVisible() method
     */
    @Test
    void testGetVisible() {
        //TEST #1
        assertTrue(this.impressionObjects.get(0).getVisible());
    }

    /**
     * Tests the setVisible() method
     */
    @Test
    void testSetVisible() {
        //TEST #1
        //Tests if an impression's visible attribute stays true
        //When the Boolean true is given
        this.impressionObjects.get(0).setVisible(true);
        assertTrue(this.impressionObjects.get(0).getVisible());

        //TEST #2
        //Tests if an impression's visible attribute is changed to false
        //When the Boolean false is given
        this.impressionObjects.get(0).setVisible(false);
        assertFalse(this.impressionObjects.get(0).getVisible());

        //TEST #3
        //Tests if an impression's visible attribute stays false
        //When the Boolean false is given
        this.impressionObjects.get(0).setVisible(false);
        assertFalse(this.impressionObjects.get(0).getVisible());

        //TEST #4
        //Tests if an impression's visible attribute is changed to true
        //When the Boolean true is given
        this.impressionObjects.get(0).setVisible(true);
        assertTrue(this.impressionObjects.get(0).getVisible());
    }
}
