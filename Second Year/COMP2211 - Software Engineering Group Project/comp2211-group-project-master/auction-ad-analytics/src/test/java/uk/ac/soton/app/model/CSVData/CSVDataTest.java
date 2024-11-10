package uk.ac.soton.app.model.CSVData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.exceptions.InvalidLogFile;
import uk.ac.soton.app.exceptions.InvalidLogLine;
import uk.ac.soton.app.model.CSVDataUnits.Click;
import uk.ac.soton.app.model.CSVDataUnits.Impression;
import uk.ac.soton.app.model.CSVDataUnits.Interaction;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the CSVData class
 */
public class CSVDataTest {

    //List of CSVData objects used to test some methods of the CSVData class
    List<CSVData> csvDataObjects;

    //Expected output for tests #3 and #4 in the testGetImpressions() method
    List<Impression> expectedOutput1;

    //Expected output for tests #3 and #4 in the testGetClicks() method
    List<Click> expectedOutput2;

    //Expected output for tests #3 and #4 in the testGetInteractions() method
    List<Interaction> expectedOutput3;

    /**
     * Sets up everything needed to test the CSVData class
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     */
    @BeforeEach
    void setUp() throws InvalidLogFile, InvalidLogLine {
        //Sets up a list of CSVData objects to be used to test some methods of the CSVData class
        this.csvDataObjects = new ArrayList<>();
        this.csvDataObjects.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")));
        this.csvDataObjects.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog1.csv")));
        this.csvDataObjects.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog2.csv")));
        this.csvDataObjects.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog3.csv")));

        //Sets up the expected outputs for some tests in this class
        setUpTestExpectedOutputs();
    }

    /**
     * Sets up the expected outputs of the tests on the CSVData class
     */
    void setUpTestExpectedOutputs() {
        //Sets up the expected output for tests #3 and #4 in the testGetImpressions() method
        Impression impression1 = new Impression("2015-01-01 12:00:02", "4620864431353617408", "Male", "<25", "High", "Blog","0.001713");
        Impression impression2 = new Impression("2015-01-01 12:00:04", "3365479180556158976", "Female", "25-34", "Medium", "News","0.002762");
        Impression impression3 = new Impression("2015-01-01 12:00:05", "5239785226806161408", "Female", "35-44", "Medium", "Shopping","0.001632");
        Impression impression4 = new Impression("2015-01-01 12:00:06", "8530398223564990464", "Female", "45-54", "Medium", "Social Media","0.000000");
        Impression impression5 = new Impression("2015-01-01 12:00:10", "399593948382193664", "Male", ">54", "Low", "Travel","0.002064");
        Impression impression6 = new Impression("2015-01-01 12:00:11", "5694894591373382656", "Female", "<25", "Medium", "Hobbies","0.000000");
        this.expectedOutput1  = new ArrayList<>(Arrays.asList(impression1, impression2, impression3, impression4, impression5, impression6));

        //Sets up the expected output for tests #3 and #4 in the testGetImpressions() method
        Click click1 = new Click("2015-01-01 12:00:02", "4620864431353617408", "0.001713");
        Click click2 = new Click("2015-01-01 12:00:04", "3365479180556158976", "0.002762");
        Click click3 = new Click("2015-01-01 12:00:05", "5239785226806161408", "0.001632");
        Click click4 = new Click("2015-01-01 12:00:06", "8530398223564990464", "0.000000");
        Click click5 = new Click("2015-01-01 12:00:10", "399593948382193664", "0.002064");
        Click click6 = new Click("2015-01-01 12:00:11", "5694894591373382656", "0.000000");
        this.expectedOutput2 = new ArrayList<>(Arrays.asList(click1, click2, click3, click4, click5, click6));

        //Sets up the expected output for tests #3 and #4 in the testGetImpressions() method
        Interaction interaction1 = new Interaction("2015-01-01 12:00:02", "4620864431353617408", "2015-01-01 12:05:13", "7", "No");
        Interaction interaction2 = new Interaction("2015-01-01 12:00:04", "3365479180556158976","2015-01-01 12:02:01", "1", "No");
        Interaction interaction3 = new Interaction("2015-01-01 12:00:05", "5239785226806161408", "2015-01-01 12:05:19", "10", "No");
        Interaction interaction4 = new Interaction("2015-01-01 12:00:06", "8530398223564990464", "2015-01-01 12:06:30", "3", "Yes");
        Interaction interaction5 = new Interaction("2015-01-01 12:00:10", "399593948382193664", "n/a", "1", "No");
        Interaction interaction6 = new Interaction("2015-01-01 12:00:11", "5694894591373382657", "2015-01-01 12:05:48", "4", "Yes");
        this.expectedOutput3 = new ArrayList<>(Arrays.asList(interaction1, interaction2, interaction3, interaction4, interaction5, interaction6));
    }

