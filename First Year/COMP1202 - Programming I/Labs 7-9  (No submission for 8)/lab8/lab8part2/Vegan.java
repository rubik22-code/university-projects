package lab8part2;

public class Vegan extends Herbivore {

  public Vegan(String veganName, int veganAge) {
    super(veganName, veganAge);
  }

  public void makeNoise() {
    System.out.println("I love plants!");
  }

}