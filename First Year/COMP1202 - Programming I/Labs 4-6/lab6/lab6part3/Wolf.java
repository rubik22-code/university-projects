package lab6part3;

public class Wolf extends Carnivore {

  public Wolf(String wolfName, int wolfAge) {
    super(wolfName, wolfAge);
  }

  public void makeNoise() {
    System.out.println("howl");
  }

}
