package lab6part3;

public class Main {

  public static void main(String[] args) {
    Wolf newWolf = new Wolf("Wolf1", 1);
    Parrot newParrot = new Parrot("Parrot1", 1);

    newWolf.makeNoise();
    newParrot.makeNoise();

    Meat newMeat = new Meat("Ham");
    Plant newPlant = new Plant("Aloe");

    Vegan newVegan = new Vegan("TheVeganTeacher", 49);

    newVegan.makeNoise();

    Herbivore newHerbivore = new Herbivore("Koala", 10);

    Carnivore newCarnivore = new Carnivore("Cheetah", 40);

    try {
      newHerbivore.eat(newMeat);
      newCarnivore.eat(newPlant);
    }
    catch (Exception collision) {
      System.err.println(collision);
    }

  }

}
