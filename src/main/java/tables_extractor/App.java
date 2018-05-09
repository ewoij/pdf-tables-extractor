package tables_extractor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Rectangle;
import technology.tabula.Table;
import technology.tabula.detectors.DetectionAlgorithm;
import technology.tabula.detectors.NurminenDetectionAlgorithm;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.writers.CSVWriter;

public class App 
{
    private static DetectionAlgorithm detector = new NurminenDetectionAlgorithm();
    private static BasicExtractionAlgorithm tableExtractor = new BasicExtractionAlgorithm();

    public static void main(String[] args) throws Exception
    {
        String pdfDirectoryPath = args[0];
        String outputDirectoryPath = args[1];
        File pdfDirectory = new File(pdfDirectoryPath);
        File outputDirectory = new File(outputDirectoryPath);

        File[] pdfs = getFiles(pdfDirectory, "pdf");

        for (File pdfFile : pdfs) {
            try {
                saveItems(pdfFile, outputDirectory);
            } catch (Exception e) {
                String message = "Error: '" + pdfFile.toString() + "': " + e.toString();
				System.err.println(message);
                System.out.println(message);
            }
        }
    }

	private static File[] getFiles(File pdfDirectory, final String ext) {
		return pdfDirectory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("." + ext);
            }
        });
	}

	private static void saveItems(File pdfFile, File outputDirectory) throws Exception {
        FileManager fileManager = new FileManager(outputDirectory, pdfFile);
        PDDocument pdDocument = PDDocument.load(pdfFile);
        PDDocument pdDocumentTabula = PDDocument.load(pdfFile); // I have to create another doc just for tabula, because for some unknow reason, the first page raise "java.io.IOException: COSStream has been closed and cannot be read." when trying to get its content (only on first, others are fine)
        saveDocumentInfo(pdDocument, fileManager);
        ObjectExtractor objectExtractor = new ObjectExtractor(pdDocumentTabula);
        try {
            PageIterator pageIterator = objectExtractor.extract();
            while(pageIterator.hasNext()){
                Page page = pageIterator.next();
                List<TableResult> tables = getTables(page);
                if (tables.size() > 0){
                    int pageNumber = page.getPageNumber();
                    saveTables(tables, pageNumber, fileManager);
                    savePageImage(pdDocument, pageNumber, fileManager);
                    savePageInfo(page, pageNumber, fileManager);
                }
            }
        }
        finally {
            pdDocument.close();
            pdDocumentTabula.close();
            objectExtractor.close();
        }
	}

	private static void saveDocumentInfo(PDDocument pdDocument, FileManager fileManager) throws FileNotFoundException {
        JsonObject jObject = new JsonObject();
        jObject.addProperty("numberOfPages", pdDocument.getNumberOfPages());
        stringToFile(jObject.toString(), fileManager.getDocumentInfoFile());
    }

	private static List<TableResult> getTables(Page page) throws Exception {
        List<Rectangle> guesses = detector.detect(page);
        List<TableResult> tables = new ArrayList<TableResult>();
        for (Rectangle guess : guesses) {
            Page area = page.getArea(guess);
            List<Table> extractedTables = tableExtractor.extract(area);
            if (extractedTables.size() > 1){
                throw new Exception("More than 1 table extracted per guess.");
            }
            Table table = extractedTables.get(0);
            tables.add(new TableResult(table, guess));
        }

        return tables;
	}

	private static void saveTables(List<TableResult> tables, int pageNumber, FileManager fileManager) throws IOException {
        int tableId = 0;
		for (TableResult table : tables) {
            saveTableInfo(pageNumber, tableId, table, fileManager);
            saveTable(pageNumber, tableId, table, fileManager);
            tableId++;
        }
	}

	private static void saveTable(int pageNumber, int tableId, TableResult table, FileManager fileManager) throws IOException {
        StringBuilder sb = new StringBuilder();
        (new CSVWriter()).write(sb, table.getTable());
        stringToFile(sb.toString(), fileManager.getTableCsvFile(pageNumber, tableId));
    }

	private static void saveTableInfo(int pageNumber, int tableId, TableResult table, FileManager fileManager) throws FileNotFoundException {
        JsonObject jArea = new JsonObject();
        Rectangle area = table.getArea();
		jArea.addProperty("x", area.getX());
        jArea.addProperty("y", area.getY());
        jArea.addProperty("width", area.getWidth());
        jArea.addProperty("height", area.getHeight());

        JsonObject jObject = new JsonObject();
        jObject.add("area", jArea);

        stringToFile(jObject.toString(), fileManager.getTableInfoFile(pageNumber, tableId));
    }

	private static void savePageImage(PDDocument pdDocument, int pageNumber, FileManager fileManager) throws IOException {
		BufferedImage renderImage = new PDFRenderer(pdDocument).renderImage(pageNumber - 1, 2f);
		File outputFile = fileManager.getPageImageFile(pageNumber);
        ImageIO.write(renderImage, "png", outputFile);
	}

	private static void savePageInfo(Page page, int pageNumber, FileManager fileManager) throws FileNotFoundException {
        JsonObject area = new JsonObject();
        area.addProperty("width", page.getWidth());
        area.addProperty("height", page.getHeight());

        JsonObject jObject = new JsonObject();
        jObject.add("dimensions", area);

        stringToFile(jObject.toString(), fileManager.getPageInfoFile(pageNumber));
    }
    
    private static void stringToFile(String s, File file) throws FileNotFoundException{
        PrintWriter out = new PrintWriter(file);
        try {
            out.write(s);
        }
        finally {
            out.close();
        }
    }
}
