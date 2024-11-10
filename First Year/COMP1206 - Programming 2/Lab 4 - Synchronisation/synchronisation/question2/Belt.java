public class Belt extends CyclicQueue {

  private Object monitor = new Object();

  public Belt(int capacity) {
    super(capacity);
  }

  public void enqueue(int i) throws IndexOutOfBoundsException {

    synchronized (monitor) {
      while ((tail + 1) % array.length == head) {
        try {
          monitor.wait();
        } catch (InterruptedException e) {
        }
      }

      monitor.notify();
      tail = (tail + 1) % array.length;
      array[tail] = i;

      if (head == -1) {
        head = 0;
      }
    }
  }

  public int dequeue() throws IndexOutOfBoundsException {

    synchronized (monitor) {
      int tempHead = -1;

      while (isEmpty()) {
        try {
          monitor.wait();
        } catch (InterruptedException e) {
        }
      }

        monitor.notify();
        tempHead = array[head];

        if (head == tail) {
          head = -1;
          tail = -1;
        }

        else {
          head = (head + 1) % array.length;
        }

      return tempHead;
    }
  }
}
