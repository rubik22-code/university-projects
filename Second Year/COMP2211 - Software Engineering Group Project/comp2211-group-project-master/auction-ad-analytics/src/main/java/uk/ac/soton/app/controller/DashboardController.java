package uk.ac.soton.app.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uk.ac.soton.app.Application;
import uk.ac.soton.app.enums.Privilege;
import uk.ac.soton.app.model.DataBase.DatabaseManager;
import uk.ac.soton.app.model.DataBase.User;
import uk.ac.soton.app.model.State;
import uk.ac.soton.app.enums.TimeInterval;
import uk.ac.soton.app.exceptions.*;
import uk.ac.soton.app.model.Campaign;
import uk.ac.soton.app.model.GraphUtils;
import uk.ac.soton.app.model.ProcessedData.HistogramData;
import uk.ac.soton.app.model.ProcessedData.Metrics;
import uk.ac.soton.app.model.ProcessedData.TimeIntervalMetrics;
import uk.ac.soton.app.model.TimeUnits.ModelDate;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Dashboard-specific controller that acts as a middleman between the UI and the model
 * -    Handles event input to dashboard, such as button clicks
 * -    Updates UI view based on results from model, e.g. updating metric labels
 */
public class DashboardController extends Controller
{
    // Campaign cache
    public Campaign selectedCampaign;
    @FXML
    private Tab selectedCampaignTab;

    // Cache for current graphing data
    private TimeInterval interval;

    // Flag to check if a mismatch warning has already been shown
    private boolean mismatchWarnFlag = false;

    // Components
    @FXML
    private Text name_tag;
    @FXML
    private TabPane campaign_list;
    @FXML
    private StackPane chart_node;
    @FXML
    private LineChart<String, Number> line_chart;
    @FXML
    private BarChart<String, Number> cost_histogram;
    @FXML
    private ImageView loading_wheel;
    @FXML
    private Button uploadButton;
    @FXML
    private Text summary_header;
    @FXML
    private AnchorPane dashboard_view;

    // Misc. Menu Openers
    @FXML
    private Button histogram_toggle;
    @FXML
    private Button copyButton;
    @FXML
    private Button userButton;

    // Windows
    @FXML
    private AnchorPane main_window;
    @FXML
    private Pane shade_window;
    @FXML
    private AnchorPane bounce_window;
    @FXML
    private AnchorPane time_window;
    @FXML
    private AnchorPane filter_window;
    @FXML
    private AnchorPane export_window;
    @FXML
    private AnchorPane user_window;

    // Dashboard statistics
    @FXML
    private Text impressions;
    @FXML
    private Text clicks;
    @FXML
    private Text uniques;
    @FXML
    private Text bounces;
    @FXML
    private Text conversions;
    @FXML
    private Text total_cost;
    @FXML
    private Text ctr;
    @FXML
    private Text cpa;
    @FXML
    private Text cpc;
    @FXML
    private Text cpm;
    @FXML
    private Text bounce_rate;

    // Graph toggle boxes
    @FXML
    private CheckBox impression_toggle;
    @FXML
    private CheckBox click_toggle;
    @FXML
    private CheckBox uniques_toggle;
    @FXML
    private CheckBox bounces_toggle;
    @FXML
    private CheckBox conversions_toggle;
    @FXML
    private CheckBox cost_toggle;
    @FXML
    private CheckBox ctr_toggle;
    @FXML
    private CheckBox cpa_toggle;
    @FXML
    private CheckBox cpc_toggle;
    @FXML
    private CheckBox cpm_toggle;
    @FXML
    private CheckBox bounce_rate_toggle;

    // Bounce definition pop-up
    @FXML
    private Slider page_view_def;
    @FXML
    private Text page_view_deftxt;
    @FXML
    private Slider time_view_def;
    @FXML
    private Text time_view_deftxt;

    // Time settings pop-up
    @FXML
    private DatePicker periodOriginPicker;
    @FXML
    private DatePicker periodEndPicker;
    @FXML
    private ChoiceBox<TimeInterval> intervalPicker;

    // Filter settings
    @FXML
    private CheckBox filterMale;
    @FXML
    private CheckBox filterFemale;
    @FXML
    private CheckBox filterLT25;
    @FXML
    private CheckBox filter25to34;
    @FXML
    private CheckBox filter35to44;
    @FXML
    private CheckBox filter45to54;
    @FXML
    private CheckBox filterGT54;
    @FXML
    private CheckBox filterLow;
    @FXML
    private CheckBox filterMedium;
    @FXML
    private CheckBox filterHigh;
    @FXML
    private CheckBox filterNews;
    @FXML
    private CheckBox filterShopping;
    @FXML
    private CheckBox filterSocialMedia;
    @FXML
    private CheckBox filterBlog;
    @FXML
    private CheckBox filterHobbies;
    @FXML
    private CheckBox filterTravel;

    // User Management Components
    @FXML
    private TextField addUsername;
    @FXML
    private TextField deleteUsername;
    @FXML
    private PasswordField addPassword;
    @FXML
    private CheckBox isEditor;

