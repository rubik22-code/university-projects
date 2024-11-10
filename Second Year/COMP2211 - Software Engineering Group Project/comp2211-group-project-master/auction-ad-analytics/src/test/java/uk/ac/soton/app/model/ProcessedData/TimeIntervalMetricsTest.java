package uk.ac.soton.app.model.ProcessedData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.enums.TimeInterval;
import uk.ac.soton.app.exceptions.InvalidLogFile;
import uk.ac.soton.app.exceptions.InvalidLogLine;
import uk.ac.soton.app.model.CSVData.CSVData;
import uk.ac.soton.app.model.CSVDataUnits.Click;
import uk.ac.soton.app.model.CSVDataUnits.Impression;
import uk.ac.soton.app.model.CSVDataUnits.Interaction;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the TimeIntervalMetrics class
 */
public class TimeIntervalMetricsTest {

    //List of TimeIntervalMetrics objects used to test some methods of the TimeIntervalMetrics class
    List<TimeIntervalMetrics> timeIntervalMetricsObjects;

    //Expected outputs for the tests in the testGetTimeIntervals() method
    List<List<ModelDate>> expectedOutputs1;

    //Expected outputs for the tests in the testGetMetrics() method
    List<List<Metrics>> expectedOutputs2;


    /**
     * Sets up everything needed to test the TimeIntervalMetrics class
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     */
    @BeforeEach
    void setUp() throws InvalidLogLine, InvalidLogFile {
        //Sets up a list of TimeIntervalMetrics objects to be used to test some methods of the TimeIntervalMetrics class
        this.timeIntervalMetricsObjects = new ArrayList<>();

        CSVData csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"));
        this.timeIntervalMetricsObjects.add(new TimeIntervalMetrics(csvData, TimeInterval.DAY, 2, new ModelDate(0, 0, 0, 0, 2, 0)));

        CSVData csvData1 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog1.csv"));
        this.timeIntervalMetricsObjects.add(new TimeIntervalMetrics(csvData1, TimeInterval.HOUR, 2, new ModelDate(0, 0, 0, 0, 2, 0)));

        CSVData csvData2 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog2.csv"));
        this.timeIntervalMetricsObjects.add(new TimeIntervalMetrics(csvData2, TimeInterval.DAY, 2, new ModelDate(0, 0, 0, 0, 2, 0)));

        CSVData csvData3 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog3.csv"));
        this.timeIntervalMetricsObjects.add(new TimeIntervalMetrics(csvData3, TimeInterval.WEEK, 2, new ModelDate(0, 0, 0, 0, 2, 0)));

        CSVData csvData4 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog4.csv"));
        this.timeIntervalMetricsObjects.add(new TimeIntervalMetrics(csvData4, TimeInterval.MONTH, 2, new ModelDate(0, 0, 0, 0, 2, 0)));

        List<CSVData> csvDataObjects = new ArrayList<>(Arrays.asList(csvData1, csvData2, csvData3, csvData4));
        List<TimeInterval> timeIntervals = new ArrayList<>(Arrays.asList(TimeInterval.HOUR, TimeInterval.DAY, TimeInterval.WEEK, TimeInterval.MONTH));
        for (CSVData csvDataObject : csvDataObjects) {
            csvDataObject.getImpressions().get(0).setVisible(false);
            csvDataObject.getImpressions().get(1).setVisible(false);
            csvDataObject.getImpressions().get(2).setVisible(false);
            csvDataObject.getImpressions().get(10).setVisible(false);
            csvDataObject.getImpressions().get(11).setVisible(false);
            csvDataObject.getClicks().get(0).setVisible(false);
            csvDataObject.getClicks().get(1).setVisible(false);
            csvDataObject.getClicks().get(9).setVisible(false);
            csvDataObject.getInteractions().get(0).setVisible(false);
            csvDataObject.getInteractions().get(1).setVisible(false);
            csvDataObject.getInteractions().get(9).setVisible(false);
            this.timeIntervalMetricsObjects.add(new TimeIntervalMetrics(csvDataObject, timeIntervals.get(csvDataObjects.indexOf(csvDataObject)), 2, new ModelDate(0, 0, 0, 0, 2, 0)));
        }

        for (CSVData csvDataObject : csvDataObjects) {
            for (int i = 0; i < 12; i++) {csvDataObject.getImpressions().get(i).setVisible(true);}
            for (int i = 0; i < 10; i++) {csvDataObject.getClicks().get(i).setVisible(true);}
            for (int i = 0; i < 10; i++) {csvDataObject.getInteractions().get(i).setVisible(true);}
            csvDataObject.getImpressions().get(1).setVisible(false);
            csvDataObject.getImpressions().get(3).setVisible(false);
            csvDataObject.getImpressions().get(6).setVisible(false);
            csvDataObject.getImpressions().get(7).setVisible(false);
            csvDataObject.getImpressions().get(10).setVisible(false);
            csvDataObject.getClicks().get(0).setVisible(false);
            csvDataObject.getClicks().get(2).setVisible(false);
            csvDataObject.getClicks().get(5).setVisible(false);
            csvDataObject.getClicks().get(6).setVisible(false);
            csvDataObject.getClicks().get(9).setVisible(false);
            csvDataObject.getInteractions().get(0).setVisible(false);
            csvDataObject.getInteractions().get(2).setVisible(false);
            csvDataObject.getInteractions().get(5).setVisible(false);
            csvDataObject.getInteractions().get(6).setVisible(false);
            csvDataObject.getInteractions().get(9).setVisible(false);
            this.timeIntervalMetricsObjects.add(new TimeIntervalMetrics(csvDataObject, timeIntervals.get(csvDataObjects.indexOf(csvDataObject)), 2, new ModelDate(0, 0, 0, 0, 2, 0)));
        }

        for (CSVData csvDataObject : csvDataObjects) {
            for (int i = 0; i < 12; i++) {csvDataObject.getImpressions().get(i).setVisible(false);}
            for (int i = 0; i < 10; i++) {csvDataObject.getClicks().get(i).setVisible(false);}
            for (int i = 0; i < 10; i++) {csvDataObject.getInteractions().get(i).setVisible(false);}
            this.timeIntervalMetricsObjects.add(new TimeIntervalMetrics(csvDataObject, timeIntervals.get(csvDataObjects.indexOf(csvDataObject)), 2, new ModelDate(0, 0, 0, 0, 2, 0)));
        }

        //Sets up the expected outputs for some tests in this class
        setUpTestExpectedOutputs();
    }

