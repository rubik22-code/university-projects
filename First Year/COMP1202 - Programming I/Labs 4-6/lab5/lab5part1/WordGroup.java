package lab5part1;

public class WordGroup {
    String words;

    public WordGroup(String words) { // Converts to lowercase.
        this.words = words.toLowerCase();
    }

    public String[] getWordArray() { // Removes spaces.
        return words.split(" ");
    }

}