    // Password Change Menu
    @FXML
    private AnchorPane password_window;
    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmPassword;

    // Top buttons
    @FXML
    private Button logout_button;
    @FXML
    private Button change_password_button;

    // Track whether in loading state
    private boolean loading;
    private boolean managementProcess;

    private Tab add_campaign;

    /**
     * Performs initialisation to set up the scene
     * -    Sets the username message on the dashboard
     * -    Creates important dashboard objects, such as tooltips
     * -    Sets event listeners for UI objects, such as the add tab button
     */
    public void initStage()
    {
        State state = getState();
        User user = state.getUser();

        // Update user-specific details
        name_tag.setText(String.format("%s!", user.username()));
        userButton.setVisible(user.accessLevel() != Privilege.VIEWER);
        isEditor.setDisable(user.accessLevel() != Privilege.ADMIN);
        logger.info("Opening dashboard");

        // Scene has already been initialised
        if ( add_campaign != null ) return;
        logger.info("Initialising dashboard scene");

        // Load existing campaigns
        if ( state.getCampaignCount() > 0 ) {
            copyButton.setVisible(false);
            userButton.setVisible(false);
            logout_button.setVisible(false);
            change_password_button.setVisible(false);

            summary_header.setVisible(true);
            dashboard_view.setVisible(true);

            Tab tab = createCampaignTab(selectedCampaign);
            campaign_list.getSelectionModel().select(tab);
            selectedCampaignTab = tab;

            forceSelectCampaign(selectedCampaign, selectedCampaignTab);
        }

        // Create add campaign button for tab pane
        if ( state.getCampaignCount() == 0 ) {
            add_campaign = new Tab();
            add_campaign.setText("+");
            add_campaign.setClosable(false);
            add_campaign.setId("add_campaign");
            add_campaign.setOnSelectionChanged(event -> addCampaign());
            campaign_list.getTabs().add(add_campaign);
        }

        // Add tooltips to metric labels
        Tooltip tooltip = new Tooltip("The number of people who have seen your ad.\nMeasured in people");
        Tooltip.install(impressions, tooltip);

        tooltip = new Tooltip("The number of people who have clicked your ad.\nMeasured in people");
        Tooltip.install(clicks, tooltip);

        tooltip = new Tooltip("The number of unique people who have clicked your ad.\nMeasured in people");
        Tooltip.install(uniques, tooltip);

        tooltip = new Tooltip("The number of people who have clicked on your ad but didn't engage with the website.\nMeasured in people");
        Tooltip.install(bounces, tooltip);

        tooltip = new Tooltip("The number of people who have clicked on your ad and acted on it.\nMeasured in people");
        Tooltip.install(conversions, tooltip);

        tooltip = new Tooltip("The total cost of your campaign.\nMeasured in GBP(£), rounded to 2 decimal places");
        Tooltip.install(total_cost, tooltip);

        tooltip = new Tooltip("The average number of clicks per impression.\nMeasured in clicks per impression, rounded to 4 decimal places");
        Tooltip.install(ctr, tooltip);

        tooltip = new Tooltip("The amount of money spent on average for each conversion.\nMeasured in GBP(£), rounded to 3 decimal places");
        Tooltip.install(cpa, tooltip);

        tooltip = new Tooltip("The amount of money spent on average for each click.\nMeasured in GBP(£), rounded to 3 decimal places");
        Tooltip.install(cpc, tooltip);

        tooltip = new Tooltip("The amount of money spent on average for every thousand impressions.\nMeasured in GBP(£), rounded to 3 decimal places");
        Tooltip.install(cpm, tooltip);

        tooltip = new Tooltip("The average number of bounces for every click.\nMeasured in bounces per click, rounded to 4 decimal places");
        Tooltip.install(bounce_rate, tooltip);

        /* Set loading wheel image */
        Image image = new Image(Objects.requireNonNull(Application.class.getResource("loading.gif")).toExternalForm(), false);
        loading_wheel.setImage(image);

        /* Handle bounce definition pop-up */
        page_view_def.valueProperty().addListener((observableValue, oldValue, newValue)
                -> page_view_deftxt.setText(new DecimalFormat("#").format(newValue)));
        time_view_def.valueProperty().addListener((observableValue, oldValue, newValue)
                -> time_view_deftxt.setText(new DecimalFormat("#").format(newValue)));

        /* Handle time settings pop-up */
        intervalPicker.getItems().add(TimeInterval.HOUR);
        intervalPicker.getItems().add(TimeInterval.DAY);
        intervalPicker.getItems().add(TimeInterval.WEEK);
        intervalPicker.getItems().add(TimeInterval.MONTH);
        intervalPicker.setValue(TimeInterval.DAY);
    }

    private void reloadCampaign()
    {
        Campaign campaign = this.selectedCampaign;
        Tab campaignTab = this.selectedCampaignTab;

        logger.info("Reloading campaign {}", campaign.getCampaignName());

        this.selectedCampaign = null;
        this.selectedCampaignTab = null;

        selectCampaign(campaign, campaignTab);
    }

