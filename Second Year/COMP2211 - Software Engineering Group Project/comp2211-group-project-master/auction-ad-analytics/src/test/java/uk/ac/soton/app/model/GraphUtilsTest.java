package uk.ac.soton.app.model;

import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.soton.app.enums.TimeInterval;
import uk.ac.soton.app.exceptions.InvalidLogFile;
import uk.ac.soton.app.exceptions.InvalidLogLine;
import uk.ac.soton.app.model.CSVData.CSVData;
import uk.ac.soton.app.model.ProcessedData.HistogramData;
import uk.ac.soton.app.model.ProcessedData.TimeIntervalMetrics;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test the GraphUtils class
 */
public class GraphUtilsTest {

    //XYChart.Series objects used to test some methods of the GraphUtils class
    private List<XYChart.Series<String, Number>> XYChartSeriesObjects;


    /**
     * Sets up everything needed to test the GraphUtils class
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     */
    @BeforeEach
    public void setUp() throws InvalidLogFile, InvalidLogLine {
        //Sets up a list of XYChart.Series<String, Number> objects to be used to test some methods of the GraphUtils class
        CSVData csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/GraphUtils/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/GraphUtils/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/GraphUtils/ServerLogs/ServerLog1.csv"));
        TimeIntervalMetrics timeIntervalMetricsObject = new TimeIntervalMetrics(csvData, TimeInterval.HOUR, 2, new ModelDate(0, 0, 0, 0, 2, 0));
        HistogramData histogramDataObject = new HistogramData(csvData.getClicks(), 4);

        XYChart.Series<String, Number> clickSeries = GraphUtils.getClickSeries(timeIntervalMetricsObject, TimeInterval.HOUR);
        XYChart.Series<String, Number> uniqueSeries = GraphUtils.getUniqueSeries(timeIntervalMetricsObject, TimeInterval.HOUR);
        XYChart.Series<String, Number> conversionSeries = GraphUtils.getConversionSeries(timeIntervalMetricsObject, TimeInterval.HOUR);
        XYChart.Series<String, Number> CTRSeries = GraphUtils.getCTRSeries(timeIntervalMetricsObject, TimeInterval.HOUR);
        XYChart.Series<String, Number> CPCSeries = GraphUtils.getCPCSeries(timeIntervalMetricsObject, TimeInterval.HOUR);
        XYChart.Series<String, Number> bounceRateSeries = GraphUtils.getBounceRateSeries(timeIntervalMetricsObject, TimeInterval.HOUR);

        csvData = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/GraphUtils/ImpressionLogs/ImpressionLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/GraphUtils/ClickLogs/ClickLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/GraphUtils/ServerLogs/ServerLog2.csv"));
        timeIntervalMetricsObject = new TimeIntervalMetrics(csvData, TimeInterval.DAY, 2, new ModelDate(0, 0, 0, 0, 2, 0));

        XYChart.Series<String, Number> impressionSeries = GraphUtils.getImpressionSeries(timeIntervalMetricsObject, TimeInterval.DAY);
        XYChart.Series<String, Number> bounceSeries = GraphUtils.getBounceSeries(timeIntervalMetricsObject, TimeInterval.DAY);
        XYChart.Series<String, Number> costSeries = GraphUtils.getCostSeries(timeIntervalMetricsObject, TimeInterval.DAY);
        XYChart.Series<String, Number> CPASeries = GraphUtils.getCPASeries(timeIntervalMetricsObject, TimeInterval.DAY);
        XYChart.Series<String, Number> CPMSeries = GraphUtils.getCPMSeries(timeIntervalMetricsObject, TimeInterval.DAY);
        XYChart.Series<String, Number> clickCostHistogramSeries = GraphUtils.getClickCostHistogramSeries(histogramDataObject);

        this.XYChartSeriesObjects = Arrays.asList(clickSeries, impressionSeries, uniqueSeries, bounceSeries, conversionSeries, costSeries, CTRSeries, CPASeries, CPCSeries, CPMSeries, bounceRateSeries, clickCostHistogramSeries);
    }

