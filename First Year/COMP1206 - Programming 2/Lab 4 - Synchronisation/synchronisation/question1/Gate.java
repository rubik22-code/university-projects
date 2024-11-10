public class Gate implements Runnable {

  private Counter counter;
  private int gateVisitors;

  public Gate(Counter counter, int gateVisitors) {
    this.counter = counter;
    this.gateVisitors = gateVisitors;
  }

  public void run() {
    for (int i = 0; i < gateVisitors; i++) {
      counter.addOne();
    }
  }

  public int getGateVisitors() {
    return gateVisitors;
  }
}
