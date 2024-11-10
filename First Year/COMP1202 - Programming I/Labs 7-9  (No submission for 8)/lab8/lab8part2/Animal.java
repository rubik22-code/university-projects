package lab8part2;

public abstract class Animal implements Comparable<Animal> {

  public int compareTo(Animal differentAnimal) {

    if (this.animalAge == differentAnimal.animalAge) {
      return 0;
    }

    else if (this.animalAge > differentAnimal.animalAge) {
      return 1;
    }

    else {
      return -1;
    }

  }
  private String animalName;
  private int animalAge;
  public Animal(String animalName, int animalAge) {
    this.animalName = animalName;
    this.animalAge = animalAge;
  }

  public Animal() {
    this("newborn", 0);
  }

  public String getName() {
    return animalName;
  }
  public int getAge() {
    return animalAge;
  }
  public abstract void makeNoise();

  public abstract void eat(Food foodToEat) throws Exception;

  public void eat(Food foodToEat, Integer tempNumber) {
    for (int i = 0; i < tempNumber; i++) {
      System.out.println("The animal has been fed.");
    }
  }

}
