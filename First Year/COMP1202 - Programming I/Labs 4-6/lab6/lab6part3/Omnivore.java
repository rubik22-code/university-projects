package lab6part3;

public class Omnivore extends Animal {

  public Omnivore(String omnivoreName, int omnivoreAge) {
    super(omnivoreName, omnivoreAge);
  }

  public void makeNoise() {
    System.out.println("Omnivore!");
  }

  public void eat(Food foodToEat) {
    System.out.println("The animal is eating" + foodToEat);
  }

}
