package lab6part2;

public class Carnivore extends Animal {

  public Carnivore(String carnivoreName, int carnivoreAge) {
    super(carnivoreName, carnivoreAge);
  }

  public void makeNoise() {
    System.out.println("Carnivore!");
  }

}
