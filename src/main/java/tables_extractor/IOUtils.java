package tables_extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;

public class IOUtils {

    public void dump(String string, File file) throws FileNotFoundException{
        PrintWriter out = new PrintWriter(file);
        try {
            out.write(string);
        }
        finally {
            out.close();
        }
    }

    public File[] getFiles(File pdfDirectory, final String ext) {
        return pdfDirectory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("." + ext);
            }
        });
    }
}