    /**
     * Tests the getClickSeries() method
     */
    @Test
    public void testGetClickSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("Clicks", XYChartSeriesObjects.get(0).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(0).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01 12:00:00", XYChartSeriesObjects.get(0).getData().get(0).getXValue());
        assertEquals(2, XYChartSeriesObjects.get(0).getData().get(0).getYValue());
        assertEquals("2015-01-01 13:00:00", XYChartSeriesObjects.get(0).getData().get(1).getXValue());
        assertEquals(3, XYChartSeriesObjects.get(0).getData().get(1).getYValue());
        assertEquals("2015-01-01 14:00:00", XYChartSeriesObjects.get(0).getData().get(2).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(0).getData().get(2).getYValue());
        assertEquals("2015-01-01 15:00:00", XYChartSeriesObjects.get(0).getData().get(3).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(0).getData().get(3).getYValue());
        assertEquals("2015-01-01 16:00:00", XYChartSeriesObjects.get(0).getData().get(4).getXValue());
        assertEquals(2, XYChartSeriesObjects.get(0).getData().get(4).getYValue());
        assertEquals("2015-01-01 17:00:00", XYChartSeriesObjects.get(0).getData().get(5).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(0).getData().get(5).getYValue());
        assertEquals("2015-01-01 18:00:00", XYChartSeriesObjects.get(0).getData().get(6).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(0).getData().get(6).getYValue());
    }

    /**
     * Tests the getImpressionSeries() method
     */
    @Test
    public void testGetImpressionSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("Impressions", XYChartSeriesObjects.get(1).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(1).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01", XYChartSeriesObjects.get(1).getData().get(0).getXValue());
        assertEquals(3, XYChartSeriesObjects.get(1).getData().get(0).getYValue());
        assertEquals("2015-01-02", XYChartSeriesObjects.get(1).getData().get(1).getXValue());
        assertEquals(3, XYChartSeriesObjects.get(1).getData().get(1).getYValue());
        assertEquals("2015-01-03", XYChartSeriesObjects.get(1).getData().get(2).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(1).getData().get(2).getYValue());
        assertEquals("2015-01-04", XYChartSeriesObjects.get(1).getData().get(3).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(1).getData().get(3).getYValue());
        assertEquals("2015-01-05", XYChartSeriesObjects.get(1).getData().get(4).getXValue());
        assertEquals(2, XYChartSeriesObjects.get(1).getData().get(4).getYValue());
        assertEquals("2015-01-06", XYChartSeriesObjects.get(1).getData().get(5).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(1).getData().get(5).getYValue());
        assertEquals("2015-01-07", XYChartSeriesObjects.get(1).getData().get(6).getXValue());
        assertEquals(2, XYChartSeriesObjects.get(1).getData().get(6).getYValue());
    }

    /**
     * Tests the getUniqueSeries() method
     */
    @Test
    public void testGetUniqueSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("Uniques", XYChartSeriesObjects.get(2).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(2).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01 12:00:00", XYChartSeriesObjects.get(2).getData().get(0).getXValue());
        assertEquals(2, XYChartSeriesObjects.get(2).getData().get(0).getYValue());
        assertEquals("2015-01-01 13:00:00", XYChartSeriesObjects.get(2).getData().get(1).getXValue());
        assertEquals(3, XYChartSeriesObjects.get(2).getData().get(1).getYValue());
        assertEquals("2015-01-01 14:00:00", XYChartSeriesObjects.get(2).getData().get(2).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(2).getData().get(2).getYValue());
        assertEquals("2015-01-01 15:00:00", XYChartSeriesObjects.get(2).getData().get(3).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(2).getData().get(3).getYValue());
        assertEquals("2015-01-01 16:00:00", XYChartSeriesObjects.get(2).getData().get(4).getXValue());
        assertEquals(2, XYChartSeriesObjects.get(2).getData().get(4).getYValue());
        assertEquals("2015-01-01 17:00:00", XYChartSeriesObjects.get(2).getData().get(5).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(2).getData().get(5).getYValue());
        assertEquals("2015-01-01 18:00:00", XYChartSeriesObjects.get(2).getData().get(6).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(2).getData().get(6).getYValue());
    }

