package lab9part2;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FlashCardReader {

  BufferedReader reader;

  private ArrayList<FlashCard> cards = new ArrayList<FlashCard>();

  public FlashCardReader(String filename) {

    try {
      reader = new BufferedReader(new FileReader(filename));
    }

    catch (FileNotFoundException e) {
      System.out.println("The file has not been found.");
    }
  }

  public String getLine() {
    String readingFromLine = new String();

    try {
      readingFromLine = reader.readLine();
    }

    catch (IOException e) {
      System.out.println("The next line of the file cannot be returned.");
    }

    return readingFromLine;
  }

  public Boolean fileIsReady() {

    boolean fileState = false;

    try {
      if (reader.ready() == true){
        fileState = true;
      }
      else {
        fileState = false;
      }
    }

    catch (Exception e) {
      System.out.println("Error with fileIsReady.");
    }

    System.out.println(fileState);
    return fileState;
  }

  public ArrayList<FlashCard> getFlashCards() {

    FlashCardReader getReader = new FlashCardReader("Questions.txt");

    for (int i = 0; i < 10; i++) {
      String splitter = getReader.getLine(); // Reads line
      String[] splitterArray = splitter.split(":"); // Splits based on ":"

      cards.add(new FlashCard(splitterArray[0], splitterArray[1]));
    }

    return cards;
  }

}