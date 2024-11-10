package lab9part2;

import java.util.ArrayList;

public class Quiz {

  private ArrayList<FlashCard> quizCards = new ArrayList<FlashCard>();

  FlashCardReader newFlashCardReader;

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
    String userAnswer;

    for (FlashCard i : quizCards) {
      System.out.println(i.getQuestion());

      userAnswer = myToolbox.readStringFromCmd();

      if (i.getAnswer().equals(userAnswer)) {
        System.out.println("right");
      }
      else {
        System.out.println("wrong");
        System.out.println(i.getAnswer());
      }

    }

  }

}