    /**
     * Tests the getBounceSeries() method
     */
    @Test
    public void testGetBounceSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("Bounces", XYChartSeriesObjects.get(3).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(3).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01", XYChartSeriesObjects.get(3).getData().get(0).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(3).getData().get(0).getYValue());
        assertEquals("2015-01-02", XYChartSeriesObjects.get(3).getData().get(1).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(3).getData().get(1).getYValue());
        assertEquals("2015-01-03", XYChartSeriesObjects.get(3).getData().get(2).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(3).getData().get(2).getYValue());
        assertEquals("2015-01-04", XYChartSeriesObjects.get(3).getData().get(3).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(3).getData().get(3).getYValue());
        assertEquals("2015-01-05", XYChartSeriesObjects.get(3).getData().get(4).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(3).getData().get(4).getYValue());
        assertEquals("2015-01-06", XYChartSeriesObjects.get(3).getData().get(5).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(3).getData().get(5).getYValue());
        assertEquals("2015-01-07", XYChartSeriesObjects.get(3).getData().get(6).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(3).getData().get(6).getYValue());
    }

    /**
     * Tests the getConversionSeries() method
     */
    @Test
    public void testGetConversionSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("Conversions", XYChartSeriesObjects.get(4).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(4).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01 12:00:00", XYChartSeriesObjects.get(4).getData().get(0).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(4).getData().get(0).getYValue());
        assertEquals("2015-01-01 13:00:00", XYChartSeriesObjects.get(4).getData().get(1).getXValue());
        assertEquals(2, XYChartSeriesObjects.get(4).getData().get(1).getYValue());
        assertEquals("2015-01-01 14:00:00", XYChartSeriesObjects.get(4).getData().get(2).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(4).getData().get(2).getYValue());
        assertEquals("2015-01-01 15:00:00", XYChartSeriesObjects.get(4).getData().get(3).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(4).getData().get(3).getYValue());
        assertEquals("2015-01-01 16:00:00", XYChartSeriesObjects.get(4).getData().get(4).getXValue());
        assertEquals(2, XYChartSeriesObjects.get(4).getData().get(4).getYValue());
        assertEquals("2015-01-01 17:00:00", XYChartSeriesObjects.get(4).getData().get(5).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(4).getData().get(5).getYValue());
        assertEquals("2015-01-01 18:00:00", XYChartSeriesObjects.get(4).getData().get(6).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(4).getData().get(6).getYValue());
    }

    /**
     * Tests the getCostSeries() method
     */
    @Test
    public void testGetCostSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("Costs", XYChartSeriesObjects.get(5).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(5).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01", XYChartSeriesObjects.get(5).getData().get(0).getXValue());
        assertEquals(0.0025519212, XYChartSeriesObjects.get(5).getData().get(0).getYValue());
        assertEquals("2015-01-02", XYChartSeriesObjects.get(5).getData().get(1).getXValue());
        assertEquals(9.342585000000002E-4, XYChartSeriesObjects.get(5).getData().get(1).getYValue());
        assertEquals("2015-01-03", XYChartSeriesObjects.get(5).getData().get(2).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(5).getData().get(2).getYValue());
        assertEquals("2015-01-04", XYChartSeriesObjects.get(5).getData().get(3).getXValue());
        assertEquals(9.82941E-4, XYChartSeriesObjects.get(5).getData().get(3).getYValue());
        assertEquals("2015-01-05", XYChartSeriesObjects.get(5).getData().get(4).getXValue());
        assertEquals(2.1719999999999999E-7, XYChartSeriesObjects.get(5).getData().get(4).getYValue());
        assertEquals("2015-01-06", XYChartSeriesObjects.get(5).getData().get(5).getXValue());
        assertEquals(2.2650000000000002E-7, XYChartSeriesObjects.get(5).getData().get(5).getYValue());
        assertEquals("2015-01-07", XYChartSeriesObjects.get(5).getData().get(6).getXValue());
        assertEquals(0.0014574469, XYChartSeriesObjects.get(5).getData().get(6).getYValue());
    }

