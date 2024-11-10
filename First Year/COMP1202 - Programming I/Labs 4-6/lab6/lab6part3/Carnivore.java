package lab6part3;

public class Carnivore extends Animal {

  public Carnivore(String carnivoreName, int carnivoreAge) {
    super(carnivoreName, carnivoreAge);
  }

  public void makeNoise() {
    System.out.println("Carnivore!");
  }

  public void eat(Food foodToEat) throws Exception {

    if (foodToEat instanceof Plant) {
      throw new Exception("Carnivores won't eat plants.");
    }

    else {
      System.out.println("The animal is eating" + foodToEat);
    }

  }

}
