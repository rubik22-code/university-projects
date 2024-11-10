package lab6part2;

public class Herbivore extends Animal {

  public Herbivore(String herbivoreName, int herbivoreAge) {
    super(herbivoreName, herbivoreAge);
  }

  public void makeNoise() {
    System.out.println("Herbivore!");
  }

}
