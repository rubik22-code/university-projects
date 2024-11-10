import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import music.Composition;
import music.MusicSheet;
import people.conductors.Conductor;
import people.musicians.Cellist;
import people.musicians.Musician;
import people.musicians.Pianist;
import people.musicians.Violinist;
import utils.SoundSystem;

/**
 * To simulate a group of musicians playing some music.
 */

public class EcsBandAid {

  SoundSystem newSoundSystem;
  List<MusicSheet> compositionCollection;
  List<Musician> musicianCollection;
  Random newRandom;
  Conductor newConductor;

  /**
   * Constructor for the EcsBandAid class.
   * The conductor is initialised in the constructor as it's sharing only one conductor amongst the musicians.
   * @param newSoundSystem the sound system to be used.
   * @param musicianCollection the list of musicians to be used.
   * @param compositionCollection the list of compositions to be used.
   */
  public EcsBandAid(SoundSystem newSoundSystem, List<Musician> musicianCollection, List<MusicSheet> compositionCollection) {
    this.newSoundSystem = newSoundSystem;
    this.compositionCollection = compositionCollection;
    this.musicianCollection = musicianCollection;

    newConductor = new Conductor("newConductor", newSoundSystem);
  }

  /**
   * This method is used to simulate the musicians playing the music.
   * @param args the command line arguments.
   * @throws IOException if the file is not found.
   */

  public static void main(String[] args) throws IOException {

    SoundSystem newSoundSystem = null;

    try {
      newSoundSystem = new SoundSystem();
      newSoundSystem.setTextMode(true);
    } catch (Exception e) {
      System.err.println("Error: Cannot create the SoundSystem.");
    }

    List<Musician> musicianCollection = readMusicianFile(newSoundSystem, args[0]);
    List<MusicSheet> compositionCollection = readCompositionFile(args[1]);
    int years = Integer.parseInt(args[2]);

    EcsBandAid newBandAid = new EcsBandAid(newSoundSystem, musicianCollection, compositionCollection);

    newBandAid.registerToConductor();

    /**
     * This performs the simulation using the musicians file and the compositions file.
     */

    for (int i = 0; i < years; i++) {
      newBandAid.performForAYear(musicianCollection);
    }
  }

  /**
   * Reading the musicians names and what instruments they play from the file.
   * @param newSoundSystem the sound system to be used.
   * @param filename the name of the file to be read.
   * @return the list of musicians with their respective names and instruments.
   */

