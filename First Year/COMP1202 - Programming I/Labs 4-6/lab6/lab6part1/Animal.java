package lab6part1;

public class Animal {
  private String animalName;
  private int animalAge;
  public Animal(String animalName, int animalAge) {
    this.animalName = animalName;
    this.animalAge = animalAge;
  }
  public String getName() {
    return animalName;
  }
  public int getAge() {
    return animalAge;
  }

}
