import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class DAGSortTest {


  @DisplayName("Successful case")
  @Test
  public void testSuccessfulSortDAG() throws InvalidNodeException, CycleDetectedException {
    int[][] edges = {{3}, {3,4}, {4,7}, {5,6,7}, {6}, {}, {}, {}};
    int[] expected = {2, 1, 4, 0, 3, 7, 6, 5};

    assertArrayEquals(DAGSort.sortDAG(edges), expected);
  }

  @DisplayName("Unsuccessful case")
  @Test
  public void testUnsuccessfulSortDAG() throws InvalidNodeException, CycleDetectedException {
    int[][] edges = {{3}, {3,4}, {4,7}, {5,6,7}, {6}, {}, {}, {}};
    int[] expected = {7, 7, 7, 7, 7, 7, 7, 7};

    Assertions.assertNotEquals(DAGSort.sortDAG(edges), expected);
  }

  @DisplayName("Null case")
  @Test
  public void testNullInputSortDAG() {
    assertThrows(NullPointerException.class,() -> DAGSort.sortDAG(null));
  }

  @DisplayName("Cycle case")
  @Test
  public void testCycleSortDAG() throws InvalidNodeException {
    try {
      int[][] edges = {{1}, {2}, {0}};
      DAGSort.sortDAG(edges);
    } catch (CycleDetectedException e) {
      System.out.println("CycleDetectedException thrown");
    }
  }

  @DisplayName("Invalid index case")
  @Test
  public void testIndexSortDAG() throws CycleDetectedException {
    try {
      int[][] edges = {{1}, {2}, {3}, {4, 5}};
      DAGSort.sortDAG(edges);
    } catch (InvalidNodeException e) {
      System.out.println("InvalidNodeException thrown");
    }
  }

  @DisplayName("Empty case")
  @Test
  public void emptySortDAG() throws InvalidNodeException, CycleDetectedException {
    int[][] edges = {};
    int[] expected = {};
    assertArrayEquals(expected, DAGSort.sortDAG(edges));
  }

  @DisplayName("Multiple sort case")
  @Test
  public void multipleSortDAG() throws InvalidNodeException, CycleDetectedException {
    int[][] edges = {{1, 2}, {3}, {3}, {}};

    // Different orders of the numbers
    int[] firstExpected = {0, 1, 2, 3};
    int[] secondExpected = {0, 2, 1, 3};
    int[] thirdExpected = {3, 1, 2, 0};

    int[] result = DAGSort.sortDAG(edges);
    Assertions.assertTrue(Arrays.equals(firstExpected, result) || Arrays.equals(secondExpected, result) || Arrays.equals(thirdExpected, result));
  }

  @DisplayName("Large case")
  @Test
  public void largeSortDAG() throws InvalidNodeException, CycleDetectedException {
    int[][] edges = new int[1000][];

    // Filling the array with values to test the largest case of 1000
    Arrays.fill(edges, new int[0]);

    int[] largeNodes = DAGSort.sortDAG(edges);

    Assertions.assertEquals(1000, largeNodes.length);
  }
}