    /**
     * Sets up the expected outputs of the tests on the TimeIntervalMetrics class
     */
    void setUpTestExpectedOutputs() throws InvalidLogFile, InvalidLogLine {
        //Sets up the expected output for test #2 in the testGetTimeIntervals() method
        ModelDate date1 = new ModelDate(2015,1,1,12,0,0);
        ModelDate date2 = new ModelDate(2015,1,1,13,0,0);
        ModelDate date3 = new ModelDate(2015,1,1,14,0,0);
        ModelDate date4 = new ModelDate(2015,1,1,15,0,0);
        ModelDate date5 = new ModelDate(2015,1,1,16,0,0);
        ModelDate date6 = new ModelDate(2015,1,1,17,0,0);
        ModelDate date7 = new ModelDate(2015,1,1,18,0,0);
        this.expectedOutputs1 = new ArrayList<>(List.of(new ArrayList<>(Arrays.asList(date1, date2, date3, date4, date5, date6, date7))));

        //Sets up the expected output for test #3 in the testGetTimeIntervals() method
        ModelDate date8 = new ModelDate(2015,1,1,0,0,0);
        ModelDate date9 = new ModelDate(2015,1,2,0,0,0);
        ModelDate date10 = new ModelDate(2015,1,3,0,0,0);
        ModelDate date11 = new ModelDate(2015,1,4,0,0,0);
        ModelDate date12 = new ModelDate(2015,1,5,0,0,0);
        ModelDate date13 = new ModelDate(2015,1,6,0,0,0);
        ModelDate date14 = new ModelDate(2015,1,7,0,0,0);
        this.expectedOutputs1.add(new ArrayList<>(Arrays.asList(date8, date9, date10, date11, date12, date13, date14)));

        //Sets up the expected output for test #4 in the testGetTimeIntervals() method
        ModelDate date15 = new ModelDate(2015,1,1,0,0,0);
        ModelDate date16 = new ModelDate(2015,1,8,0,0,0);
        ModelDate date17 = new ModelDate(2015,1,15,0,0,0);
        ModelDate date18 = new ModelDate(2015,1,22,0,0,0);
        ModelDate date19 = new ModelDate(2015,1,29,0,0,0);
        ModelDate date20 = new ModelDate(2015,2,5,0,0,0);
        ModelDate date21 = new ModelDate(2015,2,12,0,0,0);
        this.expectedOutputs1.add(new ArrayList<>(Arrays.asList(date15, date16, date17, date18, date19, date20, date21)));

        //Sets up the expected output for test #5 in the testGetTimeIntervals() method
        ModelDate date22 = new ModelDate(2015,1,1,0,0,0);
        ModelDate date23 = new ModelDate(2015,2,1,0,0,0);
        ModelDate date24 = new ModelDate(2015,3,1,0,0,0);
        ModelDate date25 = new ModelDate(2015,4,1,0,0,0);
        ModelDate date26 = new ModelDate(2015,5,1,0,0,0);
        ModelDate date27 = new ModelDate(2015,6,1,0,0,0);
        ModelDate date28 = new ModelDate(2015,7,1,0,0,0);
        this.expectedOutputs1.add(new ArrayList<>(Arrays.asList(date22, date23, date24, date25, date26, date27, date28)));

        //Sets up the expected output for test #6 in the testGetTimeIntervals() method
        this.expectedOutputs1.add(new ArrayList<>(Arrays.asList(date2, date3, date4, date5, date6)));

        //Sets up the expected output for test #7 in the testGetTimeIntervals() method
        this.expectedOutputs1.add(new ArrayList<>(Arrays.asList(date9, date10, date11, date12, date13)));

        //Sets up the expected output for test #8 in the testGetTimeIntervals() method
        ModelDate date29 = new ModelDate(2015,1,9,0,0,0);
        ModelDate date30 = new ModelDate(2015,1,16,0,0,0);
        ModelDate date31 = new ModelDate(2015,1,23,0,0,0);
        ModelDate date32 = new ModelDate(2015,1,30,0,0,0);
        this.expectedOutputs1.add(new ArrayList<>(Arrays.asList(date29, date30, date31, date32)));

        //Sets up the expected output for test #9 in the testGetTimeIntervals() method
        this.expectedOutputs1.add(new ArrayList<>(Arrays.asList(date23, date24, date25, date26, date27)));

        //Sets up the expected outputs for tests #2, #3, #4 and #5 in the testGetMetrics() method
        this.expectedOutputs2 = new ArrayList<>();
        CSVData csvData1 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog1.csv"));
        CSVData csvData2 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog2.csv"));
        CSVData csvData3 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog3.csv"));
        CSVData csvData4 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog4.csv"));
        List<CSVData> csvDataObjects = new ArrayList<>(Arrays.asList(csvData1, csvData2, csvData3, csvData4));
        for (CSVData csvDataObject : csvDataObjects) {
            List<Impression> impressions1 = new ArrayList<>(Arrays.asList(csvDataObject.getImpressions().get(0), csvDataObject.getImpressions().get(1), csvDataObject.getImpressions().get(2)));
            List<Impression> impressions2 = new ArrayList<>(Arrays.asList(csvDataObject.getImpressions().get(3), csvDataObject.getImpressions().get(4), csvDataObject.getImpressions().get(5)));
            List<Impression> impressions3 = new ArrayList<>();
            List<Impression> impressions4 = new ArrayList<>(List.of(csvDataObject.getImpressions().get(6)));
            List<Impression> impressions5 = new ArrayList<>(Arrays.asList(csvDataObject.getImpressions().get(7), csvDataObject.getImpressions().get(8)));
            List<Impression> impressions6 = new ArrayList<>(List.of(csvDataObject.getImpressions().get(9)));
            List<Impression> impressions7 = new ArrayList<>(Arrays.asList(csvDataObject.getImpressions().get(10), csvDataObject.getImpressions().get(11)));
            List<List<Impression>> impressions = new ArrayList<>(Arrays.asList(impressions1, impressions2, impressions3, impressions4, impressions5, impressions6, impressions7));

            List<Click> clicks1 = new ArrayList<>(Arrays.asList(csvDataObject.getClicks().get(0), csvDataObject.getClicks().get(1)));
            List<Click> clicks2 = new ArrayList<>(Arrays.asList(csvDataObject.getClicks().get(2), csvDataObject.getClicks().get(3), csvDataObject.getClicks().get(4)));
            List<Click> clicks3 = new ArrayList<>();
            List<Click> clicks4 = new ArrayList<>(List.of(csvDataObject.getClicks().get(5)));
            List<Click> clicks5 = new ArrayList<>(Arrays.asList(csvDataObject.getClicks().get(6), csvDataObject.getClicks().get(7)));
            List<Click> clicks6 = new ArrayList<>(List.of(csvDataObject.getClicks().get(8)));
            List<Click> clicks7 = new ArrayList<>(List.of(csvDataObject.getClicks().get(9)));
            List<List<Click>> clicks = new ArrayList<>(Arrays.asList(clicks1, clicks2, clicks3, clicks4, clicks5, clicks6, clicks7));

            List<Interaction> interactions1 = new ArrayList<>(Arrays.asList(csvDataObject.getInteractions().get(0), csvDataObject.getInteractions().get(1)));
            List<Interaction> interactions2 = new ArrayList<>(Arrays.asList(csvDataObject.getInteractions().get(2), csvDataObject.getInteractions().get(3), csvDataObject.getInteractions().get(4)));
            List<Interaction> interactions3 = new ArrayList<>();
            List<Interaction> interactions4 = new ArrayList<>(List.of(csvDataObject.getInteractions().get(5)));
            List<Interaction> interactions5 = new ArrayList<>(Arrays.asList(csvDataObject.getInteractions().get(6), csvDataObject.getInteractions().get(7)));
            List<Interaction> interactions6 = new ArrayList<>(List.of(csvDataObject.getInteractions().get(8)));
            List<Interaction> interactions7 = new ArrayList<>(List.of(csvDataObject.getInteractions().get(9)));
            List<List<Interaction>> interactions = new ArrayList<>(Arrays.asList(interactions1, interactions2, interactions3, interactions4, interactions5, interactions6, interactions7));

            List<Metrics> metrics = new ArrayList<>();
            for (int i = 0; i < 7; i++) metrics.add(new Metrics(impressions.get(i), clicks.get(i), interactions.get(i), 2, new ModelDate(0, 0, 0, 0, 2, 0)));
            this.expectedOutputs2.add(metrics);
        }

        //Sets up the expected outputs for tests #6, #7, #8 and #9 in the testGetMetrics() method
        for (CSVData csvDataObject : csvDataObjects) {
            List<Impression> impressions1 = new ArrayList<>(Arrays.asList(csvDataObject.getImpressions().get(0), csvDataObject.getImpressions().get(2)));
            List<Impression> impressions2 = new ArrayList<>(Arrays.asList(csvDataObject.getImpressions().get(4), csvDataObject.getImpressions().get(5)));
            List<Impression> impressions3 = new ArrayList<>();
            List<Impression> impressions4 = new ArrayList<>();
            List<Impression> impressions5 = new ArrayList<>(List.of(csvDataObject.getImpressions().get(8)));
            List<Impression> impressions6 = new ArrayList<>(List.of(csvDataObject.getImpressions().get(9)));
            List<Impression> impressions7 = new ArrayList<>(List.of(csvDataObject.getImpressions().get(11)));
            List<List<Impression>> impressions = new ArrayList<>(Arrays.asList(impressions1, impressions2, impressions3, impressions4, impressions5, impressions6, impressions7));

            List<Click> clicks1 = new ArrayList<>(List.of(csvDataObject.getClicks().get(1)));
            List<Click> clicks2 = new ArrayList<>(Arrays.asList(csvDataObject.getClicks().get(3), csvDataObject.getClicks().get(4)));
            List<Click> clicks3 = new ArrayList<>();
            List<Click> clicks4 = new ArrayList<>();
            List<Click> clicks5 = new ArrayList<>(List.of(csvDataObject.getClicks().get(7)));
            List<Click> clicks6 = new ArrayList<>(List.of(csvDataObject.getClicks().get(8)));
            List<Click> clicks7 = new ArrayList<>();
            List<List<Click>> clicks = new ArrayList<>(Arrays.asList(clicks1, clicks2, clicks3, clicks4, clicks5, clicks6, clicks7));

            List<Interaction> interactions1 = new ArrayList<>(List.of(csvDataObject.getInteractions().get(1)));
            List<Interaction> interactions2 = new ArrayList<>(Arrays.asList(csvDataObject.getInteractions().get(3), csvDataObject.getInteractions().get(4)));
            List<Interaction> interactions3 = new ArrayList<>();
            List<Interaction> interactions4 = new ArrayList<>();
            List<Interaction> interactions5 = new ArrayList<>(List.of(csvDataObject.getInteractions().get(7)));
            List<Interaction> interactions6 = new ArrayList<>(List.of(csvDataObject.getInteractions().get(8)));
            List<Interaction> interactions7 = new ArrayList<>();
            List<List<Interaction>> interactions = new ArrayList<>(Arrays.asList(interactions1, interactions2, interactions3, interactions4, interactions5, interactions6, interactions7));

            List<Metrics> metrics = new ArrayList<>();
            for (int i = 0; i < 7; i++) metrics.add(new Metrics(impressions.get(i), clicks.get(i), interactions.get(i), 2, new ModelDate(0, 0, 0, 0, 2, 0)));
            this.expectedOutputs2.add(metrics);
        }

    }

