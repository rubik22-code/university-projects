package uk.ac.soton.app.model.ProcessedData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.exceptions.InvalidLogFile;
import uk.ac.soton.app.exceptions.InvalidLogLine;
import uk.ac.soton.app.model.CSVData.CSVData;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the Metrics class
 */
public class MetricsTest {

    //List of Metrics objects used to test some methods of the Metrics class
    List<Metrics> metricsObjects;

    /**
     * Sets up everything needed to test the Metrics class
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     */
    @BeforeEach
    void setUp() throws InvalidLogFile, InvalidLogLine {
        //Sets up a list of Metrics objects to be used to test some methods of the Metrics class
        this.metricsObjects = new ArrayList<>();

        CSVData csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"));
        this.metricsObjects.add(new Metrics(csvData, 1, new ModelDate(0, 0, 0, 0, 0, 0)));

        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ClickLogs/ClickLog.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ServerLogs/ServerLog.csv"));
        this.metricsObjects.add(new Metrics(csvData, 1, new ModelDate(0, 0, 0, 0, 0, 0)));
        this.metricsObjects.add(new Metrics(csvData, 2, new ModelDate(0, 0, 0, 0, 0, 0)));
        this.metricsObjects.add(new Metrics(csvData, 1, new ModelDate(0, 0, 0, 0, 0, 1)));
        this.metricsObjects.add(new Metrics(csvData, 6, new ModelDate(0, 0, 0, 0, 2, 0)));

        csvData.getImpressions().get(2).setVisible(false);
        csvData.getImpressions().get(7).setVisible(false);
        csvData.getImpressions().get(8).setVisible(false);
        csvData.getClicks().get(1).setVisible(false);
        csvData.getClicks().get(6).setVisible(false);
        csvData.getClicks().get(7).setVisible(false);
        csvData.getInteractions().get(1).setVisible(false);
        csvData.getInteractions().get(6).setVisible(false);
        csvData.getInteractions().get(7).setVisible(false);
        this.metricsObjects.add(new Metrics(csvData, 2, new ModelDate(0, 0, 0, 0, 0, 0)));

        for (int i = 0; i < 12; i++) {csvData.getImpressions().get(i).setVisible(false);}
        for (int i = 0; i < 10; i++) {csvData.getClicks().get(i).setVisible(false);}
        for (int i = 0; i < 10; i++) {csvData.getInteractions().get(i).setVisible(false);}
        this.metricsObjects.add(new Metrics(csvData, 1, new ModelDate(0, 0, 0, 0, 0, 0)));

        this.metricsObjects.add(new Metrics(new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ClickLogs/ClickLog.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ServerLogs/ServerLog.csv")), 5, new ModelDate(0, 0, 0, 0, 0, 0)));
        this.metricsObjects.add(new Metrics(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ServerLogs/ServerLog.csv")), 5, new ModelDate(0, 0, 0, 0, 0, 0)));
        this.metricsObjects.add(new Metrics(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ClickLogs/ClickLog.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")), 5, new ModelDate(0, 0, 0, 0, 0, 0)));
        this.metricsObjects.add(new Metrics(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ImpressionLogs/ImpressionLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ClickLogs/ClickLog.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/Metrics/ServerLogs/ServerLog.csv")), 5, new ModelDate(0, 0, 0, 0, 0, 0)));

    }

    /**
     * Tests the checkIfBounce() method
     */
    @Test
    void testCheckIfBounce() {
        //TEST #1
        //Tests if false is returned
        //When the exit date is null and there is no minimum time required to not be a bounce
        assertFalse(metricsObjects.get(0).checkIfBounce(new ModelDate(2015, 1, 1, 12, 1, 21), null, new ModelDate(0, 0, 0, 0, 0, 0)));

        //TEST #2
        //Tests if true is returned
        //When the exit date is null and there is a minimum time required to not be a bounce
        assertTrue(metricsObjects.get(0).checkIfBounce(new ModelDate(2015, 1, 1, 12, 1, 21), null, new ModelDate(0, 0, 0, 0, 0, 1)));

        //TEST #3
        //Tests if true is returned
        //When the duration of the interaction is below the minimum time required to not be a bounce
        assertTrue(metricsObjects.get(0).checkIfBounce(new ModelDate(2015, 1, 1, 12, 1, 21), new ModelDate(2015, 1, 1, 12, 2, 21), new ModelDate(0, 0, 0, 0, 2, 0)));

        //TEST #4
        //Tests if false is returned
        //When the duration of the interaction is equal to the minimum time required to not be a bounce
        assertFalse(metricsObjects.get(0).checkIfBounce(new ModelDate(2015, 1, 1, 12, 1, 21), new ModelDate(2015, 1, 1, 12, 3, 21), new ModelDate(0, 0, 0, 0, 2, 0)));

        //TEST #5
        //Tests if false is returned
        //When the duration of the interaction is above the minimum time required to not be a bounce
        assertFalse(metricsObjects.get(0).checkIfBounce(new ModelDate(2015, 1, 1, 12, 1, 21), new ModelDate(2015, 1, 1, 12, 4, 21), new ModelDate(0, 0, 0, 0, 2, 0)));
    }

    /**
     * Tests the getImpressions() method
     * Also tests the calculateUniquesAndTotalCost() method indirectly
     */
    @Test
    void testGetImpressions() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no impressions
        assertEquals(0, metricsObjects.get(0).getImpressions());

        //TEST #2
        //Tests if 12 is returned
        //When there are impressions
        assertEquals(12, metricsObjects.get(1).getImpressions());

        //TEST #3
        //Tests if 9 is returned
        //When some impressions have been filtered out
        assertEquals(9, metricsObjects.get(5).getImpressions());

        //TEST #4
        //Tests if 0 is returned
        //When all impressions have been filtered out
        assertEquals(0, metricsObjects.get(6).getImpressions());
    }

    /**
     * Tests the getClicks() method
     * Also tests the calculateUniquesAndTotalCost() method indirectly
     */
    @Test
    void testGetClicks() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no clicks
        assertEquals(0, metricsObjects.get(0).getClicks());

        //TEST #2
        //Tests if 10 is returned
        //When there are clicks
        assertEquals(10, metricsObjects.get(1).getClicks());

        //TEST #3
        //Tests if 7 is returned
        //When some clicks have been filtered out
        assertEquals(7, metricsObjects.get(5).getClicks());

        //TEST #4
        //Tests if 0 is returned
        //When all clicks have been filtered out
        assertEquals(0, metricsObjects.get(6).getClicks());
    }

    /**
     * Tests the getUniques() method
     * Also tests the calculateUniquesAndTotalCost() method indirectly
     */
    @Test
    void testGetUniques() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no clicks
        assertEquals(0, metricsObjects.get(0).getUniques());

        //TEST #2
        //Tests if 9 is returned
        //When there are unique and non-unique clicks
        assertEquals(9, metricsObjects.get(1).getUniques());

        //TEST #3
        //Tests if 7 is returned
        //When a unique click has been filtered out
        assertEquals(7, metricsObjects.get(5).getUniques());

        //TEST #4
        //Tests if 0 output is returned
        //When all clicks have been filtered out
        assertEquals(0, metricsObjects.get(6).getUniques());
    }

    /**
     * Tests the getBounces() method
     * Also tests the calculateUniquesAndTotalCost() method indirectly
     */
    @Test
    void testGetBounces() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no interactions
        assertEquals(0, metricsObjects.get(0).getBounces());

        //TEST #2
        //Tests if an interaction with an "n/a" exit date is not counted as a bounce
        //When its number of pages viewed is above the minimum amount required to not be a bounce and if there is no minimum amount of time required to not be a bounce
        assertEquals(0, metricsObjects.get(1).getBounces());

        //TEST #3
        //Tests if an interactions with an "N/A" exit date is counted as a bounce
        //When its number of pages viewed is below the minimum amount required to not be a bounce and if there is no minimum amount of time required to not be a bounce
        assertEquals(1, metricsObjects.get(2).getBounces());

        //TEST #4
        //Tests if an interactions with an "N/A" exit date is counted as a bounce
        //When its number of pages viewed is equal to the minimum amount required to not be a bounce and if there is a minimum amount of time required to not be a bounce
        assertEquals(1, metricsObjects.get(3).getBounces());

        //TEST #5
        //Tests if 6 is returned
        //When some interactions are below, equal to and above the minimum amount of time required to not be a bounce
        //As well as below, equal to and above the minimum amount of pages required to not be a bounce
        assertEquals(6, metricsObjects.get(4).getBounces());

        //TEST #6
        //Tests if 1 is returned
        //When some interactions have been filtered out
        assertEquals(1, metricsObjects.get(5).getBounces());

        //TEST #7
        //Tests if 0 is returned
        //When all interactions have been filtered out
        assertEquals(0, metricsObjects.get(6).getBounces());
    }

    /**
     * Tests the getConversions() method
     */
    @Test
    void testGetConversions() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no interactions
        assertEquals(0, metricsObjects.get(0).getConversions());

        //TEST #2
        //Tests if 5 is returned
        //When there are interactions
        assertEquals(5, metricsObjects.get(1).getConversions());

        //TEST #5
        //Tests if 4 is returned
        //When some interactions have been filtered out
        assertEquals(4, metricsObjects.get(5).getConversions());

        //TEST #6
        //Tests if 0 is returned
        //When all interactions have been filtered out
        assertEquals(0, metricsObjects.get(6).getConversions());
    }

    /**
     * Tests the getTotalCost() method
     * Also tests the calculateUniquesAndTotalCost() method indirectly
     */
    @Test
    void testGetTotalCost() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no impressions and clicks
        assertEquals(0, metricsObjects.get(0).getTotalCost());

        //TEST #2
        //Tests if 0.0.57270113 is returned
        //When there are impressions and clicks
        assertEquals(0.57270113, metricsObjects.get(1).getTotalCost());

        //TEST #3
        //Tests if 0.0.45545381 is returned
        //When some impressions and clicks have been filtered out
        assertEquals(0.45545381, metricsObjects.get(5).getTotalCost());

        //TEST #4
        //Tests if 0 is returned
        //When all impressions and clicks have been filtered out
        assertEquals(0, metricsObjects.get(6).getTotalCost());
    }

    /**
     * Tests the getCTR() method
     * Also tests the calculateRemainingMetrics() method indirectly
     */
    @Test
    void testGetCTR() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no impressions
        assertEquals(0, metricsObjects.get(7).getCTR());

        //TEST #2
        //Tests if 0.0.8333333134651184 is returned
        //When there are impressions
        assertEquals(0.8333333134651184, metricsObjects.get(4).getCTR());
    }

    /**
     * Tests the getCPA() method
     * Also tests the calculateRemainingMetrics() method indirectly
     */
    @Test
    void testGetCPA() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no interactions
        assertEquals(0, metricsObjects.get(9).getCPA());

        //TEST #2
        //Tests if 0.0.1145402267575264 is returned
        //When there are interactions
        assertEquals(0.1145402267575264, metricsObjects.get(4).getCPA());
    }

