package tables_extractor.file;

import java.io.File;

import com.google.inject.Inject;

import tables_extractor.Utils;
import tables_extractor.inject.annotations.OutputDir;

public class FlatPdfFileManager extends PdfFileManager{

    private final File outputDir;
    
    @Inject
    public FlatPdfFileManager(@OutputDir File outputDir){
        this.outputDir = outputDir;
    }

    @Override
    public File getTableInfoFile(File pdf, int pageNumber, int tableId) {
        return makeDirs(new File(
            outputDir,
            String.format("%s.page.%03d.table.%02d.%s", getFileName(pdf), pageNumber, tableId, "json")
        ));
    }

    @Override
    public File getTableCsvFile(File pdf, int pageNumber, int tableId) {
        return makeDirs(new File(
            outputDir,
            String.format("%s.page.%03d.table.%02d.%s", getFileName(pdf), pageNumber, tableId, "csv")
        ));
    }

    @Override
    public File getPageInfoFile(File pdf, int pageNumber) {
        return makeDirs(new File(
            outputDir,
            String.format("%s.page.%03d.%s", getFileName(pdf), pageNumber, "json")
        ));
    }

    @Override
    public File getPageImageFile(File pdf, int pageNumber) {
        return makeDirs(new File(
            outputDir,
            String.format("%s.page.%03d.%s", getFileName(pdf), pageNumber, "png")
        ));
    }

    @Override
    public File getDocumentInfoFile(File pdf) {
        return makeDirs(new File(
            outputDir,
            String.format("%s.document.%s", getFileName(pdf), "json")
        ));
    }

    private String getFileName(File file){
        int maxLength = 200;
        String ends = "..";
        String fileName = Utils.getFileNameWithoutExtension(file);
        if (fileName.length() > maxLength){
            fileName = fileName.substring(0, maxLength - ends.length()) + ends;
        }
        return fileName;
    }
}