    /**
     * Tests the getCTRSeries() method
     */
    @Test
    public void testGetCTRSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("CTR", XYChartSeriesObjects.get(6).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(6).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01 12:00:00", XYChartSeriesObjects.get(6).getData().get(0).getXValue());
        assertEquals( (float) 0.6666667, XYChartSeriesObjects.get(6).getData().get(0).getYValue());
        assertEquals("2015-01-01 13:00:00", XYChartSeriesObjects.get(6).getData().get(1).getXValue());
        assertEquals((float) 1, XYChartSeriesObjects.get(6).getData().get(1).getYValue());
        assertEquals("2015-01-01 14:00:00", XYChartSeriesObjects.get(6).getData().get(2).getXValue());
        assertEquals((float) 0, XYChartSeriesObjects.get(6).getData().get(2).getYValue());
        assertEquals("2015-01-01 15:00:00", XYChartSeriesObjects.get(6).getData().get(3).getXValue());
        assertEquals((float) 1, XYChartSeriesObjects.get(6).getData().get(3).getYValue());
        assertEquals("2015-01-01 16:00:00", XYChartSeriesObjects.get(6).getData().get(4).getXValue());
        assertEquals((float) 1, XYChartSeriesObjects.get(6).getData().get(4).getYValue());
        assertEquals("2015-01-01 17:00:00", XYChartSeriesObjects.get(6).getData().get(5).getXValue());
        assertEquals((float) 1, XYChartSeriesObjects.get(6).getData().get(5).getYValue());
        assertEquals("2015-01-01 18:00:00", XYChartSeriesObjects.get(6).getData().get(6).getXValue());
        assertEquals((float) 0.5, XYChartSeriesObjects.get(6).getData().get(6).getYValue());
    }

    /**
     * Tests the getCPASeries() method
     */
    @Test
    public void testGetCPASeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("CPA", XYChartSeriesObjects.get(7).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(7).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01", XYChartSeriesObjects.get(7).getData().get(0).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(7).getData().get(0).getYValue());
        assertEquals("2015-01-02", XYChartSeriesObjects.get(7).getData().get(1).getXValue());
        assertEquals(4.671292379498482E-4, XYChartSeriesObjects.get(7).getData().get(1).getYValue());
        assertEquals("2015-01-03", XYChartSeriesObjects.get(7).getData().get(2).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(7).getData().get(2).getYValue());
        assertEquals("2015-01-04", XYChartSeriesObjects.get(7).getData().get(3).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(7).getData().get(3).getYValue());
        assertEquals("2015-01-05", XYChartSeriesObjects.get(7).getData().get(4).getXValue());
        assertEquals(1.0859999747481198E-7, XYChartSeriesObjects.get(7).getData().get(4).getYValue());
        assertEquals("2015-01-06", XYChartSeriesObjects.get(7).getData().get(5).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(7).getData().get(5).getYValue());
        assertEquals("2015-01-07", XYChartSeriesObjects.get(7).getData().get(6).getXValue());
        assertEquals(0.001457446962594986, XYChartSeriesObjects.get(7).getData().get(6).getYValue());
    }

    /**
     * Tests the getCPCSeries() method
     */
    @Test
    public void testGetCPCSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("CPC", XYChartSeriesObjects.get(8).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(8).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01 12:00:00", XYChartSeriesObjects.get(8).getData().get(0).getXValue());
        assertEquals(0.0012259606271982193, XYChartSeriesObjects.get(8).getData().get(0).getYValue());
        assertEquals("2015-01-01 13:00:00", XYChartSeriesObjects.get(8).getData().get(1).getXValue());
        assertEquals(3.1141949817538263E-4, XYChartSeriesObjects.get(8).getData().get(1).getYValue());
        assertEquals("2015-01-01 14:00:00", XYChartSeriesObjects.get(8).getData().get(2).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(8).getData().get(2).getYValue());
        assertEquals("2015-01-01 15:00:00", XYChartSeriesObjects.get(8).getData().get(3).getXValue());
        assertEquals(9.829410165548324E-4, XYChartSeriesObjects.get(8).getData().get(3).getYValue());
        assertEquals("2015-01-01 16:00:00", XYChartSeriesObjects.get(8).getData().get(4).getXValue());
        assertEquals(1.0859999747481198E-7, XYChartSeriesObjects.get(8).getData().get(4).getYValue());
        assertEquals("2015-01-01 17:00:00", XYChartSeriesObjects.get(8).getData().get(5).getXValue());
        assertEquals(2.2649999664281495E-7, XYChartSeriesObjects.get(8).getData().get(5).getYValue());
        assertEquals("2015-01-01 18:00:00", XYChartSeriesObjects.get(8).getData().get(6).getXValue());
        assertEquals(0.001457446962594986, XYChartSeriesObjects.get(8).getData().get(6).getYValue());
    }