    private void addFilter(ArrayList<String> filter, CheckBox filterBox)
    {
        if ( filterBox.isSelected() ) filter.add(filterBox.getText());
    }

    public void uploadCampaign()
    {

    }

    public void addUser()
    {
        if ( managementProcess ) return;
        managementProcess = true;

        String username = addUsername.getText();
        Privilege privilege = Privilege.VIEWER;
        if ( isEditor.isSelected() ) privilege = Privilege.EDITOR;
        try {
            getState().getDatabaseManager().createUser(getState().getUser(), username, addPassword.getText(), privilege);
        } catch ( ApplicationException e ) {
            logger.info("Failed to create new user {}", username);
            e.displayError();
            return;
        }
        addUsername.clear();
        addPassword.clear();
        logger.info("Successfully created new user {} with privilege {}", username, privilege);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Successfully created new user " + username);
        alert.showAndWait();

        managementProcess = false;
    }

    public void deleteUser()
    {
        if ( managementProcess ) return;
        managementProcess = true;

        String username = deleteUsername.getText();
        try {
            getState().getDatabaseManager().deleteUser(getState().getUser(), username);
        } catch ( ApplicationException e ) {
            logger.info("Failed to delete user {}", username);
            e.displayError();
            return;
        }
        deleteUsername.clear();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Successfully deleted user " + username);
        alert.showAndWait();

        managementProcess = false;
    }

    public void closeUserManager()
    {
        user_window.setVisible(false);
        shade_window.setVisible(false);
        main_window.setEffect(null);

        logger.info("Closing user management window");
    }

    public void openUserManager()
    {
        if ( loading ) return;

        main_window.setEffect(new GaussianBlur());
        shade_window.setVisible(true);
        user_window.setVisible(true);

        logger.info("Opening user management window");
    }

    public void logout()
    {
        logger.info("Logging out of user {}", getState().getUser().username());

        getState().clearCampaigns();
        setScene("login");
    }

    public void printCampaign()
    {
        logger.info("Attempting to print campaign data");

        // Create printer job based on computer's default printer
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob(printer);

        // Check that print dialogue is not closed
        if ( !job.showPrintDialog(stage) ) return;

        // Setup default page layout
        PageLayout layout = printer.createPageLayout(
                Paper.A4,
                PageOrientation.LANDSCAPE,
                Printer.MarginType.DEFAULT
        );
        job.getJobSettings().setPageLayout(layout);

        // Convert entire scene to a single image view
        main_window.setEffect(null);
        export_window.setVisible(false);
        WritableImage snapshot = main_window.snapshot(new SnapshotParameters(), null);
        ImageView image = new ImageView(snapshot);
        export_window.setVisible(true);
        main_window.setEffect(new GaussianBlur());

        // Set image view to scale to the print layout
        image.setPreserveRatio(true);
        image.setFitHeight(layout.getPrintableHeight());
        image.setFitWidth(layout.getPrintableWidth());

        // Attempt to print
        if ( job.printPage(image) ) job.endJob();
    }

