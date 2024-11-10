package lab6part2;

public class Omnivore extends Animal {

  public Omnivore(String omnivoreName, int omnivoreAge) {
    super(omnivoreName, omnivoreAge);
  }

  public void makeNoise() {
    System.out.println("Omnivore!");
  }

}