    /**
     * Checks if two lists of dates are identical or not
     * @param list1 list of dates
     * @param list2 list of dates
     * @return whether the two lists are identical or not
     */
    boolean assertDates(List<ModelDate> list1, List<ModelDate> list2) {
        for (ModelDate date : list1) {
            int currentIndex = list1.indexOf(date);
            if (!date.isEqualTo(list2.get(currentIndex))) return false;
        }
        return list1.size() == list2.size();
    }

    /**
     * Checks if two lists of sets of metrics are identical or not
     * @param list1 list of sets of metrics
     * @param list2 list of sets of metrics
     * @return whether the two lists are identical or not
     */
    boolean assertMetrics(List<Metrics> list1, List<Metrics> list2) {
        if (list1.size() != list2.size()) return false;
        for (Metrics metrics : list1) {
            int currentIndex = list1.indexOf(metrics);
            if (metrics.getImpressions() != list2.get(currentIndex).getImpressions()) return false;
            if (metrics.getClicks() != list2.get(currentIndex).getClicks()) return false;
            if (metrics.getUniques() != list2.get(currentIndex).getUniques()) return false;
            if (metrics.getBounces() != list2.get(currentIndex).getBounces()) return false;
            if (metrics.getConversions() != list2.get(currentIndex).getConversions()) return false;
            if (metrics.getTotalCost() != list2.get(currentIndex).getTotalCost()) return false;
            if (metrics.getCTR() != list2.get(currentIndex).getCTR()) return false;
            if (metrics.getCPA() != list2.get(currentIndex).getCPA()) return false;
            if (metrics.getCPC() != list2.get(currentIndex).getCPC()) return false;
            if (metrics.getCPM() != list2.get(currentIndex).getCPM()) return false;
            if (metrics.getBounceRate() != list2.get(currentIndex).getBounceRate()) return false;
        }
        return true;
    }

