package uk.ac.soton.app.model.CSVData;

import javafx.util.Pair;
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
 * Class used to test the CSVFilter class
 */
public class CSVFilterTest {

    //Inputs for the tests in this class
    List<CSVData> inputs;

    //Expected outputs for the tests in this class
    List<CSVData> expectedOutputs;

    /**
     * Sets up everything needed to test the CSVFilter class
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     */
    @BeforeEach
    void setUp() throws InvalidLogLine, InvalidLogFile {
        setUpTestInputs();
        setUpTestExpectedOutputs();
    }

    /**
     * Sets up inputs needed to test the CSVFilter class
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     */
    void setUpTestInputs() throws InvalidLogLine, InvalidLogFile {
        //Sets up an input for tests #1, #3, #6 in the testFilter() method
        CSVData input1 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv"));
        for (int i = 0; i < 6; i++) {input1.getImpressions().get(i).setVisible(true);}
        for (int i = 6; i < 12; i++) {input1.getImpressions().get(i).setVisible(false);}
        for (int i = 0; i < 6; i++) {input1.getClicks().get(i).setVisible(true);}
        for (int i = 6; i < 12; i++) {input1.getClicks().get(i).setVisible(false);}
        for (int i = 0; i < 6; i++) {input1.getInteractions().get(i).setVisible(true);}
        for (int i = 6; i < 12; i++) {input1.getInteractions().get(i).setVisible(false);}
        this.inputs = new ArrayList<>();
        inputs.add(input1);

        //Sets up an input for test #2 in the testFilter() method
        CSVData input2 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv"));
        for (int i = 0; i < 6; i++) {input2.getImpressions().get(i).setVisible(false);}
        for (int i = 6; i < 12; i++) {input2.getImpressions().get(i).setVisible(true);}
        for (int i = 0; i < 6; i++) {input2.getClicks().get(i).setVisible(false);}
        for (int i = 6; i < 12; i++) {input2.getClicks().get(i).setVisible(true);}
        for (int i = 0; i < 6; i++) {input2.getInteractions().get(i).setVisible(false);}
        for (int i = 6; i < 12; i++) {input2.getInteractions().get(i).setVisible(true);}
        inputs.add(input2);

        //Sets up an input for test #4 in the testFilter() method
        inputs.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog2.csv")));

        //Sets up an input for test #5 in the testFilter() method
        inputs.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv")));

        //Sets up an input for test #7 in the testFilter() method
        inputs.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")));

        //Sets up an input for test #1 in the clearFilters() method
        CSVData input3 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv"));
        for (int i = 0; i < 12; i++) {input3.getImpressions().get(i).setVisible(false);}
        for (int i = 0; i < 12; i++) {input3.getClicks().get(i).setVisible(false);}
        for (int i = 0; i < 12; i++) {input3.getInteractions().get(i).setVisible(false);}
        inputs.add(input3);

        //Sets up an input for test #2 in the clearFilters() method
        inputs.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv")));
    }

