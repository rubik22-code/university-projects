package lab8part1;

public class Wolf extends Carnivore {

  public Wolf(String wolfName, int wolfAge) {
    super(wolfName, wolfAge);
  }

  public Wolf() {
    super();
  }

  public void makeNoise() {
    System.out.println("howl");
  }

}
