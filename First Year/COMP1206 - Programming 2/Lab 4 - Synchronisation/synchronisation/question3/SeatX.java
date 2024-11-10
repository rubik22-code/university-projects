import java.util.concurrent.locks.ReentrantLock;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.Seat;

public class SeatX implements Seat {

  ReentrantLock firstLock;
  ReentrantLock secondLock;

  public void askFork1() {
    secondLock.lock();
  }

  public void askFork2() {
    firstLock.lock();
  }

  public void assignForks(ReentrantLock reentrantLock, ReentrantLock reentrantLock1) {
    firstLock = reentrantLock;
    secondLock = reentrantLock1;
  }
}
