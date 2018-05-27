package tables_extractor.file;

import java.io.File;
import java.nio.file.Paths;

import com.google.inject.Inject;

import tables_extractor.Utils;
import tables_extractor.inject.annotations.OutputDir;

public class DeepPdfFileManager extends PdfFileManager{

    private final File outputDir;

    @Inject
    public DeepPdfFileManager(@OutputDir File outputDir){
        this.outputDir = outputDir;
    }

    @Override
    public File getTableInfoFile(File pdf, int pageNumber, int tableId) {
        return makeDirs(Paths.get(
            outputDir.toString(),
            Utils.getFileNameWithoutExtension(pdf),
            "tables", 
            String.format("page.%03d.table.%02d.%s", pageNumber, tableId, "json")
        ).toFile());
    }

    @Override
    public File getTableCsvFile(File pdf, int pageNumber, int tableId) {
        return makeDirs(Paths.get(
            outputDir.toString(), 
            Utils.getFileNameWithoutExtension(pdf),
            "tables", 
            String.format("page.%03d.table.%02d.%s", pageNumber, tableId, "csv")
        ).toFile());
    }

    @Override
    public File getPageInfoFile(File pdf, int pageNumber) {
        return makeDirs(Paths.get(
            outputDir.toString(), 
            Utils.getFileNameWithoutExtension(pdf),
            "pages", 
            String.format("page.%03d.%s", pageNumber, "json")
        ).toFile());
    }

    @Override
    public File getPageImageFile(File pdf, int pageNumber) {
        return makeDirs(Paths.get(
            outputDir.toString(), 
            Utils.getFileNameWithoutExtension(pdf),
            "pages", 
            String.format("page.%03d.%s", pageNumber, "png")
        ).toFile());
    }

    @Override
    public File getDocumentInfoFile(File pdf) {
        return makeDirs(Paths.get(
            outputDir.toString(),
            Utils.getFileNameWithoutExtension(pdf),
            "document.json"
        ).toFile());
    }

}