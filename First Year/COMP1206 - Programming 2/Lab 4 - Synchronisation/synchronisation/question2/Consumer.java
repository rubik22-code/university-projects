import java.util.Random;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.FactoryWorker;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.NumberQueue;

public class Consumer extends FactoryWorker {

  public Consumer(int id,
      NumberQueue belt) {
    super("Consumer", id, belt);
  }

  public void message(int i) {
    System.out.println("Consumer" + id + " picked " + belt + " from the belt");
  }

  public int action() {
    belt.dequeue();
    return 0;
  }

  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        action();
      }
      catch (Exception e) {
        messageError();
      }
    }
  }
}
