package people.conductors;
import java.util.ArrayList;
import music.Composition;
import music.MusicScore;
import orchestra.Orchestra;
import people.Person;
import people.musicians.Musician;
import utils.SoundSystem;

/**
 * A conductor is a person who leads an orchestra in a performance of a composition.
 */

public class Conductor extends Person {

  SoundSystem newSoundSystem;
  String conductorName;
  ArrayList<Musician> band = new ArrayList<>();
  Orchestra newOrchestra;

  /**
   * Creates a new conductor with the given name.
   * @param conductorName the name of the conductor.
   * @param newSoundSystem the sound system used by the conductor, used to stop the music.
   */

  public Conductor(String conductorName, SoundSystem newSoundSystem) {
    this.newSoundSystem = newSoundSystem;
    this.conductorName = conductorName;
    this.newOrchestra = new Orchestra();
  }

  /**
   * Registers a new musician to the band with the conductor.
   * @param newMusician the musician to be registered.
   */

  public void registerMusician(Musician newMusician) {
    band.add(newMusician);
  }

  /**
   * Custom method created so that musician has 50% chance to leave the band.
   * Unregisters a musician from the band with the conductor.
   * @param newMusician the musician to be unregistered.
   */

  public void unregisterMusician(Musician newMusician) {
    band.remove(newMusician);
  }

  /**
   * Plays the composition with the orchestra.
   * Conductor can arrange the play of a composition.
   * @param newComposition the composition to be played.
   */

  public void playComposition(Composition newComposition) {
    // Get the scores from the composition.
    MusicScore[] scores = newComposition.getScores();

    for (Musician musician : band) {
      // Asks musician to stand up if already seated and finished playing.
      if (newOrchestra.isSeated(musician)) {
        newOrchestra.standUp(musician);
      }
    }

    for (MusicScore musicScore : scores) {
      for (Musician musician : band) {
        // If the musician is not seated and the score is for the musician, then seat the musician.
        if (!newOrchestra.isSeated(musician) && musicScore.getInstrumentID() == musician.getInstrumentID()) {
          // Arrange for the musicians to sit down with the orchestra.
          newOrchestra.sitDown(musician);
          // Assign the score to the musicians in the band.
          musician.readScore(musicScore.getNotes(), musicScore.isSoft());
          break;
        }
      }
    }

    // Play the composition until the end.
    for (int i = 0; i < newComposition.getLength(); i++) {
        try {
          // Play the next note in the composition.
          newOrchestra.playNextNote();
          // Sleep for the note length.
          Thread.sleep(newComposition.getNoteLength());
        }
        catch (InterruptedException e ) {
          System.out.println("Cannot delay.");
        }
      }
    // Stop the music play.
    newSoundSystem.init();
  }
}
