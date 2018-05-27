package tables_extractor;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import tables_extractor.inject.annotations.OutputDir;

public class Logging
{
    private final File outputDir;
    private final Logger rootLogger = Logger.getLogger("");
    private final Logger pdfBoxLogger = Logger.getLogger("org.apache.pdfbox");

    @Inject
    public Logging(@OutputDir File outputDir){
        this.outputDir = outputDir;
    }

	public void init() throws SecurityException, IOException {
        Handler fileHandler = createFileHandler();
        rootLogger.addHandler(fileHandler);

        pdfBoxLogger.setLevel(Level.SEVERE); // too many warnings
    }

	private Handler createFileHandler() throws IOException {
		File logFile = getLogFile();
        logFile.getParentFile().mkdirs();
		Handler handler = new FileHandler(logFile.toString());
		return handler;
	}

	private File getLogFile() {
        String formattedDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
		String fileName = String.format("log-%s.log", formattedDate);
		return new File(outputDir, fileName);
	}
}