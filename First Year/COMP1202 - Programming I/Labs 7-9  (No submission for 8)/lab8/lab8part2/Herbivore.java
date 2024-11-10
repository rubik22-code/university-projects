package lab8part2;

public class Herbivore extends Animal {

  public Herbivore(String herbivoreName, int herbivoreAge) {
    super(herbivoreName, herbivoreAge);
  }

  public void makeNoise() {
    System.out.println("Herbivore!");
  }

  public void eat(Food foodToEat) throws Exception {
    if (foodToEat instanceof Meat) {
      throw new Exception("Herbivores won't eat meat.");
    }

    else {
      System.out.println("The animal is eating" + foodToEat);
    }
  }

}
