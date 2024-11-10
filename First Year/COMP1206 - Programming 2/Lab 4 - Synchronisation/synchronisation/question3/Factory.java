import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.Seat;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.SeatFactory;
import uk.ac.soton.ecs.comp1206.labtestlibrary.recursion.Tuple;

public class Factory implements SeatFactory {

  public Tuple<Class<? extends Seat>, Class<? extends Seat>> getSeats() {
    Tuple newTuple = new Tuple(SeatX.class, SeatY.class);
    return newTuple;
  }
}
