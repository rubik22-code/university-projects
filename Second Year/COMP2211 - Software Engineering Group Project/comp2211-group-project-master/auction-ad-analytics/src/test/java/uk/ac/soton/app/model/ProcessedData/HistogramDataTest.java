package uk.ac.soton.app.model.ProcessedData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.exceptions.InvalidLogFile;
import uk.ac.soton.app.exceptions.InvalidLogLine;
import uk.ac.soton.app.model.CSVData.CSVData;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the HistogramData class
 */
public class HistogramDataTest {

    //List of HistogramData objects used to test some methods of the HistogramData class
    List<HistogramData> histogramDataObjects;

    /**
     * Sets up everything needed to test the HistogramData class
     */
    @BeforeEach
    void setUp() throws InvalidLogFile, InvalidLogLine {
        //Sets up a list of HistogramData objects to be used to test some methods of the HistogramData class
        this.histogramDataObjects = new ArrayList<>();
        this.histogramDataObjects.add(new HistogramData(new ArrayList<>(), 5));

        CSVData csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/HistogramData/ImpressionLogs/impressionLog.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/HistogramData/ClickLogs/ClickLog.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/HistogramData/ServerLogs/serverLog.csv"));
        this.histogramDataObjects.add(new HistogramData(csvData.getClicks(), 4));

        csvData.getClicks().get(0).setVisible(false);
        csvData.getClicks().get(1).setVisible(false);
        csvData.getClicks().get(5).setVisible(false);
        this.histogramDataObjects.add(new HistogramData(csvData.getClicks(), 4));

        for (int i = 0; i < 8; i++) {csvData.getClicks().get(i).setVisible(false);}
        this.histogramDataObjects.add(new HistogramData(csvData.getClicks(), 4));

        csvData.getClicks().remove(6);
        csvData.getClicks().remove(5);
        csvData.getClicks().remove(3);
        csvData.getClicks().remove(1);
        csvData.getClicks().remove(0);
        csvData.getClicks().get(0).setVisible(true);
        csvData.getClicks().get(1).setVisible(true);
        csvData.getClicks().get(2).setVisible(true);
        this.histogramDataObjects.add(new HistogramData(csvData.getClicks(), 4));
    }

    /**
     * Tests the getCostDistribution() method
     * Also tests the calculateUniquesAndTotalCost() method indirectly
     */
    @Test
    void testGetCostDistribution() {
        //TEST #1
        //Tests if the expected list of cost range upper bounds is returned
        //When there are no clicks
        List<Double> expectedOutput = new ArrayList<>(Arrays.asList(0.01, 0.02, 0.03, 0.04, 0.05));
        boolean outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(0).getCostDistribution().size();
        boolean actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(0).getCostDistribution());
        boolean expectedOutputMatches = histogramDataObjects.get(0).getCostDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);

        //TEST #2
        //Tests if the expected list of cost range upper bounds is returned
        //When there are clicks
        expectedOutput = new ArrayList<>(Arrays.asList(0.06, 0.12, 0.18, 0.24));
        outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(1).getCostDistribution().size();
        actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(1).getCostDistribution());
        expectedOutputMatches = histogramDataObjects.get(1).getCostDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);

        //TEST #3
        //Tests if the expected list of cost range upper bounds is returned
        //When some clicks are filtered out
        expectedOutput = new ArrayList<>(Arrays.asList(0.02, 0.04, 0.06, 0.08));
        outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(2).getCostDistribution().size();
        actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(2).getCostDistribution());
        expectedOutputMatches = histogramDataObjects.get(2).getCostDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);

        //TEST #4
        //Tests if the expected list of cost range upper bounds is returned
        //When all clicks are filtered out
        expectedOutput = new ArrayList<>(Arrays.asList(0.01, 0.02, 0.03, 0.04));
        outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(3).getCostDistribution().size();
        actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(3).getCostDistribution());
        expectedOutputMatches = histogramDataObjects.get(3).getCostDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);

        //TEST #5
        //Tests if the expected list of cost range upper bounds is returned
        //When the highest click cost is 0
        expectedOutput = new ArrayList<>(Arrays.asList(0.01, 0.02, 0.03, 0.04));
        outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(4).getCostDistribution().size();
        actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(4).getCostDistribution());
        expectedOutputMatches = histogramDataObjects.get(4).getCostDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);
    }

    /**
     * Tests the getFrequencyDistribution() method
     * Also tests the calculateFrequencyDistribution() method indirectly
     */
    @Test
    void testGetFrequencyDistribution() {
        //TEST #1
        //Tests if the expected list of cost range frequencies is returned
        //When there are no clicks
        List<Integer> expectedOutput = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
        boolean outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(0).getFrequencyDistribution().size();
        boolean actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(0).getFrequencyDistribution());
        boolean expectedOutputMatches = histogramDataObjects.get(0).getFrequencyDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);

        //TEST #2
        //Tests if the expected list of cost range frequencies is returned
        //When there are clicks
        expectedOutput = new ArrayList<>(Arrays.asList(5, 1, 0, 2));
        outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(1).getFrequencyDistribution().size();
        actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(1).getFrequencyDistribution());
        expectedOutputMatches = histogramDataObjects.get(1).getFrequencyDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);

        //TEST #3
        //Tests if the expected list of cost range frequencies is returned
        //When some clicks are filtered out
        expectedOutput = new ArrayList<>(Arrays.asList(4, 0, 0, 1));
        outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(2).getFrequencyDistribution().size();
        actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(2).getFrequencyDistribution());
        expectedOutputMatches = histogramDataObjects.get(2).getFrequencyDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);

        //TEST #4
        //Tests if the expected list of cost range frequencies is returned
        //When all clicks are filtered out
        expectedOutput = new ArrayList<>(Arrays.asList(0, 0, 0, 0));
        outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(3).getFrequencyDistribution().size();
        actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(3).getFrequencyDistribution());
        expectedOutputMatches = histogramDataObjects.get(3).getFrequencyDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);

        //TEST #5
        //Tests if the expected list of cost range frequencies is returned
        //When the highest click cost is 0
        expectedOutput = new ArrayList<>(Arrays.asList(3, 0, 0, 0));
        outputsSizeMatch = expectedOutput.size() == histogramDataObjects.get(4).getFrequencyDistribution().size();
        actualOutputMatches = expectedOutput.containsAll(histogramDataObjects.get(4).getFrequencyDistribution());
        expectedOutputMatches = histogramDataObjects.get(4).getFrequencyDistribution().containsAll(expectedOutput);
        assertTrue(outputsSizeMatch && actualOutputMatches && expectedOutputMatches);
    }
}
