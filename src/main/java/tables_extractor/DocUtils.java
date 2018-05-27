package tables_extractor;

import java.util.ArrayList;
import java.util.List;

import tables_extractor.TableResult;
import technology.tabula.Page;
import technology.tabula.Rectangle;
import technology.tabula.Table;
import technology.tabula.detectors.DetectionAlgorithm;
import technology.tabula.detectors.NurminenDetectionAlgorithm;
import technology.tabula.extractors.BasicExtractionAlgorithm;

public class DocUtils{

	public List<TableResult> extractTables(Page page) {
		DetectionAlgorithm detector = new NurminenDetectionAlgorithm();
		BasicExtractionAlgorithm tableExtractor = new BasicExtractionAlgorithm();
		List<Rectangle> guesses = detector.detect(page);
        List<TableResult> tables = new ArrayList<TableResult>();
        for (Rectangle guess : guesses) {
            Page area = page.getArea(guess);
            List<Table> extractedTables = tableExtractor.extract(area);
            Table table = extractedTables.get(0);
            tables.add(new TableResult(table, guess));
        }
        return tables;
	}

}