    /**
     * Tests the getCPC() method
     * Also tests the calculateRemainingMetrics() method indirectly
     */
    @Test
    void testGetCPC() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no clicks
        assertEquals(0, metricsObjects.get(8).getCPC());

        //TEST #2
        //Tests if 0.0.0572701133787632 is returned
        //When there are clicks
        assertEquals(0.0572701133787632, metricsObjects.get(4).getCPC());
    }

    /**
     * Tests the getCPM() method
     * Also tests the calculateRemainingMetrics() method indirectly
     */
    @Test
    void testGetCPM() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no impressions
        assertEquals(0, metricsObjects.get(7).getCPM());

        //TEST #2
        //Tests if 0.0.5861625075340271 is returned
        //When there are impressions
        assertEquals(0.5861625075340271, metricsObjects.get(10).getCPM());
    }

    /**
     * Tests the getBounceRate() method
     * Also tests the calculateRemainingMetrics() method indirectly
     */
    @Test
    void testGetBounceRate() {
        //TEST #1
        //Tests if 0 is returned
        //When there are no clicks
        assertEquals(0, metricsObjects.get(9).getBounceRate());

        //TEST #2
        //Tests if 0.0.6000000238418579 is returned
        //When there are clicks
        assertEquals(0.6000000238418579, metricsObjects.get(4).getBounceRate());
    }
}