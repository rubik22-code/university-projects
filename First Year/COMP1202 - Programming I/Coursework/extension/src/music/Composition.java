package music;
import java.util.List;

/**
 * Made up of several music scores for different instruments.
 */

public interface Composition {
  // Return the name of the composition.
  String getName();
  // Add a score to the composition.
  void addScore(String instrumentName, List<String> notes, boolean soft);
  // Return the array of music scores in the composition.
  MusicScore[] getScores();
  // How many notes are to be played.
  int getLength();
  // The length for one note (tempo composition).
  int getNoteLength();
}