    /**
     * Tests the getFirstDate() method
     */
    @Test
    void testGetFirstDate() throws InvalidLogLine, InvalidLogFile {
        //TEST #1
        //Tests if null is returned
        //When there are no impressions, clicks or interactions
        CSVData csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"));
        ModelDate actualOutput = timeIntervalMetricsObjects.get(0).getFirstDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertNull(actualOutput);

        //TEST #2
        //Tests if the expected date is returned
        //When the earliest date is of an impression
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog5.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog6.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog6.csv"));
        actualOutput = timeIntervalMetricsObjects.get(0).getFirstDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertTrue(actualOutput.isEqualTo(new ModelDate(2015, 1, 1, 12, 0, 2)));

        //TEST #3
        //Tests if the expected date is returned
        //When the earliest date is of a click
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog6.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog5.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog6.csv"));
        actualOutput = timeIntervalMetricsObjects.get(0).getFirstDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertTrue(actualOutput.isEqualTo(new ModelDate(2015, 1, 1, 12, 0, 2)));

        //TEST #4
        //Tests if the expected date is returned
        //When the earliest date is of an interaction
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog6.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog6.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog5.csv"));
        actualOutput = timeIntervalMetricsObjects.get(0).getFirstDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertTrue(actualOutput.isEqualTo(new ModelDate(2015, 1, 1, 12, 0, 2)));

        //TEST #5
        //Tests if the expected date is returned
        //When the earliest impression, click and interaction have been filtered out
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog5.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog5.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog5.csv"));
        csvData.getImpressions().get(0).setVisible(false);
        csvData.getClicks().get(0).setVisible(false);
        csvData.getInteractions().get(0).setVisible(false);
        actualOutput = timeIntervalMetricsObjects.get(0).getFirstDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertTrue(actualOutput.isEqualTo(new ModelDate(2015, 1, 1, 12, 0, 4)));

        //TEST #6
        //Tests if null is returned
        //When all impressions, clicks and interactions are filtered out
        for (int i = 0; i < 6; i++) {csvData.getImpressions().get(i).setVisible(false);}
        for (int i = 0; i < 6; i++) {csvData.getClicks().get(i).setVisible(false);}
        for (int i = 0; i < 6; i++) {csvData.getInteractions().get(i).setVisible(false);}
        actualOutput = timeIntervalMetricsObjects.get(0).getFirstDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertNull(actualOutput);
    }

