package tables_extractor;

import technology.tabula.Rectangle;
import technology.tabula.Table;

class TableResult
{
    private Table table;
    private Rectangle area;
    
    public TableResult(Table table, Rectangle area){
        this.table = table;
        this.area = area;
    }

	public Table getTable() {
		return table;
    }
    
	public Rectangle getArea() {
		return area;
	}
}