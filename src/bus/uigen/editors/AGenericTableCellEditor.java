package bus.uigen.editors;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

import bus.uigen.oadapters.ObjectAdapter;

public class AGenericTableCellEditor implements TableCellEditor {
	 public void addCellEditorListener(CellEditorListener l) {};
     
	  public void cancelCellEditing() {};
	          
	 public Object getCellEditorValue()  { 
		 //return obj; 
		 return null;
		 }
	 public  boolean isCellEditable(EventObject anEvent) {
		 return true;
		//return isEditable;
		};
	 public void removeCellEditorListener(CellEditorListener l){
		 
	 };
	 public boolean shouldSelectCell(EventObject anEvent){
		return true;
	 };
	 public boolean stopCellEditing() {
		 return true;
	 }
	 
	 public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		 return (Component) ((ObjectAdapter) value).getUIComponent().getPhysicalComponent();
		 //return (Component) cb.getPhysicalComponent();
	 }

}
