package uk.ac.soton.app.model.CSVDataUnits;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Class used to test the Click class
 */
public class ClickTest {

    //List of Click objects used to test some methods of the Click class
    List<Click> clickObjects;

    /**
     * Sets up everything needed to test the Click class
     */
    @BeforeEach
    void setUp() {
        //Sets up a list of Click objects to be used to test some methods of the Click class
        this.clickObjects = new ArrayList<>();
        this.clickObjects.add(new Click("1000-01-01 00:00:00", "1", "0.000000"));
        this.clickObjects.add(new Click("2015-01-01 12:00:02", "8530398223564990464", "0.00171358"));
    }

    /**
     * Tests the Click() constructor
     */
    @Test
    void testClick() {
        //TEST #1
        //Tests if an IllegalArgumentException is thrown
        //When an invalid date is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-13-01 12:00:02", "4620864431353610000", "0.001632"));

        //TEST #2
        //Tests if an IllegalArgumentException is thrown
        //When a date with no space between the date and time is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-0112:00:02", "4620864431353610000","0.001632"));

        //TEST #3
        //Tests if an IllegalArgumentException is thrown
        //When a date with a missing hyphen is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-0101 12:00:02", "4620864431353610000","0.001632"));

        //TEST #4
        //Tests if an IllegalArgumentException is thrown
        //When a date with a missing colon is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 12:0002", "4620864431353610000","0.001632"));

        //TEST #5
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2O15-01-01 12:00:02", "4620864431353610000","0.001632"));

        //TEST #6
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a year with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Click("0000-01-01 12:00:02", "4620864431353610000", "0.001632"));

        //TEST #7
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a month with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-00-01 12:00:02", "4620864431353610000", "0.001632"));

        //TEST #8
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a day with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-00 12:00:02", "4620864431353610000", "0.001632"));

        //TEST #9
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a year with less than 4 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("215-01-01 12:00:02", "4620864431353610000","0.001632"));

        //TEST #10
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a year with more than 4 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("02015-01-01 12:00:02", "4620864431353610000","0.001632"));

        //TEST #11
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a month with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-1-01 12:00:02", "4620864431353610000", "0.001632"));

        //TEST #12
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a month with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-001-01 12:00:02", "4620864431353610000","0.001632"));

        //TEST #13
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a day with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-1 12:00:02", "4620864431353610000", "0.001632"));

        //TEST #14
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a day with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-001 12:00:02", "4620864431353610000", "0.001632"));

        //TEST #15
        //Tests if an IllegalArgumentException is thrown
        //When a date containing an hour with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 1:00:02", "4620864431353610000", "0.001632"));

        //TEST #16
        //Tests if an IllegalArgumentException is thrown
        //When a date containing an hour with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 012:00:02", "4620864431353610000","0.001632"));

        //TEST #17
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a minute with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 12:0:02", "4620864431353610000","0.001632"));

        //TEST #18
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a minute with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 12:000:02", "4620864431353610000","0.001632"));

        //TEST #19
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a second with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 12:00:2", "4620864431353610000","0.001632"));

        //TEST #20
        //Tests if an IllegalArgumentException is thrown
        //When a date containing a second with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 12:00:002", "4620864431353610000","0.001632"));

        //TEST #21
        //Tests if an IllegalArgumentException is thrown
        //When an ID containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 12:00:02", "462O864431353610000","0.001632"));

        //TEST #22
        //Tests if an IllegalArgumentException is thrown
        //When an ID below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 12:00:02", "0","0.001632"));

        //TEST #23
        //Tests if an IllegalArgumentException is thrown
        //When a cost containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 12:00:02", "4620864431353610000L","0.O01632"));

        //TEST #24
        //Tests if an IllegalArgumentException is thrown
        //When a cost below 0 is given
        assertThrows(IllegalArgumentException.class, () -> new Click("2015-01-01 12:00:02", "4620864431353610000L","-0.O00001"));
    }

    /**
     * Tests the compareTo() method
     */
    @Test
    void testCompareTo() {
        //TEST #1
        //Tests if -1 is returned
        //When the date of the given click is greater than the date of the click
        assertEquals(-1, this.clickObjects.get(1).compareTo(new Click("2015-01-01 12:00:03", "4620864431353610000", "11.794442")));

        //TEST #2
        //Tests if -1 is returned
        //When the date of the given click is equal to the date of the click
        assertEquals(0, this.clickObjects.get(1).compareTo(new Click("2015-01-01 12:00:02", "4620864431353610000", "11.794442")));

        //TEST #3
        //Tests if -1 is returned
        //When the date of the given click is less than the date of the click
        assertEquals(1, this.clickObjects.get(1).compareTo(new Click("2015-01-01 12:00:01", "4620864431353610000", "11.794442")));
    }

    /**
     * Tests the getDate() method
     */
    @Test
    void testGetDate() {
        //TEST #1
        //Tests if the expected date is returned
        //When the click has a date with the lowest possible year, month, day, hour, minute and second values
        assertTrue(this.clickObjects.get(0).getDate().isEqualTo(new ModelDate(1000,1,1,0,0,0)));
    }

    /**
     * Tests the getID() method
     */
    @Test
    void testGetID() {
        //TEST #1
        //Tests if the expected ID is returned
        //When the click has an ID with a value of 1
        assertEquals(1, this.clickObjects.get(0).getID());

        //TEST #2
        //Tests if the expected ID is returned
        //When the click has an ID that can only be stored by a long
        assertEquals(8530398223564990464L, this.clickObjects.get(1).getID());
    }

    /**
     * Tests the getCost() method
     */
    @Test
    void testCost() {
        //TEST #1
        //Tests if the expected cost is returned
        //When the click has a cost with a value of 0
        assertEquals(0, this.clickObjects.get(0).getCost());

        //TEST #2
        //Tests if the expected cost is returned
        //When the click has a cost with a value that can only be stored by a double
        assertEquals(0.00171358, this.clickObjects.get(1).getCost());
    }

    /**
     * Tests the getVisible() method
     */
    @Test
    void testGetVisible() {
        //TEST #1
        assertTrue(this.clickObjects.get(0).getVisible());
    }

    /**
     * Tests the setVisible() method
     */
    @Test
    void testSetVisible() {
        //TEST #1
        //Tests if a click's visible attribute stays true
        //When the Boolean true is given
        this.clickObjects.get(0).setVisible(true);
        assertTrue(this.clickObjects.get(0).getVisible());

        //TEST #2
        //Tests if a click's visible attribute is changed to false
        //When the Boolean false is given
        this.clickObjects.get(0).setVisible(false);
        assertFalse(this.clickObjects.get(0).getVisible());

        //TEST #3
        //Tests if a click's visible attribute stays false
        //When the Boolean false is given
        this.clickObjects.get(0).setVisible(false);
        assertFalse(this.clickObjects.get(0).getVisible());

        //TEST #4
        //Tests if a click's visible attribute is changed to true
        //When the Boolean true is given
        this.clickObjects.get(0).setVisible(true);
        assertTrue(this.clickObjects.get(0).getVisible());
    }
}
