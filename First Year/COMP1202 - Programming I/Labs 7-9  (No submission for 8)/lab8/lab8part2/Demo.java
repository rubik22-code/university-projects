package lab8part2;
import java.util.ArrayList;
import java.util.Collections;

// What is an interface? Groups related methods with empty bodies.
// An abstract class extends, whilst interface class implements.

public class Demo {

  public static void main(String[] args) {

    Wolf jacob = new Wolf("Jacob", 5);
    Parrot steven = new Parrot("Steven", 3);

    ArrayList<Animal> animalArrayList = new ArrayList<Animal>();
    animalArrayList.add(0, jacob);
    animalArrayList.add(1, steven);

    for (Animal i : animalArrayList) {
      System.out.println("Before sorting:");
      System.out.println(i);
    }

    Collections.sort(animalArrayList);

    for (Animal i : animalArrayList) {
      System.out.println("After sorting:");
      System.out.println(i);
    }


  }

}