  public static List<Musician> readMusicianFile(SoundSystem newSoundSystem, String filename) {
    BufferedReader musicianBufferedReader = null;

    // Attempts to read the file.

    try {
      FileReader musicianFileReader = new FileReader(filename);
      musicianBufferedReader = new BufferedReader(musicianFileReader);
    } catch (Exception e) {
      System.out.println("Cannot create a file reader.");
    }

    List<Musician> musicianCollection = new ArrayList<>();

    // Read the line from the file, and split it into an array to retrieve the name and instrument.

    try {
      while (musicianBufferedReader.ready()) {

        /*
        Split the line into an array, where the first element is the name and the second element is the instrument.
        However, the instrument includes the bracket, so it needs to be removed by splitting again into a new array.
         */

        String splitter = musicianBufferedReader.readLine();
        String[] splitterArrayMusician = splitter.split("\\(");
        String musicianName;
        musicianName = splitterArrayMusician[0];
        String instrumentNameWithBracket;
        instrumentNameWithBracket = splitterArrayMusician[1];
        String[] splitterArrayInstrument = instrumentNameWithBracket.split("\\)");
        String instrumentNameWithoutBracket;
        instrumentNameWithoutBracket = splitterArrayInstrument[0];

        // Creates new musicians based on the instrument they play.

        switch (instrumentNameWithoutBracket) {
          case "Piano" -> {
            Pianist newPianist;
            newPianist = new Pianist(musicianName, newSoundSystem);
            musicianCollection.add(newPianist);
          }
          case "Cello" -> {
            Cellist newCellist;
            newCellist = new Cellist(musicianName, newSoundSystem);
            musicianCollection.add(newCellist);
          }
          case "Violin" -> {
            Violinist newViolinist;
            newViolinist = new Violinist(musicianName, newSoundSystem);
            musicianCollection.add(newViolinist);
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Couldn't run while loop.");
    }

    return musicianCollection;
  }

  /**
   * Reading the compositions from the file.
   * @param filename the name of the file to be read.
   * @return the list of compositions.
   */

  public static List<MusicSheet> readCompositionFile(String filename) throws IOException {
    FileReader compositionFileReader = new FileReader(filename);
    BufferedReader compositionBufferedReader = new BufferedReader(compositionFileReader);
    List<MusicSheet> compositionCollection = new ArrayList<>();
    MusicSheet newMusicSheet;

    // Read the line from the file, similar to the above method, and split it into an array to retrieve the name, tempo and length.

    while (compositionBufferedReader.ready()) {

      String splitter = compositionBufferedReader.readLine();

      String compositionName = "";
      String compositionTempo = "";
      String compositionLength = "";

      // This is to check if the line starts from a new music sheet.

      while (splitter.startsWith("Name:") || splitter.startsWith("Tempo:") || splitter.startsWith("Length:")) {

        // Only accesses part of the array to add to the new music sheet, where splits are based on ":".

        String[] splitterArray = splitter.split(": ");
        compositionName = splitterArray[1];
        splitter = compositionBufferedReader.readLine();
        splitterArray = splitter.split(": ");
        compositionTempo = splitterArray[1];
        splitter = compositionBufferedReader.readLine();
        splitterArray = splitter.split(": ");
        compositionLength = splitterArray[1];

        splitter = compositionBufferedReader.readLine();

        newMusicSheet = new MusicSheet(compositionName, compositionTempo, Integer.parseInt(compositionLength));

        String instrumentName = "";
        boolean soft = false;
        List<String> notesList = null;

        // This is to check if the line starts from a new instrument.

        while (!splitter.startsWith("Name:") || !splitter.startsWith("Tempo:") || !splitter.startsWith("Length:")) {

          // Splits based on ",".

          String[] splitterArrayNotes = splitter.split(", ", 3);

          instrumentName = splitterArrayNotes[0];

          soft = splitterArrayNotes[1].equals("soft");

          // Removes the brackets from the notes, and splits it into an array.

          String notes = splitterArrayNotes[2];
          notes = notes.replaceAll("\\{", "");
          notes = notes.replaceAll("}", "");
          String[] notesArray = notes.split(",");

          // Trims to accept both formats.

          for (int i = 0; i < notesArray.length; i++) {
            notesArray[i] = notesArray[i].trim();
          }

          /// This is where the notes conversion goes, where the notes are converted from an array into a list of notes.

          notesList = new ArrayList<>();

          // Adds every element of the notes array to the notes list.

          Collections.addAll(notesList, notesArray);

          newMusicSheet.addScore(instrumentName, notesList, soft);

          // This is to check if the line starts from a new instrument, or if it is the end of the file.

          if (compositionBufferedReader.ready()) {
            splitter = compositionBufferedReader.readLine();
          }

          // If this condition is true, that must mean it must escape to the outer while loop, and create a new music sheet.

          if (splitter.startsWith("Name:") || splitter.startsWith("Tempo:") || splitter.startsWith("Length:") || !compositionBufferedReader.ready()) {
            break;
          }
        }
        compositionCollection.add(newMusicSheet);
      }
    }
    return compositionCollection;
  }

  /**
   * This method is used to play the compositions using the musicians.
   * @param musicianCollection the list of musicians.
   */

  public void performForAYear(List<Musician> musicianCollection) {

    ArrayList<Musician> musicianArrayList = new ArrayList<>(musicianCollection);

    newRandom = new Random();

    // Conductor plays three random compositions.

    for (int i = 0; i < 3; i++) {
      Composition randomComposition = compositionCollection.get(newRandom.nextInt(compositionCollection.size())); // Chooses a element in that index by the bounds of the size.
      newConductor.playComposition(randomComposition);
    }

    // Each musician has a 50% chance to leave the band.

    int musicianChance = newRandom.nextInt(musicianArrayList.size());

    if (newRandom.nextBoolean()) {
      newConductor.unregisterMusician(musicianArrayList.get(musicianChance));
    }
  }

  /**
   * This method registers the musicians to the conductor only once.
   * This is separate from the performForAYear method, because the musicians can only be registered once.
   */

  public void registerToConductor() {
    for (Musician i : musicianCollection) {
      newConductor.registerMusician(i);
    }
  }
}
