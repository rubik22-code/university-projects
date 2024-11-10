package uk.ac.soton.app.model;

import javafx.concurrent.Task;
import javafx.stage.DirectoryChooser;
import javafx.util.Pair;
import uk.ac.soton.app.enums.TimeInterval;
import uk.ac.soton.app.exceptions.*;
import uk.ac.soton.app.model.CSVData.CSVData;
import uk.ac.soton.app.model.CSVData.CSVFilter;
import uk.ac.soton.app.model.ProcessedData.HistogramData;
import uk.ac.soton.app.model.ProcessedData.Metrics;
import uk.ac.soton.app.model.ProcessedData.TimeIntervalMetrics;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import java.io.File;
import java.util.ArrayList;

public class Campaign
{
    // Values for defining how a bounce is registered
    private int pagesViewedForInteraction = 1;
    private ModelDate timeViewedForInteraction = new ModelDate(0, 0, 0, 0, 3, 0);

    // Time settings
    private TimeInterval interval = TimeInterval.DAY;
    private ModelDate periodStartDate;
    private ModelDate periodEndDate;

    // Filters
    private ArrayList<String> genderFilters = new ArrayList<>();
    private ArrayList<String> ageFilters = new ArrayList<>();
    private ArrayList<String> incomeFilters = new ArrayList<>();
    private ArrayList<String> contextFilters = new ArrayList<>();

    // Attributes
    private String campaignName;
    private CSVData data;
    private Metrics metrics;
    private TimeIntervalMetrics timeIntervalMetrics;
    private HistogramData histogramData;
    private boolean loading;

    public Campaign()
    {
        campaignName = "LOADING";
    }

    public void loadCampaign() throws MissingCampaign, MissingLogFile {
        loading = true;
        Campaign campaign = this;

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Campaign Selector");

        File campaignDirectory = directoryChooser.showDialog(null);
        if ( campaignDirectory == null ) throw new MissingCampaign();

        campaignName = campaignDirectory.getName();

        File[] campaignData = campaignDirectory.listFiles();
        if (campaignData == null) throw new MissingLogFile("click_log");

        // Fetch .csv files in directory
        File serverFile = null, impressionFile = null, clickFile = null;
        for (File file : campaignData) {
            if (file.getName().equals("impression_log.csv")) {
                impressionFile = file;
                continue;
            }
            if (file.getName().equals("click_log.csv")) {
                clickFile = file;
                continue;
            }
            if (file.getName().equals("server_log.csv")) serverFile = file;
        }

        // Throw exception if any of the campaign data is missing
        if (impressionFile == null) throw new MissingLogFile("impression_log");
        if (clickFile == null) throw new MissingLogFile("click_log");
        if (serverFile == null) throw new MissingLogFile("server_log");

        File finalImpressionFile = impressionFile;
        File finalClickFile = clickFile;
        File finalServerFile = serverFile;

        Task<Object> task = new Task<>() {
            @Override
            protected Object call() throws InvalidLogLine, InvalidLogFile {
                data = new CSVData(finalImpressionFile, finalClickFile, finalServerFile);
                periodStartDate = data.getFirstDate().convertToTimeInterval(TimeInterval.DAY);
                periodEndDate = data.getLastDate().convertToTimeInterval(TimeInterval.DAY);
                reloadMetrics();

                loading = false;
                synchronized (campaign) {
                    campaign.notify();
                }

                return ( null );
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public String getCampaignName()
    {
        return ( campaignName );
    }

    public void setCampaignName(String campaignName)
    {
        this.campaignName = campaignName;
    }

    public void reloadMetrics()
    {
        metrics = new Metrics(data, pagesViewedForInteraction, timeViewedForInteraction);
    }

    public void reloadTimeIntervalMetrics()
    {
        timeIntervalMetrics = new TimeIntervalMetrics(data, interval, pagesViewedForInteraction, timeViewedForInteraction);
    }

    public void reloadHistogramData()
    {
        histogramData = new HistogramData(data.getClicks(), 12);
    }

    public CSVData refreshFilters()
    {
        CSVFilter.filter(
                data,
                new Pair<>(
                        periodStartDate,
                        periodEndDate
                ),
                genderFilters,
                ageFilters,
                incomeFilters,
                contextFilters
        );
        return ( data );
    }

    public Metrics getMetrics()
    {
        return ( metrics );
    }

    public TimeIntervalMetrics getMetricsOverTime()
    {
        return ( timeIntervalMetrics );
    }

    public HistogramData getHistogramData()
    {
        return ( histogramData );
    }

    public boolean isLoading() {
        return ( loading );
    }

    public int getPagesViewedForInteraction()
    {
        return ( pagesViewedForInteraction );
    }

    public void setPagesViewedForInteraction(int pagesViewedForInteraction)
    {
        this.pagesViewedForInteraction = pagesViewedForInteraction;
    }

    public ModelDate getTimeViewedForInteraction()
    {
        return ( timeViewedForInteraction );
    }

    public void setTimeViewedForInteraction(ModelDate timeViewedForInteraction)
    {
        this.timeViewedForInteraction = timeViewedForInteraction;
    }

    public CSVData getData() {
        return ( data );
    }

    public TimeInterval getInterval() {
        return ( interval );
    }

    public void setInterval(TimeInterval interval) {
        this.interval = interval;
    }

    public ModelDate getPeriodStartDate() {
        return ( periodStartDate );
    }

    public void setPeriodStartDate(ModelDate startDate) {
        this.periodStartDate = startDate;
    }

    public ModelDate getPeriodEndDate() {
        return ( periodEndDate );
    }

    public void setPeriodEndDate(ModelDate date) {
        this.periodEndDate = date;
    }

    public ArrayList<String> getGenderFilters() {
        return ( this.genderFilters );
    }

    public void setGenderFilters(ArrayList<String> filters) {
        this.genderFilters = filters;
    }

    public ArrayList<String> getAgeFilters() {
        return ( this.ageFilters );
    }

    public void setAgeFilters(ArrayList<String> filters) {
        this.ageFilters = filters;
    }

    public ArrayList<String> getIncomeFilters() {
        return ( this.incomeFilters );
    }

    public void setIncomeFilters(ArrayList<String> filters) {
        this.incomeFilters = filters;
    }

    public ArrayList<String> getContextFilters() {
        return ( this.contextFilters );
    }

    public void setContextFilters(ArrayList<String> filters) {
        this.contextFilters = filters;
    }
}