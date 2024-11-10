import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.io.ConcatenateJavaFiles;

public class JavaFileUtil implements ConcatenateJavaFiles {

  public void concatenateJavaFiles(String dirName, String fileName) throws IOException {

    File inputFile = new File(dirName);

    String fileLine;

    File[] files = inputFile.listFiles();

    // Checking if the file is a directory.

    if (!inputFile.isDirectory()) {
      throw new IllegalArgumentException("This is not a directory");
    }

    // Locating the input file.

    FileWriter fileWriter = new FileWriter(dirName + File.separator + fileName);

    for (File file : files) {

      // Merging the files together by looking at all files within the directory.

      BufferedReader reader = new BufferedReader(new FileReader(file));

      if (!file.isDirectory() && file.getName().endsWith(".java")) {
        // Reading until it reaches the end of the file.
        while ((fileLine = reader.readLine()) != null) {
          fileWriter.write(fileLine);
          fileWriter.write("\n");
        }
      }
      reader.close();
    }
    fileWriter.close();
  }
}
