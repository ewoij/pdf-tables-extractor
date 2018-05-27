package tables_extractor;

import technology.tabula.Rectangle;
import technology.tabula.Table;

public class TableResult {
    private final Table table;
    private final Rectangle area;

    public TableResult(Table table, Rectangle area) {
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