package bus.uigen.loggable;

public class ALoggableTableModelEvent implements LoggableTableModelEvent {
	IdentifiableLoggable source;
	int firstRow;
	int lastRow;
	int column;
	int type;
	public ALoggableTableModelEvent (IdentifiableLoggable theSource,
			int theFirstRow, int theLastRow, int theColumn, int theType) {
		source = theSource;
		firstRow = theFirstRow;
		lastRow = theLastRow;
		column = theColumn;
		type = theType;
	}
	public IdentifiableLoggable getSource() {
		return source;
	}
	public void setSource(IdentifiableLoggable source) {
		this.source = source;
	}
	public int getFirstRow() {
		return firstRow;
	}
	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}
	public int getLastRow() {
		return lastRow;
	}
	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	

}
