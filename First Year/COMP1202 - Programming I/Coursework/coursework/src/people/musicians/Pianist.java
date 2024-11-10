package people.musicians;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import people.Person;
import utils.SoundSystem;

/**
 * A pianist is a musician who plays the piano.
 */

public class Pianist extends Person implements Musician {

  // The type of instrument the musician plays.
  int instrumentID = 1;
  // List of notes for the current score read by the musician.
  List<Integer> notes;
  // The iterator to the next note to be played.
  Iterator<Integer> nextNote;
  // Used to play music by the musician.
  SoundSystem soundSystem;
  // Must be between 0 and 15.
  int seat;
  // Volume for the musician to play the music score.
  int loudness;

  /**
   * Constructor for a pianist.
   * @param name The name of the pianist.
   * @param newSoundSystem The sound system to play music.
   */

  public Pianist(String name, SoundSystem newSoundSystem) {
    super(name);
    this.soundSystem = newSoundSystem;
  }

  /**
   * Different constructor for a pianist, with a seat number.
   * @param name The name of the pianist.
   * @param newSoundSystem The sound system to play music.
   * @param seat The seat number for the pianist.
   */

  public Pianist(String name, SoundSystem newSoundSystem, int seat) {
    super(name);
    this.soundSystem = newSoundSystem;
    this.seat = seat;
  }

  /**
   * This sets the seat and instrument for the sound system.
   * @param seat The seat number for the pianist.
   */

  public void setSeat(int seat) {
    this.seat = seat;
    soundSystem.setInstrument(seat, this.instrumentID);
  }

  /**
   * Reads the notes for the current score.
   * @param notes The list of notes for the current score.
   * @param soft The softness for the musician to play the music score.
   */

  public void readScore(int[] notes, boolean soft) {
    this.notes = new ArrayList<>();
    // Read an array into the list.
    for (int i : notes) {
      this.notes.add(i);
    }
    // Determines the loudness of the instrument.
    if (soft) {
      this.loudness = 50;
    }
    else {
      this.loudness = 100;
    }
    // Call the next note by putting the array into the iterator.
    nextNote = this.notes.iterator();
  }

  /**
   * Plays the next note in the score.
   */

  public void playNextNote() {
    if (nextNote.hasNext()) {
      soundSystem.playNote(seat, nextNote.next(), loudness);
    }
  }

  /**
   * Gets the seat number for the pianist.
   * @return The seat number for the pianist.
   */

  public int getSeat() {
    return seat;
  }

  /**
   * Custom method for play composition in conductor.
   * Gets the instrument ID for the pianist.
   * @return The instrument ID for the pianist.
   */

  public int getInstrumentID() {
    return instrumentID;
  }
}
