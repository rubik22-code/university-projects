package lab5part3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        WordGroup firstWordGroup = new WordGroup("You can discover more about a person in an hour of play than in a year of conversation");
        WordGroup secondWordGroup = new WordGroup("When you play play hard when you work dont play at all");

        String[] firstWorldGroupArray = firstWordGroup.getWordArray(); // Two arrays of strings.
        String[] secondWorldGroupArray = secondWordGroup.getWordArray();

        for (String word : firstWorldGroupArray) { // Prints each word in array.
            System.out.println(word);
        }

        for (String word : secondWorldGroupArray) { // ^^^.
            System.out.println(word);

        }

        /*

        // This method of code just isn't working ... I'm going to move on to alternative options.

        HashSet<String> combinedHashSet = secondWordGroup.getWordSet(firstWordGroup);

        for (String i : combinedHashSet) {
            System.out.println(i);
        }

        HashMap<String,Integer> wordCountCombined = new HashMap<>();
        wordCountCombined.putAll(firstWordGroup.getWordCounts());
        wordCountCombined.putAll(secondWordGroup.getWordCounts());

        for (String i : wordCountCombined.keySet()) {
            System.out.println(i + ": " + wordCountCombined.get(i));
        }
        */

        Set wordCountCombined1 = firstWordGroup.getWordCounts().keySet();
        Set wordCountCombined2 = secondWordGroup.getWordCounts().keySet();

        for (Object i : wordCountCombined1) {
            System.out.println(i + ": " + firstWordGroup.getWordCounts().get(i));
        }

        for (Object i : wordCountCombined2) {
            System.out.println(i + ": " + secondWordGroup.getWordCounts().get(i));
        }

    }
}
