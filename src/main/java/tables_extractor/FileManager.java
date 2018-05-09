package tables_extractor;

import java.io.File;
import java.nio.file.Paths;

public class FileManager {
    private File dir;

    public FileManager(File outputDir, File pdfFile){
        String pdfNameWithoutExt = pdfFile.getName().replaceFirst("[.][^.]+$", "");
        dir = new File(outputDir, pdfNameWithoutExt);
    }

    public File getTableInfoFile(int pageNumber, int tableId){
        return makeDirs(Paths.get(dir.toString(), "tables", String.format("page.%03d.table.%02d.%s", pageNumber, tableId, "json")).toFile());
    }

    public File getTableCsvFile(int pageNumber, int tableId){
        return makeDirs(Paths.get(dir.toString(), "tables", String.format("page.%03d.table.%02d.%s", pageNumber, tableId, "csv")).toFile());
    }

    public File getPageInfoFile(int pageNumber){
        return makeDirs(Paths.get(dir.toString(), "pages", String.format("page.%03d.%s", pageNumber, "json")).toFile());
    }

    public File getPageImageFile(int pageNumber){
        return makeDirs(Paths.get(dir.toString(), "pages", String.format("page.%03d.%s", pageNumber, "png")).toFile());
    }

	public File getDocumentInfoFile() {
        return makeDirs(Paths.get(dir.toString(), "document.json").toFile());
	}

    // make dirs and return file
    private File makeDirs(File file){
        file.getParentFile().mkdirs();
        return file;
    }
}