    /**
     * Sets up the expected outputs of the tests on the CSVFilter class
     * @throws InvalidLogLine exception thrown when a CSV file contains a line in an invalid format
     * @throws InvalidLogFile exception thrown when a CSV file can't be read
     */
    void setUpTestExpectedOutputs() throws InvalidLogLine, InvalidLogFile {
        //Sets up the expected output for test #1 in the testFilter() method
        CSVData expectedOutput1 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv"));
        for (int i = 0; i < 12; i++) {if (i%2 == 0) expectedOutput1.getImpressions().get(i).setVisible(true);}
        for (int i = 0; i < 12; i++) {if (i%2 != 0) expectedOutput1.getImpressions().get(i).setVisible(false);}
        for (int i = 0; i < 12; i++) {if (i%2 == 0) expectedOutput1.getClicks().get(i).setVisible(true);}
        for (int i = 0; i < 12; i++) {if (i%2 != 0) expectedOutput1.getClicks().get(i).setVisible(false);}
        for (int i = 0; i < 12; i++) {if (i%2 == 0) expectedOutput1.getInteractions().get(i).setVisible(true);}
        for (int i = 0; i < 12; i++) {if (i%2 != 0) expectedOutput1.getInteractions().get(i).setVisible(false);}
        this.expectedOutputs = new ArrayList<>();
        expectedOutputs.add(expectedOutput1);

        //Sets up the expected output for test #2 in the testFilter() method
        CSVData expectedOutput2 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv"));
        for (int i = 0; i < 12; i++) {if (i%2 == 0) expectedOutput2.getImpressions().get(i).setVisible(false);}
        for (int i = 0; i < 12; i++) {if (i%2 != 0) expectedOutput2.getImpressions().get(i).setVisible(true);}
        for (int i = 0; i < 12; i++) {if (i%2 == 0) expectedOutput2.getClicks().get(i).setVisible(false);}
        for (int i = 0; i < 12; i++) {if (i%2 != 0) expectedOutput2.getClicks().get(i).setVisible(true);}
        for (int i = 0; i < 12; i++) {if (i%2 == 0) expectedOutput2.getInteractions().get(i).setVisible(false);}
        for (int i = 0; i < 12; i++) {if (i%2 != 0) expectedOutput2.getInteractions().get(i).setVisible(true);}
        expectedOutputs.add(expectedOutput2);

        //Sets up the expected output for test #3 in the testFilter() method
        CSVData expectedOutput3 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv"));
        for (int i = 0; i < 9; i++) {if (i%2 == 0) expectedOutput3.getImpressions().get(i).setVisible(true);}
        for (int i = 0; i < 9; i++) {if (i%2 != 0) expectedOutput3.getImpressions().get(i).setVisible(false);}
        for (int i = 9; i < 12; i++) {expectedOutput3.getImpressions().get(i).setVisible(false);}
        for (int i = 0; i < 9; i++) {if (i%2 == 0) expectedOutput3.getClicks().get(i).setVisible(true);}
        for (int i = 0; i < 9; i++) {if (i%2 != 0) expectedOutput3.getClicks().get(i).setVisible(false);}
        for (int i = 9; i < 12; i++) {expectedOutput3.getClicks().get(i).setVisible(false);}
        for (int i = 0; i < 9; i++) {if (i%2 == 0) expectedOutput3.getInteractions().get(i).setVisible(true);}
        for (int i = 0; i < 9; i++) {if (i%2 != 0) expectedOutput3.getInteractions().get(i).setVisible(false);}
        for (int i = 9; i < 12; i++) {expectedOutput3.getInteractions().get(i).setVisible(false);}
        expectedOutputs.add(expectedOutput3);

        //Sets up the expected output for test #4 in the testFilter() method
        CSVData expectedOutput4 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog3.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog2.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog2.csv"));
        for (int i = 0; i < 3; i++) {expectedOutput4.getImpressions().get(i).setVisible(true);}
        for (int i = 3; i < 9; i++) {expectedOutput4.getImpressions().get(i).setVisible(false);}
        for (int i = 9; i < 12; i++) {expectedOutput4.getImpressions().get(i).setVisible(true);}
        for (int i = 0; i < 3; i++) {expectedOutput4.getClicks().get(i).setVisible(true);}
        for (int i = 3; i < 9; i++) {expectedOutput4.getClicks().get(i).setVisible(false);}
        for (int i = 9; i < 12; i++) {expectedOutput4.getClicks().get(i).setVisible(true);}
        for (int i = 0; i < 3; i++) {expectedOutput4.getInteractions().get(i).setVisible(true);}
        for (int i = 3; i < 9; i++) {expectedOutput4.getInteractions().get(i).setVisible(false);}
        for (int i = 9; i < 12; i++) {expectedOutput4.getInteractions().get(i).setVisible(true);}
        expectedOutputs.add(expectedOutput4);

        //Sets up the expected output for test #5 in the testFilter() method
        CSVData expectedOutput5 = new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog4.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv"));
        for (int i = 0; i < 12; i++) {expectedOutput5.getImpressions().get(i).setVisible(false);}
        for (int i = 0; i < 12; i++) {expectedOutput5.getClicks().get(i).setVisible(false);}
        for (int i = 0; i < 12; i++) {expectedOutput5.getInteractions().get(i).setVisible(false);}
        expectedOutputs.add(expectedOutput5);

        //Sets up the expected output for test #6 in the testFilter() and tests #1 and #2 clearFilters() methods
        expectedOutputs.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ImpressionLogs/ImpressionLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ClickLogs/ClickLog1.csv"), new File("src/test/resources/uk/ac/soton/app/CSVTestFiles/CSVFilter/ServerLogs/ServerLog1.csv")));

        //Sets up the expected output for test #7 in the testFilter() method
        expectedOutputs.add(new CSVData(new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv"), new File("src/test/resources/uk/ac/soton/app/emptyCSVFile.csv")));
    }

    /**
     * Checks if two CSVData objects are identical or not
     * @param csvData1 CSVData object
     * @param csvData2 CSVData object
     * @return whether the two objects are identical or not
     */
    boolean assertCSVData(CSVData csvData1, CSVData csvData2) {
        boolean impressionsMatch = assertImpressions(csvData1.getImpressions(), csvData2.getImpressions());
        boolean clicksMatch = assertClicks(csvData1.getClicks(), csvData2.getClicks());
        boolean interactionsMatch = assertInteractions(csvData1.getInteractions(), csvData2.getInteractions());
        return (impressionsMatch && clicksMatch && interactionsMatch);
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
     * Tests the filter() method
     * Also tests the filterRespectiveClicks() and filterRespectiveInteractions() methods indirectly
     */
    @Test
    void testFilter() {
        //TEST #1
        //Given half of the filter values
        //Tests if it filters in all data that matches all, and not just some, of the given filter values
        //And filters out any unwanted data out and ignores any wanted data already filtered in or any unwanted data already filtered out
        Pair<ModelDate, ModelDate> dateRange = new Pair<>(new ModelDate(2015,1,1, 12,0,2), new ModelDate(2015,4,1,18,0,11));
        List<String> genderFilters = new ArrayList<>(List.of("Male"));
        List<String> ageFilters = new ArrayList<>(Arrays.asList("<25", "35-44", ">54"));
        List<String> incomeFilters = new ArrayList<>(Arrays.asList("Low", "High"));
        List<String> contextFilters = new ArrayList<>(Arrays.asList("Blog", "Shopping", "Travel"));
        CSVFilter.filter(inputs.get(0), dateRange, genderFilters, ageFilters, incomeFilters, contextFilters);
        assertTrue(assertCSVData(expectedOutputs.get(0), inputs.get(0)));

        //TEST #2
        //Given the other half of the filter values
        //Tests if it filters in all data that matches all, and not just some, of the given filter values
        //And filters out any unwanted data out and ignores any wanted data already filtered in or any unwanted data already filtered out
        genderFilters = new ArrayList<>(List.of("Female"));
        ageFilters = new ArrayList<>(Arrays.asList("25-34", "45-54"));
        incomeFilters = new ArrayList<>(List.of("Medium"));
        contextFilters = new ArrayList<>(Arrays.asList("News", "Social Media", "Hobbies"));
        CSVFilter.filter(inputs.get(1),  dateRange, genderFilters, ageFilters, incomeFilters, contextFilters);
        assertTrue(assertCSVData( expectedOutputs.get(1), inputs.get(1)));

        //TEST #3
        //Given a range of dates and some filter values
        //Tests if it filters in all data in the range of weeks that matches the filter values
        //And ignores any data outside the range of dates that matches the filter values
        dateRange = new Pair<>(new ModelDate(2015,1,1, 12,0,2), new ModelDate(2015,1,3,8,0,11));
        ageFilters = new ArrayList<>(Arrays.asList("<25", "35-44", ">54"));
        incomeFilters = new ArrayList<>(Arrays.asList("High", "Low"));
        CSVFilter.filter(inputs.get(0), dateRange, new ArrayList<>(), ageFilters, incomeFilters, new ArrayList<>());
        assertTrue(assertCSVData(expectedOutputs.get(2), inputs.get(0)));

        //TEST #4
        //Given some context filter values
        //Tests if it filters in data, by a user, that matches the filter values
        //And ignores data, by the same user, that doesn't match the filter values
        dateRange = new Pair<>(new ModelDate(2015,1,1, 12,0,2), new ModelDate(2015,4,1,18,0,11));
        contextFilters = new ArrayList<>(Arrays.asList("Blog", "News", "Shopping"));
        CSVFilter.filter(inputs.get(2), dateRange, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), contextFilters);
        assertTrue(assertCSVData(expectedOutputs.get(3), inputs.get(2)));

        //TEST #5
        //Given some filter values
        //Tests if it filters out all data if there isn't any data that matches the filter values
        contextFilters = new ArrayList<>(List.of("Travel"));
        CSVFilter.filter(inputs.get(3), dateRange, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), contextFilters);
        assertTrue(assertCSVData(expectedOutputs.get(4), inputs.get(3)));

        //TEST #6
        //Given no filters
        //Tests if it filters in all data
        CSVFilter.filter(inputs.get(0), new Pair<>(null, null), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        assertTrue(assertCSVData(expectedOutputs.get(5), inputs.get(0)));

        //TEST #7
        //Given a CSVData object with no data
        //Tests if it leaves the object as it is
        CSVFilter.filter(inputs.get(4), dateRange, new ArrayList<>(), ageFilters, new ArrayList<>(), contextFilters);
        assertTrue(expectedOutputs.get(6).getImpressions().isEmpty() && inputs.get(4).getImpressions().isEmpty());
    }

    /**
     * Tests the clearFilters() method
     */
    @Test
    void testClearFilters() {
        //TEST #1
        //Given a CSVData object with all its data filtered out
        //Tests if it filters in all its data
        CSVFilter.clearFilters(inputs.get(5));
        assertTrue(assertCSVData( expectedOutputs.get(5), inputs.get(5)));

        //TEST #2
        //Given a CSVData object with all its data filtered in
        //Tests if it leaves the data as it is
        CSVFilter.clearFilters(inputs.get(6));
        assertTrue(assertCSVData(expectedOutputs.get(5), inputs.get(6)));
    }
}
