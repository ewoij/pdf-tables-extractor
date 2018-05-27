package tables_extractor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import tables_extractor.inject.annotations.RunTableHeuristic;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;

public class PdfTablesExtractor {
    private final Repository repository;
    private final DocUtils docUtils;
    private final SimpleTableHeuristic simpleTableHeuristic;
    private final Logger logger;
    private final boolean runTableHeuristic;

    @Inject
    public PdfTablesExtractor(Repository repository, DocUtils docUtils, SimpleTableHeuristic simpleTableHeuristic, Logger logger, @RunTableHeuristic boolean runTableHeuristic) {
        this.repository = repository;
        this.docUtils = docUtils;
        this.simpleTableHeuristic = simpleTableHeuristic;
        this.logger = logger;
        this.runTableHeuristic = runTableHeuristic;
    }

    public void extract(File pdf) throws InvalidPasswordException, IOException {
        PDDocument pdDocument = PDDocument.load(pdf);
        PDDocument pdDocumentTabula = PDDocument.load(pdf); // I have to create another doc just for tabula, because for
                                                            // some unknown reason, the first page raise
                                                            // "java.io.IOException: COSStream has been closed and
                                                            // cannot be read." when trying to get its content (only on
                                                            // first, others are fine)
        repository.saveDocInfo(pdf, pdDocument);
        ObjectExtractor objectExtractor = new ObjectExtractor(pdDocumentTabula);
        try {
            int pageNumber = 1;
            int numberOfPages = pdDocument.getNumberOfPages();
            PageIterator pageIterator = objectExtractor.extract();
            while (pageIterator.hasNext()) {
                Page page = pageIterator.next();
                logger.log(Level.INFO, "Processing page {0} / {1}", new Object[]{pageNumber, numberOfPages});
                try {
                    processPage(pdf, pdDocument, page, pageNumber);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("An error occured while saving the page items. File: %s, Page %s", pdf, pageNumber), e);
                }
                pageNumber++;
            }
        } finally {
            pdDocument.close();
            pdDocumentTabula.close();
            objectExtractor.close();
        }
    }

    private void processPage(File pdf, PDDocument pdDocument, Page page, int pageNumber) {
        List<TableResult> tables = docUtils.extractTables(page);

        int savedTablesCount = 0;
        for(int tableId = 0; tableId < tables.size(); tableId++){
            TableResult table = tables.get(tableId);

            if (shouldSaveTable(pdf, table, pageNumber, tableId)){
                savedTablesCount++;
                saveTable(pdf, pageNumber, table, tableId);
            }
        }

        if (savedTablesCount > 0){
            savePage(pdf, pdDocument, page, pageNumber);
        }
    }

    private boolean shouldSaveTable(File pdf, TableResult table, int pageNumber, int tableId) {
        if (!runTableHeuristic) return true;
        boolean isTable = false;
        try {
            isTable = simpleTableHeuristic.isTable(table.getTable());
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("An error occured while running the heuristic to predict a table. File: %s, Page %s", pdf, pageNumber), e);
            return false;
        }
        if (isTable) {
            logger.log(Level.INFO, "Page {0}, Table {1} was predicted as a table.", new Object[]{pageNumber, tableId});
        } else {
            logger.log(Level.INFO, "Page {0}, Table {1} was filtered out, considered by the heuristic not being a table.", new Object[]{pageNumber, tableId});
        }
        return isTable;
    }
    
    private void saveTable(File pdf, int pageNumber, TableResult table, int tableId){
        try {
            repository.saveTable(pdf, pageNumber, table, tableId);
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("Failed to save table. Page %s, table %s", pageNumber, tableId), e);
        }
        try {
            repository.saveTableInfo(pdf, pageNumber, table, tableId);
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("Failed to save table info. Page %s, table %s", pageNumber, tableId), e);
        }   
    }

    private void savePage(File pdf, PDDocument pdDocument, Page page, int pageNumber){
        try {
            repository.savePageImage(pdf, pdDocument, pageNumber);
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("Failed to save page image. Page %s.", pageNumber), e);
        }
        try {
            repository.savePageInfo(pdf, page, pageNumber);
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("Failed to save page info. Page %s.", pageNumber), e);
        }
    }
}