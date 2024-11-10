import java.util.Random;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.FactoryWorker;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.NumberQueue;

public class Producer extends FactoryWorker {

  public Producer(int id, NumberQueue belt) {
    super("Producer", id, belt);
  }

  public void message(int i) {
    System.out.println("Producer" + id + " picked " + belt + " from the belt");
  }

  public int action() {
    Random random = new Random();
    int n = random.nextInt(99999);
    belt.enqueue(n);
    return n;
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
