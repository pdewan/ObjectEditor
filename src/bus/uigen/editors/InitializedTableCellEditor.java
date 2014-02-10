package bus.uigen.editors;

import javax.swing.table.TableCellEditor;

import bus.uigen.widgets.table.VirtualTable;

public interface InitializedTableCellEditor extends TableCellEditor {
	public void init (VirtualTable theTable);

}
