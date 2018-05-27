package tables_extractor;

import java.util.List;

import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;

public class SimpleTableHeuristic {

	public boolean isTable(Table table) {
		List<RectangularTextContainer> cells = table.getCells();
		int digitsCount = 0;
		int charCount = 0;
		int wordCount = 0;

		for (RectangularTextContainer cell : cells) {
			String text = cell.getText();
			wordCount += text.split("\\s+").length;
			charCount += text.length();
			for(int i = 0; i< text.length(); i++){
				char c = text.charAt(i);
				if (Character.isDigit(c)){
					digitsCount++;
				}
			}
		}

		double digitsPerc = digitsCount * 1. / charCount;
		double cellWordsMean = wordCount * 1. / cells.size();
		return digitsPerc > 0.1 && cellWordsMean < 5.;
	}

}