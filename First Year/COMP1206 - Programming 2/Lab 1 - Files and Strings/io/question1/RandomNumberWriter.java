import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.Random;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.io.RandomIO;

public class RandomNumberWriter implements RandomIO {

  private long objectSeed;

  public RandomNumberWriter(long objectSeed) throws IOException {
    this.objectSeed = objectSeed;
  }

  // Write characters to a file.
  public void writeRandomChars(String randomCharParam) throws IOException {
    File randomChar = new File(randomCharParam);

    Writer outChar = new FileWriter(randomChar);

    Random random = new Random(objectSeed);

    for (int i = 0; i < 10000; i++) {
      int next = random.nextInt(100000);
      outChar.write(next + "\n");
    }

    outChar.close();
  }

  // Write bytes to a file.
  public void writeRandomByte(String randomByteParam) throws IOException {
    File randomByte = new File(randomByteParam);

    OutputStream outByte = new FileOutputStream(randomByte);

    Random random = new Random(objectSeed);

    for (int i = 0; i < 10000; i++) {
      int next = random.nextInt(100000);

      // Writing exactly 4 bytes for the integer.

      byte byteArray[] = ByteBuffer.allocate(4).putInt(next).array();

      outByte.write(byteArray);

    }
    outByte.close();
  }
}