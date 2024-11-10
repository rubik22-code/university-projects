package lab5part2;

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

}

