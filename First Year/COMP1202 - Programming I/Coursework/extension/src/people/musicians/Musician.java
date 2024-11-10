package people.musicians;

/**
 * This class represents a musician.
 */

public interface Musician {

  /**
   * This method sets the seat of the musician.
   * @param seat the seat of the musician.
   */

  void setSeat(int seat);

  /**
   * This method reads the music score of the musician.
   * @param notes the notes of the score.
   * @param soft the softness of the notes.
   */

  void readScore(int[] notes, boolean soft);

  /**
   * After setting position of musician (using setSeat) and the musician reads the score (using readScore).
   */

  void playNextNote();

  /**
   * This method returns the seat of the musician.
   * @return the seat of the musician.
   */

  int getSeat();

  /**
   * This custom method returns the instrument ID of the musician.
   * Created to compare the music score and musician Instrument ID in the play composition method.
   * @return the instrument ID of the musician.
   */

  int getInstrumentID();
  }


