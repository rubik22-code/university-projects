package uk.ac.soton.comp1206.component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import javafx.util.Pair;

public class ScoresList extends ListView<Pair<String, Integer>> {

  /**
   * The list of scores
   */
  private SimpleListProperty<Pair<String, Integer>> localScores = new SimpleListProperty<>(FXCollections.observableArrayList());

  public ScoresList() {
    super();
    setItems(localScores.get());
  }

  /**
   * Get the scores property
   * @return the scores property
   */

  public SimpleListProperty<Pair<String, Integer>> scoresProperty() {
    return localScores;
  }

  /**
   * Fade in the scores list
   */
  public void reveal() {
    FadeTransition fade = new FadeTransition(Duration.seconds(3), this);
    fade.setToValue(2);
    fade.play();
  }

  /**
   * Loads the scores from a file by splitting the lines into name and score
   */
  public void loadScores(String filePath) throws IOException {

    File file = new File(filePath);
    if (!file.exists()) {
      file.createNewFile();
      writeScores("scores.txt", getDefaultScores());
    }

    List<Pair<String, Integer>> scorePairs = new ArrayList<>();

    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] parts = line.split(":");
      String name = parts[0];
      int score = Integer.parseInt(parts[1]);
      Pair<String, Integer> scorePair = new Pair<>(name, score);
      scorePairs.add(scorePair);
    }
    scanner.close();

    // Sort the scores
    Collections.sort(scorePairs,
        (firstPart, secondPart) -> secondPart.getValue().compareTo(firstPart.getValue()));

    ObservableList<Pair<String, Integer>> scoresList = FXCollections.observableArrayList(scorePairs);
    localScores.set(scoresList);
  }

  /**
   * Writes the scores to a file, using the same format as the loadScores method
   */
  public void writeScores(String filePath, List<Pair<String, Integer>> scoresList) {
    String fileName = "scores.txt";
    File file = new File(filePath);

    try {
      // Create the file if it doesn't exist
      if (!file.exists()) {
        file.createNewFile();
        List<Pair<String, Integer>> defaultScores = getDefaultScores();
        writeScores(fileName, defaultScores);
      }

      // Write the scores to the file, in the format name:score
      FileWriter writer = new FileWriter(file);
      scoresList.sort((firstPair, secondPair) -> secondPair.getValue().compareTo(firstPair.getValue()));
      for (Pair<String, Integer> score : scoresList) {
        writer.write(score.getKey() + ":" + score.getValue() + "\n");
      }
      writer.close();

    } catch (IOException e) {
      System.out.println("Cannot write scores to the file: " + e.getMessage());
    }
  }

  /**
   * Default scores to use if the scores file doesn't exist
   */
  public List<Pair<String, Integer>> getDefaultScores() {
    List<Pair<String, Integer>> defaultScores = new ArrayList<>();
    defaultScores.add(new Pair<>("Adam", 10));
    defaultScores.add(new Pair<>("Ben", 20));
    defaultScores.add(new Pair<>("Ellie", 30));
    defaultScores.add(new Pair<>("Izzy", 140));
    defaultScores.sort((firstPair, secondPair) -> secondPair.getValue().compareTo(firstPair.getValue()));
    return defaultScores;
  }
}
