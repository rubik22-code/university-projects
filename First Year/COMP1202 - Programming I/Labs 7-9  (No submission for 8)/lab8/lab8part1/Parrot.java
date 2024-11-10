package lab8part1;

public class Parrot extends Omnivore {

  public Parrot(String parrotName, int parrotAge) {
    super(parrotName, parrotAge);
  }

  public Parrot(Integer parrotNumber) {
    this("Polly", parrotNumber);
  }

  public void makeNoise() {
    System.out.println("hoot");
  }

}


