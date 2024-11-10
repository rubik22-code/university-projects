package lab6part2;

public class Main {

  public static void main(String[] args) {
    Wolf newWolf = new Wolf("Wolf1", 1);
    Parrot newParrot = new Parrot("Parrot1", 1);

    newWolf.makeNoise();
    newParrot.makeNoise();

    Meat newMeat = new Meat("Ham");
    Plant newPlant = new Plant("Aloe");

  }

}
