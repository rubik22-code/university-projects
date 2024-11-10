import java.util.ArrayList;
import java.util.List;
import uk.ac.soton.ecs.comp1206.labtestlibrary.datastructure.Tree;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.recursion.MinimumInTree;

public class MinTree implements MinimumInTree {

  public static void main(String[] args){
    Tree tree = new Tree( 24,
        new Tree( 45,
            null ,
            new Tree(8, null , null) ) ,
        new Tree ( 17,
            new Tree (74 , null , null ) ,
            null ) );

    MinTree minTree = new MinTree();
    System.out.println("Minimum is: " + minTree.findMin(tree));
  }
  List<Integer> treeList = new ArrayList<>();
  public int findMin(Tree tree) {

    if (tree != null) {
      treeList.add(tree.getVal());
    }

 		if (tree.left() != null) {
      treeList.add(tree.left().getVal());
      findMin(tree.left());
    }

    if (tree.right() != null) {
      treeList.add(tree.right().getVal());
      findMin(tree.right());
    }

    int[] array = treeList.stream().mapToInt(i -> i).toArray();

    return findMinOfIntArray(array);
  }

  public int findMinOfIntArray(int[] array) {

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