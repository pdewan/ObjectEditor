package bus.uigen.loggable;

public interface LoggableTableModelEvent {

	public IdentifiableLoggable getSource();

	public void setSource(IdentifiableLoggable source);

	public int getFirstRow();

	public void setFirstRow(int firstRow);

	public int getLastRow();

	public void setLastRow(int lastRow);

	public int getColumn();

	public void setColumn(int column);

	public int getType();

	public void setType(int type);

}