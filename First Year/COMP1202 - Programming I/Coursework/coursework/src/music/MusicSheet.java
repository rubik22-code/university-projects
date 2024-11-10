package music;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A music sheet is a collection of music notes, composed of a name, tempo and length.
 */

public class MusicSheet implements Composition {
  String musicSheetName;
  String musicSheetTempo;
  // Number of notes in the composition.
  int musicSheetLength;
  // The duration between each note.
  int musicSheetNoteLength;
  // Notes to add into the array.
  int[] notesArray;
  // The music score to add the notes to.
  MusicScore myMusicScore;
  // Using to convert the array to an ArrayList.
  List<MusicScore> myMusicScoreArrayList = new ArrayList<>();
  // The array of music scores, that's later converted to an ArrayList.
  MusicScore[] myMusicScoreArray;
  // Converts the strings into its MIDI equivalent.
  public HashMap<String, Integer> notesConversion = new HashMap<>();

  /**
   * Constructor for the music sheet.
   * This is also where the notes are converted into their MIDI equivalent.
   * @param name The name of the music sheet.
   * @param tempo The tempo of the music sheet.
   * @param length The length of the music sheet.
   */
  public MusicSheet(String name, String tempo, int length) {
    this.musicSheetName = name;
    this.musicSheetTempo = tempo;
    this.musicSheetLength = length;

    notesConversion.put("none", 0);
    notesConversion.put("A0", 21);
    notesConversion.put("A#0", 22);
    notesConversion.put("B0", 23);

    notesConversion.put("C1", 24);
    notesConversion.put("C#1", 25);
    notesConversion.put("D1", 26);
    notesConversion.put("D#1", 27);
    notesConversion.put("E1", 28);
    notesConversion.put("F1", 29);
    notesConversion.put("F#1", 30);
    notesConversion.put("G1", 31);
    notesConversion.put("G#1", 32);
    notesConversion.put("A1", 33);
    notesConversion.put("A#1", 34);
    notesConversion.put("B1", 35);

    notesConversion.put("C2", 36);
    notesConversion.put("C#2", 37);
    notesConversion.put("D2", 38);
    notesConversion.put("D#2", 39);
    notesConversion.put("E2", 40);
    notesConversion.put("F2", 41);
    notesConversion.put("F#2", 42);
    notesConversion.put("G2", 43);
    notesConversion.put("G#2", 44);
    notesConversion.put("A2", 45);
    notesConversion.put("A#2", 46);
    notesConversion.put("B2", 47);

    notesConversion.put("C3", 48);
    notesConversion.put("C#3", 49);
    notesConversion.put("D3", 50);
    notesConversion.put("D#3", 51);
    notesConversion.put("E3", 52);
    notesConversion.put("F3", 53);
    notesConversion.put("F#3", 54);
    notesConversion.put("G3", 55);
    notesConversion.put("G#3", 56);
    notesConversion.put("A3", 57);
    notesConversion.put("A#3", 58);
    notesConversion.put("B3", 59);

    notesConversion.put("C4", 60);
    notesConversion.put("C#4", 61);
    notesConversion.put("D4", 62);
    notesConversion.put("D#4", 63);
    notesConversion.put("E4", 64);
    notesConversion.put("F4", 65);
    notesConversion.put("F#4", 66);
    notesConversion.put("G4", 67);
    notesConversion.put("G#4", 68);
    notesConversion.put("A4", 69);
    notesConversion.put("A#4", 70);
    notesConversion.put("B4", 71);

    notesConversion.put("C5", 72);
    notesConversion.put("C#5", 73);
    notesConversion.put("D5", 74);
    notesConversion.put("D#5", 75);
    notesConversion.put("E5", 76);
    notesConversion.put("F5", 77);
    notesConversion.put("F#5", 78);
    notesConversion.put("G5", 79);
    notesConversion.put("G#5", 80);
    notesConversion.put("A5", 81);
    notesConversion.put("A#5", 82);
    notesConversion.put("B5", 83);

    notesConversion.put("C6", 84);
    notesConversion.put("C#6", 85);
    notesConversion.put("D6", 86);
    notesConversion.put("D#6", 87);
    notesConversion.put("E6", 88);
    notesConversion.put("F6", 89);
    notesConversion.put("F#6", 90);
    notesConversion.put("G6", 91);
    notesConversion.put("G#6", 92);
    notesConversion.put("A6", 93);
    notesConversion.put("A#6", 94);
    notesConversion.put("B6", 95);

    notesConversion.put("C7", 96);
    notesConversion.put("C#7", 97);
    notesConversion.put("D7", 98);
    notesConversion.put("D#7", 99);
    notesConversion.put("E7", 100);
    notesConversion.put("F7", 101);
    notesConversion.put("F#7", 102);
    notesConversion.put("G7", 103);
    notesConversion.put("G#7", 104);
    notesConversion.put("A7", 105);
    notesConversion.put("A#7", 106);
    notesConversion.put("B7", 107);

    notesConversion.put("C8", 108);
    notesConversion.put("C#8", 109);
    notesConversion.put("D8", 110);
    notesConversion.put("D#8", 111);
    notesConversion.put("E8", 112);
    notesConversion.put("F8", 113);
    notesConversion.put("F#8", 114);
    notesConversion.put("G8", 115);
    notesConversion.put("G#8", 116);
    notesConversion.put("A8", 117);
    notesConversion.put("A#8", 118);
    notesConversion.put("B8", 119);

    notesConversion.put("C9", 120);
    notesConversion.put("C#9", 121);
    notesConversion.put("D9", 122);
    notesConversion.put("D#9", 123);
    notesConversion.put("E9", 124);
    notesConversion.put("F9", 125);
    notesConversion.put("F#9", 126);
    notesConversion.put("G9", 127);
  }

