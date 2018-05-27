package tables_extractor.file;

import java.io.File;

public abstract class PdfFileManager{

    public abstract File getTableInfoFile(File pdf, int pageNumber, int tableId);

    public abstract File getTableCsvFile(File pdf, int pageNumber, int tableId);

    public abstract File getPageInfoFile(File pdf, int pageNumber);

    public abstract File getPageImageFile(File pdf, int pageNumber);

    public abstract File getDocumentInfoFile(File pdf);

    // make dirs and return file
    protected File makeDirs(File file){
        file.getParentFile().mkdirs();
        return file;
    }
}