package tables_extractor;

import java.io.File;

public class Utils {
  public static String getFileNameWithoutExtension(File file) {
    return file.getName().replaceFirst("\\.[^.]+$", "");
  };
}