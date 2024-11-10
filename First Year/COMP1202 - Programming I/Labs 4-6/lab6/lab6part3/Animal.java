package lab6part3;

public abstract class Animal {
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
  public abstract void makeNoise();

  public abstract void eat(Food foodToEat) throws Exception;

}