    public void exportSummary()
    {
        logger.info("Attempting to export campaign summary");

        // Get destination file
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "CSV files (*.csv)",
                "*.csv"
        );
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);
        if ( file == null ) return;
        logger.info("Attempting to export summary to file {}", file.getName());

        // Save to file
        try {
            PrintWriter writer = new PrintWriter(file);

            // Output headings
            writer.println(
                    "Timestamp,Impressions,Clicks,Uniques,Bounces,Conversions,Cost,CTR,CPA,CPC,CPM,Bounce_Rate"
            );

            // Layout setup
            int pointCounter = 0;
            HashMap<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

            // Import values
            List<XYChart.Series<String, Number>> seriesList = line_chart.getData();
            for ( XYChart.Series<String, Number> series : seriesList ) {
                seriesMap.put(series.getName(), series);
                pointCounter = series.getData().size();
            }

            // Process layout
            for ( int i = 0; i < pointCounter; i++ ) {
                final int index = i;
                final String[] timestamp = new String[1];

                // Extract point values
                HashMap<String, Number> point = new HashMap<>();
                point.put("Impressions", -1);
                point.put("Clicks", -1);
                point.put("Uniques", -1);
                point.put("Bounces", -1);
                point.put("Conversions", -1);
                point.put("Costs", -1);
                point.put("CTR", -1);
                point.put("CPA", -1);
                point.put("CPC", -1);
                point.put("CPM", -1);
                point.put("Bounce Rate", -1);
                seriesMap.forEach((String name, XYChart.Series<String, Number> series) -> {
                    if ( timestamp[0] == null ) timestamp[0] = series.getData().get(index).getXValue();
                    point.put(name, series.getData().get(index).getYValue());
                });

                // Output values at timestamp
                String time = timestamp[0];
                writer.println(String.format(
                        "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        time,
                        point.get("Impressions"),
                        point.get("Clicks"),
                        point.get("Uniques"),
                        point.get("Bounces"),
                        point.get("Conversions"),
                        point.get("Costs"),
                        point.get("CTR"),
                        point.get("CPA"),
                        point.get("CPC"),
                        point.get("CPM"),
                        point.get("Bounce Rate")
                ));
            }
            logger.info("Successfully exported summary to file {}", file.getName());

            // Terminate writer
            writer.close();
        } catch ( FileNotFoundException e ) {
            logger.info("Failed to export summary to file {}", file.getName());
        }
    }

    public void exportGraph()
    {
        logger.info("Attempting to export campaign graph");

        // Convert currently opened graph to a writable image
        WritableImage snapshot = chart_node.snapshot(new SnapshotParameters(), null);

        // Convert writable image to a rendered image
        RenderedImage image = SwingFXUtils.fromFXImage(snapshot, null);

        // Get destination file
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "PNG files (*.png)",
                "*.png"
        );
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);
        if ( file == null ) return;
        logger.info("Attempting to export graph to file {}", file.getName());

        // Save to file
        try {
            ImageIO.write(
                    image,
                    "png",
                    file
            );
            logger.info("Successfully exported graph to file {}", file.getName());
        } catch (IOException e) {
            logger.info("Failed to export graph to file {}", file.getName());
            new ExportFailure().displayError();
        }
    }

    public void openPasswordChange()
    {
        if ( loading ) return;

        main_window.setEffect(new GaussianBlur());
        shade_window.setVisible(true);
        password_window.setVisible(true);
    }

    public void closePasswordChange()
    {
        password_window.setVisible(false);
        shade_window.setVisible(false);
        main_window.setEffect(null);
    }

    public void changePassword()
    {
        String oldPwd = oldPassword.getText();
        String newPwd = newPassword.getText();
        String pwd    = confirmPassword.getText();

        oldPassword.clear();
        newPassword.clear();
        confirmPassword.clear();

        if ( !Objects.equals(pwd, newPwd) ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Entered passwords don't match");
            alert.showAndWait();
            return;
        }

        DatabaseManager databaseManager = getState().getDatabaseManager();
        boolean isValid;
        try {
            isValid = databaseManager.checkPassword(getState().getUser(), oldPwd);
        } catch ( ApplicationException e ) {
            e.displayError();
            return;
        }
        if ( !isValid ) { new InvalidLogin().displayError(); return; }
        try {
            databaseManager.changePassword(getState().getUser(), pwd);
        } catch ( ApplicationException e ) {
            e.displayError();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Successfully changed password");
        alert.showAndWait();
    }

    public void closeExportWindow()
    {
        export_window.setVisible(false);
        shade_window.setVisible(false);
        main_window.setEffect(null);

        logger.info("Closing export window");
    }

    public void openExportWindow()
    {
        if ( loading ) return;

        main_window.setEffect(new GaussianBlur());
        shade_window.setVisible(true);
        export_window.setVisible(true);

        logger.info("Opening export window");
    }

    public void applyFilterChanges()
    {
        logger.info("Updating filters");

        ArrayList<String> genderFilters = new ArrayList<>();
        ArrayList<String> currentGenderFilters = selectedCampaign.getGenderFilters();
        ArrayList<String> ageFilters = new ArrayList<>();
        ArrayList<String> currentAgeFilters = selectedCampaign.getAgeFilters();
        ArrayList<String> incomeFilters = new ArrayList<>();
        ArrayList<String> currentIncomeFilters = selectedCampaign.getIncomeFilters();
        ArrayList<String> contextFilters = new ArrayList<>();
        ArrayList<String> currentContextFilters = selectedCampaign.getContextFilters();

        /* Read in gender filters */
        addFilter(genderFilters, filterMale);
        addFilter(genderFilters, filterFemale);

        /* Read in age filters */
        addFilter(ageFilters, filterLT25);
        addFilter(ageFilters, filter25to34);
        addFilter(ageFilters, filter35to44);
        addFilter(ageFilters, filter45to54);
        addFilter(ageFilters, filterGT54);

        /* Read in income filters */
        addFilter(incomeFilters, filterLow);
        addFilter(incomeFilters, filterMedium);
        addFilter(incomeFilters, filterHigh);

        /* Read in context filters */
        addFilter(contextFilters, filterNews);
        addFilter(contextFilters, filterShopping);
        addFilter(contextFilters, filterSocialMedia);
        addFilter(contextFilters, filterBlog);
        addFilter(contextFilters, filterHobbies);
        addFilter(contextFilters, filterTravel);

        /* Update data */
        selectedCampaign.setGenderFilters(genderFilters);
        selectedCampaign.setAgeFilters(ageFilters);
        selectedCampaign.setIncomeFilters(incomeFilters);
        selectedCampaign.setContextFilters(contextFilters);

        /* Hide filter menu */
        filter_window.setVisible(false);
        shade_window.setVisible(false);
        main_window.setEffect(null);
        logger.info("Closing filter menu");

        /* Exit early if no reloading required */
        if (    genderFilters.equals(currentGenderFilters)
                && ageFilters.equals(currentAgeFilters)
                && incomeFilters.equals(currentIncomeFilters)
                && contextFilters.equals(currentContextFilters)
        ) return;

        /* Re-select the current campaign */
        reloadCampaign();
    }

    public void openCopy()
    {
        logger.info("Cloning campaign for comparison");

        /* Create stage */
        Stage stage = new Stage();

        /* Add state reference to new stage */
        State state = (State) this.stage.getUserData();
        stage.setUserData(state);

        /* Fetch scene root */
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("dashboard.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            logger.info("Failed to load new dashboard.fxml");
            stage.close();
        }
        DashboardController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setLogger(logger);
        controller.selectedCampaign = selectedCampaign;
        controller.initStage();

        /* Prepare scene */
        Scene scene = new Scene(root);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(state.getStyle().asPath());

        /* Prepare stage information */
        stage.setTitle("Dashboard");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
    }

    public void applyTimeSettings()
    {
        logger.info("Updating time settings");

        /* Read in time interval data */
        TimeInterval interval = intervalPicker.getValue();
        if ( interval == null ) interval = TimeInterval.DAY;
        TimeInterval currentInterval = selectedCampaign.getInterval();
        logger.info("New interval: {}", interval);

        /* Read in start date data */
        LocalDate date = periodOriginPicker.getValue();
        ModelDate startDate = selectedCampaign.getPeriodStartDate();
        if ( date != null )
            startDate = new ModelDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0, 0);
        ModelDate currentStartDate = selectedCampaign.getPeriodStartDate();
        logger.info("New start date: {}", startDate.asString());

        /* Read in end date data */
        LocalDate date2 = periodEndPicker.getValue();
        ModelDate endDate = selectedCampaign.getPeriodEndDate();
        if ( date != null )
            endDate = new ModelDate(date2.getYear(), date2.getMonthValue(), date2.getDayOfMonth(), 0, 0, 0);
        ModelDate currentEndDate = selectedCampaign.getPeriodEndDate();
        logger.info("New end date: {}", endDate.asString());

        /* Check data validity */
        if ( endDate.isLessThan(startDate) ) {
            logger.info("New start date is later than the new end date");
            new InvalidDateCombination().displayError();
            return;
        }

        /* Update data */
        selectedCampaign.setInterval(interval);
        selectedCampaign.setPeriodStartDate(startDate);
        selectedCampaign.setPeriodEndDate(endDate);
        logger.info("Updated campaign time settings");

        /* Hide time settings menu */
        time_window.setVisible(false);
        shade_window.setVisible(false);
        main_window.setEffect(null);
        logger.info("Closing time settings window");

        /* Exit early if no reloading required */
        if (    interval.equals(currentInterval)
                && startDate.isEqualTo(currentStartDate)
                && endDate.isEqualTo(currentEndDate)
        ) return;

        /* Re-select the current campaign */
        reloadCampaign();
    }

    public void openTimeSettings()
    {
        if ( loading ) return;

        main_window.setEffect(new GaussianBlur());
        shade_window.setVisible(true);
        time_window.setVisible(true);

        logger.info("Opened time settings window");
    }

    public void openFilters()
    {
        if ( loading ) return;

        main_window.setEffect(new GaussianBlur());
        shade_window.setVisible(true);
        filter_window.setVisible(true);

        logger.info("Opened filters window");
    }

    public void toggleHistogram()
    {
        if ( loading ) return;

        line_chart.setVisible(!line_chart.isVisible());
        if ( cost_histogram.isVisible() ) {
            histogram_toggle.setText("Cost Histogram");
            logger.info("Closing histogram, opening trends");
        } else {
            histogram_toggle.setText("Trend Chart");
            logger.info("Opening histogram, closing trends");
            refreshHistogram();
        }
        cost_histogram.setVisible(!cost_histogram.isVisible());
    }

    private void refreshHistogram()
    {
        logger.info("Refreshing histogram");
        cost_histogram.getData().clear();
        selectedCampaign.reloadHistogramData();
        HistogramData histogramData = selectedCampaign.getHistogramData();
        cost_histogram.getData().add(GraphUtils.getClickCostHistogramSeries(histogramData));
        logger.info("Refreshed histogram");
    }

    public void openBounceDefinition()
    {
        if ( loading ) return;

        main_window.setEffect(new GaussianBlur());
        shade_window.setVisible(true);
        bounce_window.setVisible(true);

        logger.info("Opened bounce definition window");
    }

    public void applyBounceDefinition()
    {
        int pages = Integer.parseInt(page_view_deftxt.getText());

        int minutes = Integer.parseInt(time_view_deftxt.getText());
        ModelDate timeViewedDefinition = new ModelDate(0, 0, 0,minutes / 60, minutes % 60, 0);

        int currentPages = selectedCampaign.getPagesViewedForInteraction();
        ModelDate currentTimeViewed = selectedCampaign.getTimeViewedForInteraction();

        selectedCampaign.setPagesViewedForInteraction(pages);
        selectedCampaign.setTimeViewedForInteraction(timeViewedDefinition);
        logger.info("Updated bounce pages to {}", pages);
        logger.info("Updated bounce minutes to {}", minutes);

        /* Hide bounce definition menu */
        bounce_window.setVisible(false);
        shade_window.setVisible(false);
        main_window.setEffect(null);
        logger.info("Closed bounce definition window");

        /* Exit early if no reloading required */
        if ( currentPages == pages && timeViewedDefinition.isEqualTo(currentTimeViewed) ) return;

        /* Re-select current campaign */
        reloadCampaign();
    }

    /**
     * Creates a tab for a new campaign
     * @param campaign the campaign that will be represented when clicking on the tab
     */
    private Tab createCampaignTab(Campaign campaign)
    {
        logger.info("Created new campaign tab for {}", campaign.getCampaignName());
        Tab campaignTab = new Tab();
        campaignTab.setOnSelectionChanged(event -> selectCampaign(campaign, campaignTab));
        campaignTab.setText(campaign.getCampaignName());
        List<Tab> tabs = campaign_list.getTabs();
        tabs.add(Math.max(0, tabs.size() - 1), campaignTab);
        return ( campaignTab );
    }

    /**
     * Creates a new (default) campaign object, handling errors
     * @return the new campaign
     */
    private Campaign createCampaign()
    {
        Campaign campaign = new Campaign();
        try {
            campaign.loadCampaign();
        } catch ( ApplicationException e ) {
            logger.info("Error while creating new campaign: {}", e.getMessage());
            e.displayError();
            return ( null );
        } catch ( Exception ignore ) {
            logger.info("Unexpected error while creating new campaign");
            return ( null );
        }
        logger.info("Successfully created new campaign");
        return ( campaign );
    }

    /**
     * Triggered when a default campaign is selected
     * @param campaign the campaign that has been selected
     */
    private void forceSelectCampaign(Campaign campaign, Tab campaignTab)
    {
        if ( loading ) {
            campaign_list.getSelectionModel().select(selectedCampaignTab);
            return;
        }
        logger.info("Selecting campaign {}", campaign.getCampaignName());

        selectedCampaign = campaign;
        selectedCampaignTab = campaignTab;

        loading = true;

        /* Fetch required settings / filters */
        TimeInterval interval = selectedCampaign.getInterval();

        line_chart.getData().clear();
        cost_histogram.getData().clear();

        shade_window.setVisible(true);
        impressions.setText("...");
        clicks.setText("...");
        uniques.setText("...");
        bounces.setText("...");
        conversions.setText("...");
        total_cost.setText("...");
        ctr.setText("...");
        cpa.setText("...");
        cpc.setText("...");
        cpm.setText("...");
        bounce_rate.setText("...");

        /* Enable loading wheel */
        /* LOADING GIF GENERATED AT: https://loading.io/ */
        loading_wheel.setVisible(true);

        /* Start data thread */
        Thread thread = updateDataThread(interval);
        thread.start();
    }

    /**
     * Triggered when a campaign tab is selected
     * @param campaign the campaign that has been selected
     */
    private void selectCampaign(Campaign campaign, Tab campaignTab)
    {
        if ( Objects.equals(campaign, selectedCampaign) ) return;
        forceSelectCampaign(campaign, campaignTab);
    }

    /**
     * Creates a new thread that handles the updating of data when switching campaigns
     * @param interval the time interval to display data by
     * @return the new thread
     */
    private Thread updateDataThread(TimeInterval interval) {
        DashboardController controller = this;

        final Campaign currentCampaign = selectedCampaign;
        Task<Object> task = new Task<>() {
            @Override
            protected Object call()
            {
                if ( selectedCampaign.isLoading() ) synchronized (currentCampaign) {
                    try {
                        selectedCampaign.wait();
                    } catch (Exception ignore) {}
                }

                selectedCampaign.refreshFilters();
                selectedCampaign.reloadMetrics();
                selectedCampaign.reloadTimeIntervalMetrics();
                Metrics metrics = selectedCampaign.getMetrics();
                TimeIntervalMetrics timeIntervalMetrics = selectedCampaign.getMetricsOverTime();

                controller.interval = interval;
                loading = false;

                /* Update display */
                Platform.runLater(() -> {
                    /* Modify bounce definition to reflect current campaign */
                    page_view_def.setValue(selectedCampaign.getPagesViewedForInteraction());
                    page_view_deftxt.setText(String.valueOf(selectedCampaign.getPagesViewedForInteraction()));
                    time_view_def.setValue(selectedCampaign.getTimeViewedForInteraction().minute());
                    time_view_deftxt.setText(String.valueOf(selectedCampaign.getTimeViewedForInteraction().minute()));

                    /* Modify time settings to reflect current campaign */
                    ModelDate startDate = selectedCampaign.getPeriodStartDate();
                    periodOriginPicker.setValue(LocalDate.of(startDate.year(), startDate.month(), startDate.day()));
                    ModelDate endDate = selectedCampaign.getPeriodEndDate();
                    periodEndPicker.setValue(LocalDate.of(endDate.year(), endDate.month(), endDate.day()));

                    /* Modify filters to reflect current campaign */
                    List<String> genderFilters = currentCampaign.getGenderFilters();
                    List<String> ageFilters = currentCampaign.getAgeFilters();
                    List<String> incomeFilters = currentCampaign.getIncomeFilters();
                    List<String> contextFilters = currentCampaign.getContextFilters();

                    filterMale.setSelected(genderFilters.contains(filterMale.getText()));
                    filterFemale.setSelected(genderFilters.contains(filterFemale.getText()));

                    filterLT25.setSelected(ageFilters.contains(filterLT25.getText()));
                    filter25to34.setSelected(ageFilters.contains(filter25to34.getText()));
                    filter35to44.setSelected(ageFilters.contains(filter35to44.getText()));
                    filter45to54.setSelected(ageFilters.contains(filter45to54.getText()));
                    filterGT54.setSelected(ageFilters.contains(filterGT54.getText()));

                    filterLow.setSelected(incomeFilters.contains(filterLow.getText()));
                    filterMedium.setSelected(incomeFilters.contains(filterMedium.getText()));
                    filterHigh.setSelected(incomeFilters.contains(filterHigh.getText()));

                    filterNews.setSelected(contextFilters.contains(filterNews.getText()));
                    filterShopping.setSelected(contextFilters.contains(filterShopping.getText()));
                    filterSocialMedia.setSelected(contextFilters.contains(filterSocialMedia.getText()));
                    filterBlog.setSelected(contextFilters.contains(filterBlog.getText()));
                    filterHobbies.setSelected(contextFilters.contains(filterHobbies.getText()));
                    filterTravel.setSelected(contextFilters.contains(filterTravel.getText()));

                    /* Reload main view */
                    reloadDashboardView(metrics);
                    reloadGraphView(timeIntervalMetrics, interval);
                    refreshHistogram();

                    /* Disable loading wheel */
                    loading_wheel.setVisible(false);
                    shade_window.setVisible(false);
                });

                return ( null );
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);

        return ( thread );
    }

    /**
     * Reloads the dashboard metrics based on the open campaign
     */
    private void reloadDashboardView(Metrics data)
    {
        logger.info("Reloading dashboard view");
        impressions.setText(new DecimalFormat("#,###").format(data.getImpressions()));
        clicks.setText(new DecimalFormat("#,###").format(data.getClicks()));
        uniques.setText(new DecimalFormat("#,###").format(data.getUniques()));
        bounces.setText(new DecimalFormat("#,###").format(data.getBounces()));
        conversions.setText(new DecimalFormat("#,###").format(data.getConversions()));
        total_cost.setText(new DecimalFormat("£#,##0.00").format(data.getTotalCost()));
        ctr.setText(new DecimalFormat("#,##0.0000").format(data.getCTR()));
        cpa.setText(new DecimalFormat("£#,##0.000").format(data.getCPA()));
        cpc.setText(new DecimalFormat("£#,##0.000").format(data.getCPC()));
        cpm.setText(new DecimalFormat("£#,##0.000").format(data.getCPM()));
        bounce_rate.setText(new DecimalFormat("#,##0.0000").format(data.getBounceRate()));
    }

    /**
     * Reloads the dashboard graph based on the open campaign
     */
    private void reloadGraphView(TimeIntervalMetrics timeIntervalMetrics, TimeInterval interval)
    {
        Boolean shouldWarn = false;
        logger.info("Reloading graph view");
        line_chart.getData().clear();

        /* Enable different series views based on user toggles */
        // Impressions
        if ( impression_toggle.isSelected() ) {
            if ( cost_toggle.isSelected() || ctr_toggle.isSelected() || cpm_toggle.isSelected() || cpa_toggle.isSelected() || cpc_toggle.isSelected() || bounce_rate_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical impressions");
            XYChart.Series<String, Number> series = GraphUtils.getImpressionSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("#,###").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // Clicks
        if ( click_toggle.isSelected() ) {
            if ( cost_toggle.isSelected() || ctr_toggle.isSelected() || cpm_toggle.isSelected() || cpa_toggle.isSelected() || cpc_toggle.isSelected() || bounce_rate_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical clicks");
            XYChart.Series<String, Number> series = GraphUtils.getClickSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("#,###").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // Uniques
        if ( uniques_toggle.isSelected() ) {
            if ( cost_toggle.isSelected() || ctr_toggle.isSelected() || cpm_toggle.isSelected() || cpa_toggle.isSelected() || cpc_toggle.isSelected() || bounce_rate_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical uniques");
            XYChart.Series<String, Number> series = GraphUtils.getUniqueSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("#,###").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // Bounces
        if ( bounces_toggle.isSelected() ) {
            if ( cost_toggle.isSelected() || ctr_toggle.isSelected() || cpm_toggle.isSelected() || cpa_toggle.isSelected() || cpc_toggle.isSelected() || bounce_rate_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical bounces");
            XYChart.Series<String, Number> series = GraphUtils.getBounceSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("#,###").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // Conversions
        if ( conversions_toggle.isSelected() ) {
            if ( cost_toggle.isSelected() || ctr_toggle.isSelected() || cpm_toggle.isSelected() || cpa_toggle.isSelected() || cpc_toggle.isSelected() || bounce_rate_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical conversions");
            XYChart.Series<String, Number> series = GraphUtils.getConversionSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("#,###").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // Costs
        if ( cost_toggle.isSelected() ) {
            if ( ctr_toggle.isSelected() || cpm_toggle.isSelected() || cpa_toggle.isSelected() || cpc_toggle.isSelected() || bounce_rate_toggle.isSelected() || impression_toggle.isSelected() || uniques_toggle.isSelected() || conversions_toggle.isSelected() || click_toggle.isSelected() || bounces_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical costs");
            XYChart.Series<String, Number> series = GraphUtils.getCostSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("£#,##0.00").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // CTR
        if ( ctr_toggle.isSelected() ) {
            if ( impression_toggle.isSelected() || uniques_toggle.isSelected() || conversions_toggle.isSelected() || click_toggle.isSelected() || bounces_toggle.isSelected() || cost_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical CTR");
            XYChart.Series<String, Number> series = GraphUtils.getCTRSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("#,##0.0000").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // CPA
        if ( cpa_toggle.isSelected() ) {
            if ( impression_toggle.isSelected() || uniques_toggle.isSelected() || conversions_toggle.isSelected() || click_toggle.isSelected() || bounces_toggle.isSelected() || cost_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical CPA");
            XYChart.Series<String, Number> series = GraphUtils.getCPASeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("£#,##0.0000").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // CPC
        if ( cpc_toggle.isSelected() ) {
            if ( impression_toggle.isSelected() || uniques_toggle.isSelected() || conversions_toggle.isSelected() || click_toggle.isSelected() || bounces_toggle.isSelected() || cost_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical CPC");
            XYChart.Series<String, Number> series = GraphUtils.getCPCSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("£#,##0.0000").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // CPM
        if ( cpm_toggle.isSelected() ) {
            if ( impression_toggle.isSelected() || uniques_toggle.isSelected() || conversions_toggle.isSelected() || click_toggle.isSelected() || bounces_toggle.isSelected() || cost_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical CPM");
            XYChart.Series<String, Number> series = GraphUtils.getCPMSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for ( XYChart.Data<String, Number> point : series.getData() ) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("£#,##0.000").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }

        // Bounce Rate
        if ( bounce_rate_toggle.isSelected() ) {
            if ( impression_toggle.isSelected() || uniques_toggle.isSelected() || conversions_toggle.isSelected() || click_toggle.isSelected() || bounces_toggle.isSelected() || cost_toggle.isSelected() ) shouldWarn = true;
            logger.info("Enabling graphical bounce rate");
            XYChart.Series<String, Number> series = GraphUtils.getBounceRateSeries(timeIntervalMetrics, interval);
            line_chart.getData().add(series);
            for (XYChart.Data<String, Number> point : series.getData()) {
                Tooltip tooltip = new Tooltip(String.format(
                        "%s: %s",
                        point.getXValue(),
                        new DecimalFormat("#,##0.0000").format(point.getYValue())
                ));
                Tooltip.install(point.getNode(), tooltip);
            }
        }
        logger.info("Finished reloading graph view");

        if ( mismatchWarnFlag || !shouldWarn ) return;
        mismatchWarnFlag = true;

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Metrics with different units of measurement have been selected, please take care to avoid this by keeping the unit of measurement consistent between all graphed metrics");
        alert.showAndWait();
    }

    public void toggleMetric()
    {
        if ( loading ) return;
        reloadGraphView(selectedCampaign.getMetricsOverTime(), interval);
    }

    /**
     * Triggered when the (+) button on the campaign tab list is selected
     * -    Checks if it was deselected, in which case the request is ignored
     * -    Requests campaign details and creates associated object
     * -    Adds new tab to campaign tab list
     */
    private void addCampaign()
    {
        if ( loading ) {
            campaign_list.getSelectionModel().select(selectedCampaignTab);
            return;
        }
        if ( !add_campaign.isSelected() ) return;
        campaign_list.getSelectionModel().select(selectedCampaignTab);

        State state = getState();
        Campaign campaign = createCampaign();
        if ( campaign == null ) return;

        try {
            state.registerCampaign(campaign);
        } catch ( CampaignAlreadyExists e ) {
            logger.info("Attempted to register a campaign that has already been uploaded to the system");
            e.displayError();
            return;
        }

        campaign_list.getSelectionModel().select(createCampaignTab(campaign));
    }
}
