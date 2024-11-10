import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.UnitCounter;

public class Counter implements UnitCounter {

  private int counter;

  private Object monitor = new Object();

  public void addOne() {
    synchronized (monitor) {
      counter++;
    }
  }

  public int getCounter() {
    synchronized (monitor) {
      return counter;
    }
  }
}