    /**
     * Tests the getLastDate() method
     */
    @Test
    void testGetLastDate() throws InvalidLogLine, InvalidLogFile {
        //TEST #1
        //Tests if null is returned
        //When there are no impressions, clicks or interactions
        CSVData csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"));
        ModelDate actualOutput = timeIntervalMetricsObjects.get(0).getLastDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertNull(actualOutput);

        //TEST #2
        //Tests if the expected date is returned
        //When the latest date is of an impression
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog6.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog5.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog5.csv"));
        actualOutput = timeIntervalMetricsObjects.get(0).getLastDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertTrue(actualOutput.isEqualTo(new ModelDate(2015, 1, 1, 12, 4, 11)));

        //TEST #3
        //Tests if the expected date is returned
        //When the latest date is of a click
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog5.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog6.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog5.csv"));
        actualOutput = timeIntervalMetricsObjects.get(0).getLastDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertTrue(actualOutput.isEqualTo(new ModelDate(2015, 1, 1, 12, 4, 11)));

        //TEST #4
        //Tests if the expected date is returned
        //When the latest date is of an interaction
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog5.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog5.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog6.csv"));
        actualOutput = timeIntervalMetricsObjects.get(0).getLastDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertTrue(actualOutput.isEqualTo(new ModelDate(2015, 1, 1, 12, 4, 11)));

        //TEST #5
        //Tests if the expected date is returned
        //When the latest impression, click and interaction have been filtered out
        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ImpressionLogs/ImpressionLog6.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ClickLogs/ClickLog6.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/TimeIntervalMetrics/ServerLogs/ServerLog6.csv"));
        csvData.getImpressions().get(5).setVisible(false);
        csvData.getClicks().get(5).setVisible(false);
        csvData.getInteractions().get(5).setVisible(false);
        actualOutput = timeIntervalMetricsObjects.get(0).getLastDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertTrue(actualOutput.isEqualTo(new ModelDate(2015, 1, 1, 12, 4, 10)));

        //TEST #6
        //Tests if null is returned
        //When all impressions, clicks and interactions are filtered out
        for (int i = 0; i < 6; i++) {csvData.getImpressions().get(i).setVisible(false);}
        for (int i = 0; i < 6; i++) {csvData.getClicks().get(i).setVisible(false);}
        for (int i = 0; i < 6; i++) {csvData.getInteractions().get(i).setVisible(false);}
        actualOutput = timeIntervalMetricsObjects.get(0).getLastDate(csvData.getImpressions(), csvData.getClicks(), csvData.getInteractions());
        assertNull(actualOutput);
    }