  /**
   * Returns the length for one note depending on the tempo chosen.
   * @return The tempo number of the music sheet.
   */

  public int getNoteLength() {

    switch (musicSheetTempo) {
      case "Larghissimo" -> musicSheetNoteLength = 1500;
      case "Lento" -> musicSheetNoteLength = 1000;
      case "Andante" -> musicSheetNoteLength = 500;
      case "Moderato" -> musicSheetNoteLength = 300;
      case "Allegro" -> musicSheetNoteLength = 175;
      case "Presto" -> musicSheetNoteLength = 150;
      default -> System.out.println("Couldn't assign tempo to length, refer to code.");
    }
    return musicSheetNoteLength;
  }

  /**
   * Returns the name of the music sheet.
   * @return The name of the music sheet.
   */

  public String getName() {
    return musicSheetName;
  }

  /**
   * Where the conversion of the notes into MIDI takes place.
   * Those notes are then added to a new music score.
   * @param instrumentName The instrument name.
   * @param notes The notes to be converted.
   * @param soft Whether the instrument is soft or loud.
   */

  public void addScore(String instrumentName, List<String> notes, boolean soft) {
      // Creates an array of the size of the notes from the list.
      notesArray = new int[notes.size()];

      // For each note in the list, convert it into its MIDI equivalent into the array.
      for (int i = 0; i < notes.size(); i++) {
        notesArray[i] = notesConversion.get(notes.get(i));
      }

      // Adds to the array list, to then later convert back to array.
      myMusicScore = new MusicScore(instrumentName, notesArray, soft);
      myMusicScoreArrayList.add(myMusicScore);
  }

  /**
   * Returns the number of notes in the music sheet.
   * @return The size of the music sheet.
   */

  public int getLength() {
    return musicSheetLength;
  }

  /**
   * Returns the music score array from the array list by converting it.
   * Convert the ArrayList to an array.
   * @return The music score array.
   */

  public MusicScore[] getScores() {
    myMusicScoreArray = new MusicScore[myMusicScoreArrayList.size()];

    for (int i = 0; i < myMusicScoreArrayList.size(); i++) {
      myMusicScoreArray[i] = myMusicScoreArrayList.get(i);
    }

    return myMusicScoreArray;
  }
}
