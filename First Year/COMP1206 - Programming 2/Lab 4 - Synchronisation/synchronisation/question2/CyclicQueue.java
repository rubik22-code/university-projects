import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.NumberQueue;

public class CyclicQueue implements NumberQueue {

  int[] array;
  int head;
  int tail;

  public CyclicQueue(int capacity) {
    array = new int[capacity];
    head = -1;
    tail = -1;
  }

  // Main method to test without the harness.
  public static void main(String[] args) {
    CyclicQueue newObj = new CyclicQueue(3);
    newObj.enqueue(28);
    newObj.enqueue(14);
    newObj.enqueue(89);
    newObj.dequeue();
    newObj.enqueue(67);
  }

  public void enqueue(int i) throws IndexOutOfBoundsException {

    if ((tail + 1) % array.length == head) {
      throw new IndexOutOfBoundsException();
    }

    else {
      tail = (tail + 1) % array.length;
      array[tail] = i;

      if (head == -1) {
        head = 0;
      }
    }
  }

  public int dequeue() throws IndexOutOfBoundsException {

    int tempHead = -1;

    if (isEmpty()) {
      throw new IndexOutOfBoundsException();
    }

    else {
      tempHead = array[head];

      if (head == tail) {
        head = -1;
        tail = -1;
      }

      else {
        head = (head + 1) % array.length;
      }
    }

    return tempHead;
  }

  public boolean isEmpty() {
    return head == -1;
  }
}
