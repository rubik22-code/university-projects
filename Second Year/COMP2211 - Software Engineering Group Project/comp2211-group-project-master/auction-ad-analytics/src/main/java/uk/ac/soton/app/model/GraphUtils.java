package uk.ac.soton.app.model;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import uk.ac.soton.app.enums.TimeInterval;
import uk.ac.soton.app.model.ProcessedData.HistogramData;
import uk.ac.soton.app.model.ProcessedData.Metrics;
import uk.ac.soton.app.model.ProcessedData.TimeIntervalMetrics;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.text.DecimalFormat;
import java.util.List;

public class GraphUtils
{
    public static XYChart.Series<String, Number> getClickSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Clicks");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getClicks()));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getImpressionSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Impressions");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getImpressions()));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getUniqueSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Uniques");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getUniques()));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getBounceSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bounces");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getBounces()));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getConversionSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Conversions");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getConversions()));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getCostSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Costs");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getTotalCost() / 100.0));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getCTRSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("CTR");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getCTR()));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getCPASeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("CPA");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getCPA() / 100.0));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getCPCSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("CPC");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getCPC() / 100.0));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getCPMSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("CPM");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getCPM() / 100.0));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getBounceRateSeries(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bounce Rate");

        int counter = 0;
        for ( Metrics metric : timeIntervalMetrics.getMetrics() ) {
            ModelDate date = timeIntervalMetrics.getTimeIntervals().get(counter);
            series.getData().add(new XYChart.Data<>(date.asString(interval), metric.getBounceRate()));
            counter++;
        }

        return ( series );
    }

    public static XYChart.Series<String, Number> getClickCostHistogramSeries(HistogramData histogramData)
    {
        List<Double> costDistribution = histogramData.getCostDistribution();
        List<Integer> frequencyDistribution = histogramData.getFrequencyDistribution();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Click Costs");
        int counter = 0;
        for ( int frequency : frequencyDistribution ) {
            double upperBound = costDistribution.get(counter);

            DecimalFormat formatter = new DecimalFormat("#.##");
            series.getData().add(new XYChart.Data<>(String.format("≤%s", formatter.format(upperBound)), frequency));

            counter++;
        }

        return ( series );
    }
}