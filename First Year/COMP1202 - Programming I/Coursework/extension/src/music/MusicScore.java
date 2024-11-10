package music;

/**
 * Keeps the information about a piece of music for an individual instrument.
 */

public class MusicScore {

  int instrumentID;
  int[] notes;
  boolean soft;
  String instrumentName;

  /**
   * Creates a new MusicScore object.
   * @param instrumentName The name of the instrument to play the music.
   * @param notes The notes to play.
   * @param soft Whether the music should be played softly.
   */

  MusicScore(String instrumentName, int[] notes, boolean soft) {
    this.instrumentName = instrumentName;

    if (instrumentName.equals("Violin")) {
      instrumentID = 41;
    }
    else if (instrumentName.equals("Cello")) {
      instrumentID = 43;
    }
    else {
      instrumentID = 1; // Piano.
    }

    this.notes = notes;
    this.soft = soft;
  }

  /**
   * Returns the instrument ID.
   * @return The instrument ID.
   */

  public int getInstrumentID() {
    return instrumentID;
  }

  /**
   * Return the array of notes representing the music score.
   * @return The notes.
   */

  public int[] getNotes() {
    return notes;
  }

  /**
   * Returns whether the music should be played softly.
   * @return the boolean value of soft.
   */

  public boolean isSoft() {
    return soft;
  }
}
