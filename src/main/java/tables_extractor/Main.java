package tables_extractor;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import tables_extractor.IOUtils;
import tables_extractor.Logging;
import tables_extractor.PdfTablesExtractor;
import tables_extractor.inject.annotations.InputDir;
import tables_extractor.inject.annotations.InputFile;

public class Main {
    private final File inputDir;
    private final File inputFile;
    private final PdfTablesExtractor pdfTablesExtractor;
    private final IOUtils ioUtils;
    private final Logging logging;
    private final Logger logger;

    @Inject
    public Main(
        @Nullable @InputDir File inputDir, 
        @Nullable @InputFile File inputFile, 
        PdfTablesExtractor pdfTablesExtractor, 
        IOUtils ioUtils, 
        Logging logging, 
        Logger logger){
            this.inputDir = inputDir;
            this.inputFile = inputFile;
            this.pdfTablesExtractor = pdfTablesExtractor;
            this.ioUtils = ioUtils;
            this.logging = logging;
            this.logger = logger;
    }

    public void execute() throws SecurityException, IOException {
        logging.init();

        File[] pdfs = inputDir != null ? ioUtils.getFiles(inputDir, "pdf") : new File[]{ inputFile };

        for (int docId = 0; docId < pdfs.length; docId++){
            File pdf = pdfs[docId];
            try {
                logger.log(Level.INFO, "Processing document {0} / {1}. File: {2}", new Object[]{docId, pdfs.length, pdf.toString()});
                pdfTablesExtractor.extract(pdf);
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to process document '%s'.", pdf), e);
            }
        }
    }

}