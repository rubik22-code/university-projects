package lab9part3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Quiz {

  private ArrayList<FlashCard> quizCards = new ArrayList<FlashCard>();

  FlashCardReader newFlashCardReader;

  String userAnswer;
  String stateOfAnswer;
  String saveResults;
  boolean stateOfResults = false;
  int answersCorrect = 0;
  int numberOfQuestions = 0;

  public Quiz(String filename) {

    newFlashCardReader = new FlashCardReader("Questions.txt");

    quizCards = newFlashCardReader.getFlashCards();

    play();
  }

  public static void main(String[] args) {

    Quiz newQuiz = new Quiz("Questions.txt");

  }

  public void play() {

    Toolbox myToolbox = new Toolbox();

    System.out.println("Would you like to save your results before the quiz begins?");
    saveResults = myToolbox.readStringFromCmd();

    while (!stateOfResults) {
      if (saveResults.equals("Y")) {
        stateOfResults = true;
        for (FlashCard i : quizCards) {
          System.out.println("Iteration number" + i);
          System.out.println(i.getQuestion());
          numberOfQuestions = numberOfQuestions + 1;

          userAnswer = myToolbox.readStringFromCmd();

          if (i.getAnswer().equals(userAnswer)) {
            System.out.println("right");
            stateOfAnswer = "right";
            answersCorrect = answersCorrect + 1;
          } else {
            System.out.println("wrong");
            stateOfAnswer = "wrong";
            System.out.println(i.getAnswer());
          }
          save(userAnswer, stateOfAnswer); // Saves the answers for each line.
        }
        save(userAnswer, stateOfAnswer); // Saves the answers for each line.
      }
      else if (saveResults.equals("N")) {
        stateOfResults = true;
        for (FlashCard i : quizCards) {
          System.out.println("Iteration number" + i);
          System.out.println(i.getQuestion());
          numberOfQuestions = numberOfQuestions + 1;

          userAnswer = myToolbox.readStringFromCmd();

          if (i.getAnswer().equals(userAnswer)) {
            System.out.println("right");
            stateOfAnswer = "right";
            answersCorrect = answersCorrect + 1;
          } else {
            System.out.println("wrong");
            stateOfAnswer = "wrong";
            System.out.println(i.getAnswer());
          }
        }
      }
      else {
        System.out.println("This question can only accept an input of Y for yes and N for no.");
        saveResults = myToolbox.readStringFromCmd();
      }
    }
  }

  public void save(String userAnswer, String stateOfAnswer) {
    File nameOfFile = new File("save.txt");
    String textToWrite;

    if (nameOfFile.exists()) {
      nameOfFile.delete();
    }

    for (FlashCard i : quizCards) {
      try {

        FileWriter newFileWriter = new FileWriter(nameOfFile.getAbsoluteFile(), true);
        BufferedWriter newBufferWriter = new BufferedWriter(newFileWriter);

        textToWrite = i.getQuestion() + "," + userAnswer + "," + stateOfAnswer;

        newBufferWriter.append(textToWrite);
        newBufferWriter.append("\n");
        newBufferWriter.close();

      } catch (IOException e) {
        System.out.println("The file cannot be written.");
      }
    }
    try {
      double percentageAnswersCorrect = (answersCorrect * 100) / numberOfQuestions;

      FileWriter newFileWriter = new FileWriter(nameOfFile.getAbsoluteFile(), true);
      BufferedWriter newBufferWriter = new BufferedWriter(newFileWriter);
      newBufferWriter.write(answersCorrect + "," + numberOfQuestions + "," + percentageAnswersCorrect);
      newBufferWriter.close();
    } catch (IOException e) {
      System.out.println("The file cannot be written.");
    }
  }
}

