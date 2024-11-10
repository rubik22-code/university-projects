package lab9part1;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FlashCardReader {

  BufferedReader reader;

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
}