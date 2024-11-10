package lab5part1;

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
    }
}