    /**
     * Tests the getTimeIntervals() method
     * Also tests the calculateTimeIntervals() method indirectly
     */
    @Test
    void testGetTimeIntervals() {
        //TEST #1
        //Tests if the expected list of time interval lower bounds is returned
        //When there are no impressions, clicks and interactions
        assertTrue(timeIntervalMetricsObjects.get(0).getTimeIntervals().isEmpty());

        //TEST #2
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is hours
        assertTrue(assertDates(expectedOutputs1.get(0), timeIntervalMetricsObjects.get(1).getTimeIntervals()));

        //TEST #3
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is days
        assertTrue(assertDates(expectedOutputs1.get(1), timeIntervalMetricsObjects.get(2).getTimeIntervals()));

        //TEST #4
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is weeks
        assertTrue(assertDates(expectedOutputs1.get(2), timeIntervalMetricsObjects.get(3).getTimeIntervals()));

        //TEST #5
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is months
        assertTrue(assertDates(expectedOutputs1.get(3), timeIntervalMetricsObjects.get(4).getTimeIntervals()));

        //TEST #6
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is hours and the earliest and latest impressions, clicks and interactions have been filtered out
        assertTrue(assertDates(expectedOutputs1.get(4), timeIntervalMetricsObjects.get(5).getTimeIntervals()));

        //TEST #7
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is days and the earliest and latest impressions, clicks and interactions have been filtered out
        assertTrue(assertDates(expectedOutputs1.get(5), timeIntervalMetricsObjects.get(6).getTimeIntervals()));

        //TEST #8
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is weeks and the earliest and latest impressions, clicks and interactions have been filtered out
        assertTrue(assertDates(expectedOutputs1.get(6), timeIntervalMetricsObjects.get(7).getTimeIntervals()));

        //TEST #9
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is months and the earliest and latest impressions, clicks and interactions have been filtered out
        assertTrue(assertDates(expectedOutputs1.get(7), timeIntervalMetricsObjects.get(8).getTimeIntervals()));

        //TEST #10
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is hours and all impressions, clicks and interactions have been filtered out
        assertTrue(timeIntervalMetricsObjects.get(13).getTimeIntervals().isEmpty());

        //TEST #11
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is days and all impressions, clicks and interactions have been filtered out
        assertTrue(timeIntervalMetricsObjects.get(14).getTimeIntervals().isEmpty());

        //TEST #12
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is weeks and all impressions, clicks and interactions have been filtered out
        assertTrue(timeIntervalMetricsObjects.get(15).getTimeIntervals().isEmpty());

        //TEST #13
        //Tests if the expected list of time interval lower bounds is returned
        //When the given time interval length is months and all impressions, clicks and interactions have been filtered out
        assertTrue(timeIntervalMetricsObjects.get(16).getTimeIntervals().isEmpty());
    }

