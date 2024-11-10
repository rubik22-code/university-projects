import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.recursion.MinimumInArray;

public class MinInt implements MinimumInArray {

  public static void main(String[] numbers) {
    int[] arr = {24, 52, 74, 9, 34, 23, 64, 34};
    MinInt minInt = new MinInt();
    System.out.println("Minimum is: " + minInt.findMin(arr));
  }

  public int findMin(int[] array) {
    int minimum = array[0];

    if (array.length > 1) {
      minimum = findMinAux(0, array);
    }
    return minimum;
  }

  public int findMinAux(int index, int[] ints) {

    if (index == ints.length - 1) {
      return ints[index];
    }

    else {
      return Math.min(ints[index], findMinAux(index + 1, ints));
    }
  }
}