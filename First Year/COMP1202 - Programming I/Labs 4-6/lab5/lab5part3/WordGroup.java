package lab5part3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class WordGroup {
    String words;

    public WordGroup(String words) { // Converts to lowercase.
        this.words = words.toLowerCase();
    }

    public String[] getWordArray() { // Removes spaces.
        return words.split(" ");
    }

    public HashSet<String> getWordSet(WordGroup words) {
        HashSet<String> wordHashString = new HashSet<String>();

        for(String i: this.getWordArray()) {
            wordHashString.add(i);
        }

        for(String i: words.getWordArray()) {
            wordHashString.add(i);
        }

        return wordHashString;
    }

    public HashMap<String, Integer> getWordCounts() {
        HashMap<String, Integer> wordCountHashMap = new HashMap<String, Integer>();

        for (String i : this.getWordArray()) {

            if (wordCountHashMap.get(i) == null) {
                wordCountHashMap.put(i, 1);
            }

            else {
                wordCountHashMap.put(i, wordCountHashMap.get(i) + 1);
            }

        }

        return wordCountHashMap;
    }

}

