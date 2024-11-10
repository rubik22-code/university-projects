package uk.ac.soton.app.model.CSVDataUnits;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the Interaction class
 */
public class InteractionTest {

    //List of Interaction objects used to test some methods of the Interaction class
    List<Interaction> interactionObjects;

    /**
     * Sets up everything needed to test the Interaction class
     */
    @BeforeEach
    void setUp() {
        //Sets up a list of Impression objects to be used to test some methods of the Impression class
        this.interactionObjects = new ArrayList<>();
        this.interactionObjects.add(new Interaction("1000-01-01 00:00:00", "1",  "1000-01-01 00:00:00", "1", "Yes"));
        this.interactionObjects.add(new Interaction("2015-01-01 12:01:21", "8530398223564990464", "2015-01-01 12:05:13", "11", "No"));
        this.interactionObjects.add(new Interaction("2015-01-01 12:01:21", "8895519749317550000", "n/a", "7", "No"));
    }

    /**
     * Tests the Interaction() constructor
     */
    @Test
    void testInteraction() {
        //TEST #1
        //Tests if an IllegalArgumentException is thrown
        //When an invalid entry date is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-13-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #2
        //Tests if an IllegalArgumentException is thrown
        //When an entry date with no space between the date and time is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-0112:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #3
        //Tests if an IllegalArgumentException is thrown
        //When an entry date with a missing hyphen is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-0101 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #4
        //Tests if an IllegalArgumentException is thrown
        //When an entry date with a missing colon is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:0121", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #5
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2O15-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #6
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a year with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("0000-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #7
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a month with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-00-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #8
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a day with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-00 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #9
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a year with less than 4 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("215-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #10
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a year with more than 4 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("02015-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #11
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a month with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-1-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #12
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a month with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-001-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #13
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a day with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-1 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #14
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a day with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-001 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #15
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing an hour with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 1:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #16
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing an hour with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 012:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #17
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a minute with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:1:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #18
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a minute with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:001:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #19
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a second with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:2", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #20
        //Tests if an IllegalArgumentException is thrown
        //When an entry date containing a second with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:021", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #21
        //Tests if an IllegalArgumentException is thrown
        //When an ID containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "889551974931755O000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #22
        //Tests if an IllegalArgumentException is thrown
        //When an ID below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "0", "2015-01-01 12:05:13", "7", "No"));

        //TEST #23
        //Tests if an IllegalArgumentException is thrown
        //When an invalid exit date is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-13-01 12:05:13", "7", "No"));

        //TEST #24
        //Tests if an IllegalArgumentException is thrown
        //When an exit date with no space between the date and time is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-0112:05:13", "7", "No"));

        //TEST #25
        //Tests if an IllegalArgumentException is thrown
        //When an exit date with a missing hyphen is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-0101 12:05:13", "7", "No"));

        //TEST #26
        //Tests if an IllegalArgumentException is thrown
        //When an exit date with a missing colon is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:0513", "7", "No"));

        //TEST #27
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-O1-01 12:05:13", "7", "No"));

        //TEST #28
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a year with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "0000-01-01 12:05:13", "7", "No"));

        //TEST #29
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a month with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-00-01 12:05:13", "7", "No"));

        //TEST #30
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a day with a value below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-00 12:05:13", "7", "No"));

        //TEST #31
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a year with less than 4 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "215-01-01 12:05:13", "7", "No"));

