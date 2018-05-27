package tables_extractor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.google.inject.Inject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import tables_extractor.file.PdfFileManager;
import technology.tabula.Page;
import technology.tabula.Rectangle;
import technology.tabula.writers.CSVWriter;

public class Repository {

    private final PdfFileManager pdfFileManager;
    private final IOUtils ioUtils;
    private final Logger logger;

    @Inject
    public Repository(PdfFileManager pdfFileManager, IOUtils ioUtils, Logger logger){
        this.pdfFileManager = pdfFileManager;
        this.ioUtils = ioUtils;
        this.logger = logger;
    }

    public void saveDocInfo(File pdf, PDDocument pdDocument) throws FileNotFoundException {
        JsonObject jObject = new JsonObject();
        jObject.addProperty("numberOfPages", pdDocument.getNumberOfPages());
        jObject.addProperty("originalFile", pdf.toString());
        ioUtils.dump(jObject.toString(), pdfFileManager.getDocumentInfoFile(pdf));
    }

    public void savePageImage(File pdf, PDDocument pdDocument, int pageNumber) throws IOException {
        BufferedImage renderImage = new PDFRenderer(pdDocument).renderImage(pageNumber - 1, 2f);
        File outputFile = pdfFileManager.getPageImageFile(pdf, pageNumber);
        ImageIO.write(renderImage, "png", outputFile);
        logger.log(Level.INFO, "Saved image of page {0}", pageNumber);
    }

    public void savePageInfo(File pdf, Page page, int pageNumber) throws FileNotFoundException {
        JsonObject area = new JsonObject();
        area.addProperty("width", page.getWidth());
        area.addProperty("height", page.getHeight());

        JsonObject jObject = new JsonObject();
        jObject.add("dimensions", area);

        ioUtils.dump(jObject.toString(), pdfFileManager.getPageInfoFile(pdf, pageNumber));
        logger.log(Level.INFO, "Saved info of page {0}", pageNumber);
    }

    public void saveTable(File pdf, int pageNumber, TableResult table, int tableId) throws IOException {
        StringBuilder sb = new StringBuilder();
        (new CSVWriter()).write(sb, table.getTable());
        ioUtils.dump(sb.toString(), pdfFileManager.getTableCsvFile(pdf, pageNumber, tableId));
        logger.log(Level.INFO, "Saved table {0} of page {1}", new Object[]{tableId, pageNumber});
    }

    public void saveTableInfo(File pdf, int pageNumber, TableResult table, int tableId) throws FileNotFoundException {
        JsonObject jArea = new JsonObject();
        Rectangle area = table.getArea();
        jArea.addProperty("x", area.getX());
        jArea.addProperty("y", area.getY());
        jArea.addProperty("width", area.getWidth());
        jArea.addProperty("height", area.getHeight());

        JsonObject jObject = new JsonObject();
        jObject.add("area", jArea);

        ioUtils.dump(jObject.toString(), pdfFileManager.getTableInfoFile(pdf, pageNumber, tableId));
        logger.log(Level.INFO, "Saved info of table {0} from page {1}", new Object[]{tableId, pageNumber});
    }

}