    /**
     * Checks if two lists of impressions are identical or not
     * @param list1 list of impressions
     * @param list2 list of impressions
     * @return whether the two lists are identical or not
     */
    boolean assertImpressions(List<Impression> list1, List<Impression> list2) {
        if (list1.size() != list2.size()) return false;
        for (Impression impression : list1) {
            int currentIndex = list1.indexOf(impression);
            if(!(impression.getDate().isEqualTo(list2.get(currentIndex).getDate()))) return false;
            if (impression.getID() != list2.get(currentIndex).getID()) return false;
            if (!(impression.getGender().equals(list2.get(currentIndex).getGender()))) return false;
            if (!(impression.getAge().equals(list2.get(currentIndex).getAge()))) return false;
            if (!(impression.getIncome().equals(list2.get(currentIndex).getIncome()))) return false;
            if (!(impression.getContext().equals(list2.get(currentIndex).getContext()))) return false;
            if (impression.getCost() != list2.get(currentIndex).getCost()) return false;
            if (impression.getVisible() != list2.get(currentIndex).getVisible()) return false;
        }
        return true;
    }

    /**
     * Checks if two lists of clicks are identical or not
     * @param list1 list of clicks
     * @param list2 list of clicks
     * @return whether the two lists are identical or not
     */
    boolean assertClicks(List<Click> list1, List<Click> list2) {
        if (list1.size() != list2.size()) return false;
        for (Click click : list1) {
            int currentIndex = list1.indexOf(click);
            if (!(click.getDate().isEqualTo(list2.get(currentIndex).getDate()))) return false;
            if (click.getID() != list2.get(currentIndex).getID()) return false;
            if (click.getCost() != list2.get(currentIndex).getCost()) return false;
            if (click.getVisible() != list2.get(currentIndex).getVisible()) return false;
        }
        return true;
    }

    /**
     * Checks if two lists of interactions are identical or not
     * @param list1 list of interactions
     * @param list2 list of interactions
     * @return whether the two lists are identical or not
     */
    boolean assertInteractions(List<Interaction> list1, List<Interaction> list2) {
        if (list1.size() != list2.size()) return false;
        for (Interaction interaction : list1) {
            int currentIndex = list1.indexOf(interaction);
            if (!(interaction.getEntryDate().isEqualTo(list2.get(currentIndex).getEntryDate()))) return false;
            if (interaction.getID() != list2.get(currentIndex).getID()) return false;
            if (interaction.getExitDate() == null) {if (list2.get(currentIndex).getExitDate() != null) return false;}
            else {if (!(interaction.getExitDate().isEqualTo(list2.get(currentIndex).getExitDate()))) return false;}
            if (interaction.getPagesViewed() != list2.get(currentIndex).getPagesViewed()) return false;
            if (interaction.getConversion() != list2.get(currentIndex).getConversion()) return false;
            if (interaction.getVisible() != list2.get(currentIndex).getVisible()) return false;
        }
        return true;
    }

    /**
     * Checks if two sets of user IDs are identical or not
     * @param set1 set of user IDs
     * @param set2 set of user IDs
     * @return whether the two sets are identical or not
     */
    boolean assertIDs(Set<Long> set1, Set<Long> set2) {
        return set1.containsAll(set2) && set2.containsAll(set1) && (set1.size() == set2.size());
    }

