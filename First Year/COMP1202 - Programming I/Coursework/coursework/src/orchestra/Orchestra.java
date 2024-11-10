package orchestra;
import java.util.HashMap;
import people.musicians.Musician;

/**
 * This class represents an orchestra.
 */

public class Orchestra {

  HashMap<Integer, Musician> seating;

  /**
   * Constructor for Orchestra.
   */

  public Orchestra() {
    seating = new HashMap<>();
  }

  /**
   * Adds a musician to the orchestra.
   * Checks if the input musician is already in the orchestra.
   * Checks if there are no free seats in the orchestra.
   * Then assign to available seat.
   * @param myMusician the musician to add.
   */

  public int sitDown(Musician myMusician) {
    if (seating.containsValue(myMusician)) {
      return 2;
    } else if (seating.size() <= 15) {
      for (int i = 0; i <= 15; i++) {
        if (!seating.containsKey(i)) {
          seating.put(i, myMusician);
          // Orchestra seating is consistent with the individual musician’s seat.
          myMusician.setSeat(i);
          return 0;
        }
      }
    }
    return 1;
  }

  /**
   * Checks whether a musician is in the orchestra.
   * @param myMusician the musician to check.
   * @return true if the musician is in the orchestra, false otherwise.
   */

  public boolean isSeated(Musician myMusician) {
    return seating.containsValue(myMusician);
  }

  /**
   * Removes a musician from the orchestra by getting the musician’s seat number.
   * @param myMusician the musician to remove.
   */

  public void standUp(Musician myMusician) {
    seating.remove(myMusician.getSeat());
  }

  /**
   * Plays the next note of every seat in the seating.
   */

  public void playNextNote() {
    for (int i : seating.keySet()) {
      seating.get(i).playNextNote();
    }
  }
}