    /**
     * Tests the getCPMSeries() method
     */
    @Test
    public void testGetCPMSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("CPM", XYChartSeriesObjects.get(9).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(9).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01", XYChartSeriesObjects.get(9).getData().get(0).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(9).getData().get(0).getYValue());
        assertEquals("2015-01-02", XYChartSeriesObjects.get(9).getData().get(1).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(9).getData().get(1).getYValue());
        assertEquals("2015-01-03", XYChartSeriesObjects.get(9).getData().get(2).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(9).getData().get(2).getYValue());
        assertEquals("2015-01-04", XYChartSeriesObjects.get(9).getData().get(3).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(9).getData().get(3).getYValue());
        assertEquals("2015-01-05", XYChartSeriesObjects.get(9).getData().get(4).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(9).getData().get(4).getYValue());
        assertEquals("2015-01-06", XYChartSeriesObjects.get(9).getData().get(5).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(9).getData().get(5).getYValue());
        assertEquals("2015-01-07", XYChartSeriesObjects.get(9).getData().get(6).getXValue());
        assertEquals(0.0, XYChartSeriesObjects.get(9).getData().get(6).getYValue());
    }

    /**
     * Tests the getBounceRateSeries() method
     */
    @Test
    public void testGetBounceRateSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("Bounce Rate", XYChartSeriesObjects.get(10).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(7, XYChartSeriesObjects.get(10).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("2015-01-01 12:00:00", XYChartSeriesObjects.get(10).getData().get(0).getXValue());
        assertEquals((float) 0.5, XYChartSeriesObjects.get(10).getData().get(0).getYValue());
        assertEquals("2015-01-01 13:00:00", XYChartSeriesObjects.get(10).getData().get(1).getXValue());
        assertEquals((float) 0, XYChartSeriesObjects.get(10).getData().get(1).getYValue());
        assertEquals("2015-01-01 14:00:00", XYChartSeriesObjects.get(10).getData().get(2).getXValue());
        assertEquals((float) 0, XYChartSeriesObjects.get(10).getData().get(2).getYValue());
        assertEquals("2015-01-01 15:00:00", XYChartSeriesObjects.get(10).getData().get(3).getXValue());
        assertEquals((float) 1, XYChartSeriesObjects.get(10).getData().get(3).getYValue());
        assertEquals("2015-01-01 16:00:00", XYChartSeriesObjects.get(10).getData().get(4).getXValue());
        assertEquals((float) 0.5, XYChartSeriesObjects.get(10).getData().get(4).getYValue());
        assertEquals("2015-01-01 17:00:00", XYChartSeriesObjects.get(10).getData().get(5).getXValue());
        assertEquals((float) 1, XYChartSeriesObjects.get(10).getData().get(5).getYValue());
        assertEquals("2015-01-01 18:00:00", XYChartSeriesObjects.get(10).getData().get(6).getXValue());
        assertEquals((float) 0, XYChartSeriesObjects.get(10).getData().get(6).getYValue());
    }

    /**
     * Tests the getClickCostHistogramSeries() method
     */
    @Test
    public void testGetClickCostHistogramSeries() {
        //TEST #1
        //Tests the name of the series
        assertEquals("Click Costs", XYChartSeriesObjects.get(11).getName());

        //TEST #2
        //Tests the size of the series
        assertEquals(4, XYChartSeriesObjects.get(11).getData().size());

        //TEST #3
        //Tests the data points of the series
        assertEquals("≤0,04", XYChartSeriesObjects.get(11).getData().get(0).getXValue());
        assertEquals(5, XYChartSeriesObjects.get(11).getData().get(0).getYValue());
        assertEquals("≤0,08", XYChartSeriesObjects.get(11).getData().get(1).getXValue());
        assertEquals(0, XYChartSeriesObjects.get(11).getData().get(1).getYValue());
        assertEquals("≤0,12", XYChartSeriesObjects.get(11).getData().get(2).getXValue());
        assertEquals(4, XYChartSeriesObjects.get(11).getData().get(2).getYValue());
        assertEquals("≤0,16", XYChartSeriesObjects.get(11).getData().get(3).getXValue());
        assertEquals(1, XYChartSeriesObjects.get(11).getData().get(3).getYValue());
    }
}