        //TEST #32
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a year with more than 4 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "02015-01-01 12:05:13", "7", "No"));

        //TEST #33
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a month with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-1-01 12:05:13", "7", "No"));

        //TEST #34
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a month with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-001-01 12:05:13", "7", "No"));

        //TEST #35
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a day with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-1 12:05:13", "7", "No"));

        //TEST #36
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a day with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-001 12:05:13", "7", "No"));

        //TEST #37
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing an hour with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 1:05:13", "7", "No"));

        //TEST #38
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing an hour with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 012:05:13", "7", "No"));

        //TEST #39
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a minute with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:5:13", "7", "No"));

        //TEST #40
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a minute with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:005:13", "7", "No"));

        //TEST #41
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a second with less than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:1", "7", "No"));

        //TEST #42
        //Tests if an IllegalArgumentException is thrown
        //When an exit date containing a second with more than 2 digits is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:013", "7", "No"));

        //TEST #43
        //Tests if an IllegalArgumentException is thrown
        //When an entry date greater than the exit date given is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:05:14", "8895519749317550000", "2015-01-01 12:05:13", "7", "No"));

        //TEST #44
        //Tests if an IllegalArgumentException is thrown
        //When a pages viewed containing a letter is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "1O", "No"));

        //TEST #45
        //Tests if an IllegalArgumentException is thrown
        //When a pages viewed below 1 is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "0", "No"));

        //TEST #46
        //Tests if an IllegalArgumentException is thrown
        //When an unknown conversion is given
        assertThrows(IllegalArgumentException.class, () -> new Interaction("2015-01-01 12:01:21", "8895519749317550000", "2015-01-01 12:05:13", "7", "Other"));
    }

    /**
     * Tests the compareTo() method
     */
    @Test
    void testCompareTo() {
        //TEST #1
        //Tests if -1 is returned
        //When the date of the given interaction is greater than the date of the interaction
        assertEquals(-1, this.interactionObjects.get(1).compareTo(new Interaction("2015-01-01 12:01:22", "8895519749317550000","2015-01-01 12:05:13", "7", "No")));

        //TEST #2
        //Tests if -1 is returned
        //When the date of the given interaction is equal to the date of the interaction
        assertEquals(0, this.interactionObjects.get(1).compareTo(new Interaction("2015-01-01 12:01:21","8895519749317550000","2015-01-01 12:05:13", "7","No")));

        //TEST #3
        //Tests if -1 is returned
        //When the date of the given interaction is less than the date of the interaction
        assertEquals(1, this.interactionObjects.get(1).compareTo(new Interaction("2015-01-01 12:01:20","8895519749317550000", "2015-01-01 12:05:13", "7", "No")));
    }

    /**
     * Tests the getEntryDate() method
     */
    @Test
    void testGetEntryDate() {
        //TEST #1
        //Tests if the expected date is returned
        //When the interaction has an entry date with the lowest possible year, month, day, hour, minute and second values
        assertTrue(this.interactionObjects.get(0).getEntryDate().isEqualTo(new ModelDate(1000,1,1,0,0,0)));
    }

    /**
     * Tests the getID() method
     */
    @Test
    void testGetID() {
        //TEST #1
        //Tests if the expected ID is returned
        //When the interaction has an ID with a value of 1
        assertEquals(1, this.interactionObjects.get(0).getID());

        //TEST #2
        //Tests if the expected ID is returned
        //When the interaction has an ID that can only be stored by a long
        assertEquals(8530398223564990464L, this.interactionObjects.get(1).getID());
    }

    /**
     * Tests the getExitDate() method
     */
    @Test
    void testGetExitDate() {
        //TEST #1
        //Tests if the expected date is returned
        //When the interaction has an exit date with the lowest possible year, month, day, hour, minute and second values and equal to its entry date
        assertTrue(this.interactionObjects.get(0).getExitDate().isEqualTo(new ModelDate(1000,1,1,0,0,0)));

        //TEST #2
        //Tests if the expected date is returned
        //When the interaction has an exit date greater than its entry date
        assertTrue(this.interactionObjects.get(1).getExitDate().isEqualTo(new ModelDate(2015, 1, 1,12, 5, 13)));

        //TEST #3
        //Tests if null is returned
        //When the interaction's exit date is not available
        assertNull(interactionObjects.get(2).getExitDate());
    }

    /**
     * Tests the getPagesViewed() method
     */
    @Test
    void testGetPagesViewed() {
        //TEST #1
        //Tests if the expected pages viewed is returned
        //When the interaction has a pages viewed with a value of "1"
        assertEquals(1, this.interactionObjects.get(0).getPagesViewed());
    }

    /**
     * Tests the getConversion() method
     */
    @Test
    void testGetConversion() {
        //TEST #1
        //Tests if the conversion is returned
        //When the interaction has a conversion with the value "true"
        assertTrue(this.interactionObjects.get(0).getConversion());

        //TEST #2
        //Tests if the conversion is returned
        //When the interaction has a conversion with the value "false"
        assertFalse(this.interactionObjects.get(1).getConversion());

    }

    /**
     * Tests the getVisible() method
     */
    @Test
    void testGetVisible() {
        //TEST #1
        assertTrue(this.interactionObjects.get(0).getVisible());
    }

    /**
     * Tests the setVisible() method
     */
    @Test
    void testSetVisible() {
        //TEST #1
        //Tests if an interaction's visible attribute stays true
        //When the Boolean true is given
        this.interactionObjects.get(0).setVisible(true);
        assertTrue(this.interactionObjects.get(0).getVisible());

        //TEST #2
        //Tests if an interaction's visible attribute is changed to false
        //When the Boolean false is given
        this.interactionObjects.get(0).setVisible(false);
        assertFalse(this.interactionObjects.get(0).getVisible());

        //TEST #3
        //Tests if an interaction's visible attribute stays false
        //When the Boolean false is given
        this.interactionObjects.get(0).setVisible(false);
        assertFalse(this.interactionObjects.get(0).getVisible());

        //TEST #4
        //Tests if an interaction's visible attribute is changed to true
        //When the Boolean true is given
        this.interactionObjects.get(0).setVisible(true);
        assertTrue(this.interactionObjects.get(0).getVisible());
    }
}