    /**
     * Tests the getMetrics() method
     * Also tests the getWeekTimeInterval(), splitUpImpressions(), splitUpClicks(), splitUpInteractions() and calculateSetsOfMetrics() methods indirectly
     */
    @Test
    void testGetMetrics() {
        //TEST #1
        //Tests if the expected list of metrics is returned
        //When there are no impressions, clicks and interactions
        assertTrue(timeIntervalMetricsObjects.get(0).getMetrics().isEmpty());

        //TEST #2
        //Tests if the expected list of metrics is returned
        //When the given time interval length is hours
        assertTrue(assertMetrics(expectedOutputs2.get(0), timeIntervalMetricsObjects.get(1).getMetrics()));

        //TEST #3
        //Tests if the expected list of metrics is returned
        //When the given time interval length is days
        assertTrue(assertMetrics(expectedOutputs2.get(1), timeIntervalMetricsObjects.get(2).getMetrics()));

        //TEST #4
        //Tests if the expected list of metrics is returned
        //When the given time interval length is weeks
        assertTrue(assertMetrics(expectedOutputs2.get(2), timeIntervalMetricsObjects.get(3).getMetrics()));

        //TEST #5
        //Tests if the expected list of metrics is returned
        //When the given time interval length is months
        assertTrue(assertMetrics(expectedOutputs2.get(3), timeIntervalMetricsObjects.get(4).getMetrics()));

        //TEST #6
        //Tests if the expected list of metrics is returned
        //When the given time interval length is hours and some impressions, clicks and interactions have been filtered out
        assertTrue(assertMetrics(expectedOutputs2.get(4), timeIntervalMetricsObjects.get(9).getMetrics()));

        //TEST #7
        //Tests if the expected list of metrics is returned
        //When the given time interval length is days and some impressions, clicks and interactions have been filtered out
        assertTrue(assertMetrics(expectedOutputs2.get(5), timeIntervalMetricsObjects.get(10).getMetrics()));

        //TEST #8
        //Tests if the expected list of metrics is returned
        //When the given time interval length is weeks and some impressions, clicks and interactions have been filtered out
        assertTrue(assertMetrics(expectedOutputs2.get(6), timeIntervalMetricsObjects.get(11).getMetrics()));

        //TEST #9
        //Tests if the expected list of metrics is returned
        //When the given time interval length is months and some impressions, clicks and interactions have been filtered out
        assertTrue(assertMetrics(expectedOutputs2.get(7), timeIntervalMetricsObjects.get(12).getMetrics()));

        //TEST #10
        //Tests if the expected list of metrics is returned
        //When the given time interval length is hours and all impressions, clicks and interactions have been filtered out
        assertTrue(timeIntervalMetricsObjects.get(13).getMetrics().isEmpty());

        //TEST #11
        //Tests if the expected list of metrics is returned
        //When the given time interval length is days and all impressions, clicks and interactions have been filtered out
        assertTrue(timeIntervalMetricsObjects.get(14).getMetrics().isEmpty());

        //TEST #12
        //Tests if the expected list of metrics is returned
        //When the given time interval length is weeks and all impressions, clicks and interactions have been filtered out
        assertTrue(timeIntervalMetricsObjects.get(15).getMetrics().isEmpty());

        //TEST #13
        //Tests if the expected list of metrics is returned
        //When the given time interval length is months and all impressions, clicks and interactions have been filtered out
        assertTrue(timeIntervalMetricsObjects.get(16).getMetrics().isEmpty());
    }
}
