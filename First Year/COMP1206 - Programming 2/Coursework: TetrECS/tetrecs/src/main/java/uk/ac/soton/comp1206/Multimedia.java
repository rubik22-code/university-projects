package uk.ac.soton.comp1206;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Multimedia {

  static MediaPlayer audioPlayer;
  static MediaPlayer musicPlayer;

  static boolean musicEnabled = true;

  /**
   * Play an audio file (short sounds), linking Media to MediaPlayer.
   * @param audioFilePath the path to the audio file
   */

  public static void playAudio(String audioFilePath) {

    if (!musicEnabled) {
      return;
    }

    String musicToPlay = Multimedia.class.getResource("/sounds/" + audioFilePath).toExternalForm();

    try {
      Media audio = new Media(musicToPlay);
      audioPlayer = new MediaPlayer(audio);
      audioPlayer.play();

    } catch (Exception e) {
      e.printStackTrace();
      musicEnabled = false;
    }
  }

  /**
   * Play a music file (background music), linking Media to MediaPlayer.
   * @param audioFilePath the path to the music file
   */

  public static void playMusic(String audioFilePath) {

    if (!musicEnabled) {
      return;
    }

    String musicToPlay = Multimedia.class.getResource("/music/" + audioFilePath).toExternalForm();

    try {
      Media music = new Media(musicToPlay);
      musicPlayer = new MediaPlayer(music);

      // Loop the music

      musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
      musicPlayer.play();

    } catch (Exception e) {
      e.printStackTrace();
      musicEnabled = false;
    }
  }

  /**
   * Stop the audio or music player.
   */

  public static void stopPlay() {
    if (musicEnabled) {
      musicPlayer.stop();
    }
  }
}