    /**
     * Tests the CSVData() constructor
     * Also tests the readImpressionLog(), readClickLog() and readServerLog() methods indirectly
     */
    @Test
    void testCSVData() {
        //TEST #1
        //Tests if an InvalidLogFile exception is thrown
        //When no impression log is given
        assertThrows(InvalidLogFile.class, () -> new CSVData(new File(" "), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")));

        //TEST #2
        //Tests if an InvalidLogFile exception is thrown
        //When no click log is given
        assertThrows(InvalidLogFile.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File(" "), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")));

        //TEST #3
        //Tests if an InvalidLogFile exception is thrown
        //When no server log is given
        assertThrows(InvalidLogFile.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File(" ")));

        //TEST #4
        //Tests if an InvalidLogLine exception is thrown
        //When an impression log with a missing column is given
        assertThrows(InvalidLogLine.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog5.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")));

        //TEST #5
        //Tests if an InvalidLogLine exception is thrown
        //When a click log with a missing column is given
        assertThrows(InvalidLogLine.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog5.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")));

        //TEST #6
        //Tests if an InvalidLogLine exception is thrown
        //When a server log with a missing column is given
        assertThrows(InvalidLogLine.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog5.csv")));

        //TEST #7
        //Tests if an InvalidLogLine exception is thrown
        //When an impression log with an extra column is given
        assertThrows(InvalidLogLine.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog6.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")));

        //TEST #8
        //Tests if an InvalidLogLine exception is thrown
        //When a click log with an extra column is given
        assertThrows(InvalidLogLine.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog6.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")));

        //TEST #9
        //Tests if an InvalidLogLine exception is thrown
        // When a server log with an extra column is given
        assertThrows(InvalidLogLine.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog6.csv")));

        //TEST #10
        //Tests if an InvalidLogLine exception is thrown
        //When an impression log with a row containing a value in an invalid format is given
        assertThrows(InvalidLogLine.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog7.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/emptyCSVFile.csv")));

        //TEST #11
        //Tests if an InvalidLogLine exception is thrown
        //When a click log with a row containing a value in an invalid format is given
        assertThrows(InvalidLogLine.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog7.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/emptyCSVFile.csv")));

        //TEST #12
        //Tests if an InvalidLogLine exception is thrown
        //When a server log with a row containing a value in an invalid format is given
        assertThrows(InvalidLogLine.class, () -> new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog7.csv")));
    }

    /**
     * Tests the getImpressions() method
     * Also tests the readImpressionLog() method indirectly
     */
    @Test
    void testGetImpressions() {
        //TEST #1
        //Tests if an empty list of impressions is returned
        //When the CSVData object was initialised using an empty impression log
        assertTrue(csvDataObjects.get(0).getImpressions().isEmpty());

        //TEST #2
        //Tests if an empty list of impressions is returned
        //When the CSVData object was initialised using an impression log with only the header
        assertTrue(csvDataObjects.get(1).getImpressions().isEmpty());

        //TEST #3
        //Tests if the expected list of impressions is returned
        //When the CSVData object was initialised using an impression log with unsorted impressions
        assertTrue(assertImpressions(expectedOutput1, csvDataObjects.get(2).getImpressions()));

        //TEST #4
        //Tests if the expected list of impressions is returned
        //When the CSVData object was initialised using an impression log with sorted impressions
        assertTrue(assertImpressions(expectedOutput1, csvDataObjects.get(3).getImpressions()));
    }

    /**
     * Tests the getClicks() method
     * Also tests the readClickLog() method indirectly
     */
    @Test
    void testGetClicks() {
        //TEST #1
        //Tests if an empty list of clicks is returned
        //When the CSVData object was initialised using an empty click log
        assertTrue(csvDataObjects.get(0).getClicks().isEmpty());

        //TEST #2
        //Tests if an empty list of clicks is returned
        //When the CSVData object was initialised using a click log with only the header
        assertTrue(csvDataObjects.get(1).getClicks().isEmpty());

        //TEST #3
        //Tests if the expected list of clicks is returned
        //When the CSVData object was initialised using a click log with unsorted clicks
        assertTrue(assertClicks(expectedOutput2, csvDataObjects.get(2).getClicks()));

        //TEST #4
        //Tests if the expected list of clicks is returned
        //When the CSVData object was initialised using a click log with sorted clicks
        assertTrue(assertClicks(expectedOutput2, csvDataObjects.get(3).getClicks()));
    }

    /**
     * Tests the getInteractions() method
     * Also tests the readServerLog() method indirectly

     */
    @Test
    void testGetInteractions() {
        //TEST #1
        //Tests if an empty list of interactions is returned
        //When the CSVData object was initialised using an empty server log
        assertTrue(csvDataObjects.get(0).getInteractions().isEmpty());

        //TEST #2
        //Tests if an empty list of interactions is returned
        //When the CSVData object was initialised using a server log with only the header
        assertTrue(csvDataObjects.get(1).getInteractions().isEmpty());

        //TEST #3
        //Tests if the expected list of interactions is returned
        //When the CSVData object was initialised using a server log with unsorted interactions
        assertTrue(assertInteractions(expectedOutput3, csvDataObjects.get(2).getInteractions()));

        //TEST #4
        //Tests if the expected list of interactions is returned
        //When the CSVData object was initialised using a server log with sorted interactions
        assertTrue(assertInteractions(expectedOutput3, csvDataObjects.get(3).getInteractions()));
    }

    /**
     * Tests the getFirstDate() method
     * Also tests the storeFirstDate() method indirectly
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     */
    @Test
    void testGetFirstDate() throws InvalidLogFile, InvalidLogLine {
        //TEST #1
        //Tests if null is returned
        //When there are no impressions, clicks or interactions
        assertNull(csvDataObjects.get(0).getFirstDate());

        //TEST #2
        //Tests if the expected date is returned
        //When the earliest date is of an impression
        CSVData csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog4.csv"));
        assertTrue(csvData.getFirstDate().isEqualTo(new ModelDate(2015, 1, 1, 12, 0, 2)));

        //TEST #3
        //Tests if the expected date is returned
        //When the earliest date is of a click
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog4.csv"));
        assertTrue(csvData.getFirstDate().isEqualTo(new ModelDate(2015, 1, 1, 12, 0, 2)));

        //TEST #4
        //Tests if the expected date is returned
        //When the earliest date is of an interaction
         csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog3.csv"));
         assertTrue(csvData.getFirstDate().isEqualTo(new ModelDate(2015, 1, 1, 12, 0, 2)));
    }

    /**
     * Tests the getLastDate() method
     * Also tests the storeLastDate() method indirectly
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     */
    @Test
    void testGetLastDate() throws InvalidLogFile, InvalidLogLine {
        //TEST #1
        //Tests if null is returned when there are no impressions, clicks or interactions
        assertNull(csvDataObjects.get(0).getLastDate());

        //TEST #2
        //Tests if the expected date is returned
        //When the latest date is of an impression
        CSVData csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog3.csv"));
        assertTrue(csvData.getLastDate().isEqualTo(new ModelDate(2015, 1, 1, 12, 4, 11)));

        //TEST #3
        //Tests if the expected date is returned
        //When the latest date is of a click
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog3.csv"));
        assertTrue(csvData.getLastDate().isEqualTo(new ModelDate(2015, 1, 1, 12, 4, 11)));

        //TEST #4
        //Tests if the expected date is returned
        //When the latest date is of an interactions
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ImpressionLogs/ImpressionLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog4.csv"));
        assertTrue(csvData.getLastDate().isEqualTo(new ModelDate(2015, 1, 1, 12, 4, 11)));
    }

    /**
     * Tests the getUserIDs() method
     * Also tests the readClickLog() and readServerLog() methods indirectly
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     */
    @Test
    void testGetUserIDs() throws InvalidLogFile, InvalidLogLine {
        //TEST #1
        //Tests if the expected set of user IDs is returned
        //When the csvData object contains clicks and interactions
        Set<Long> expectedOutput = new HashSet<>(Arrays.asList(4620864431353617408L, 3365479180556158976L, 5239785226806161408L, 8530398223564990464L, 399593948382193664L,5694894591373382656L, 5694894591373382657L));
        assertTrue(assertIDs(expectedOutput, csvDataObjects.get(3).getUserIDs()));

        //TEST #2
        //Tests if the expected set of user IDs is returned
        //When the csvData object contains clicks but no interactions
        expectedOutput = new HashSet<>(Arrays.asList(4620864431353617408L, 3365479180556158976L, 5239785226806161408L, 8530398223564990464L, 399593948382193664L,5694894591373382656L));
        CSVData csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ClickLogs/ClickLog3.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"));
        assertTrue(assertIDs(expectedOutput, csvData.getUserIDs()));

        //TEST #3
        //Tests if the expected set of user IDs is returned
        //When the csvData object contains interactions but no clicks
        expectedOutput = new HashSet<>(Arrays.asList(4620864431353617408L, 3365479180556158976L, 5239785226806161408L, 8530398223564990464L, 399593948382193664L,5694894591373382657L));
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVData/ServerLogs/ServerLog3.csv"));
        assertTrue(assertIDs(expectedOutput, csvData.getUserIDs()));

        //TEST #4
        //Tests if the expected set of user IDs is returned
        //When the csvData object contains no clicks and interactions
        assertTrue(csvDataObjects.get(0).getUserIDs().isEmpty());
    }
}
