package bus.uigen.sadapters;
import java.util.*;import java.io.Serializable;
import java.lang.reflect.*;import java.beans.*;
import bus.uigen.editors.*;import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.misc.OEMisc;
import bus.uigen.controller.MethodInvocationManager;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.HASHTABLE_PATTERN)
public class RowToRecord /*implements RecordStructure*/ implements Serializable {
	GenericTableToVectorStructure table;
	int rowIndex;
	Object row;	
	public RowToRecord (GenericTableToVectorStructure theTable, int theRowIndex) {		table = theTable;
		rowIndex = theRowIndex;
	}	
	public Enumeration keys() {
		return table.getColumnNames().elements();
	}
	public Object get (String key) {
		return table.getValueAt (rowIndex, key);		
	}
	public Object put (String key, Object value) {
		//System.out.println ("Put: " + key);
		Object retVal = get (key);
		table.setValueAt (value, rowIndex, key);
		return retVal;
		
	}
	public boolean equals (RowToRecord otherObject) {
		GenericTableToVectorStructure table2 = otherObject.table;
		if (table.size() != table2.size()) return false;
		if (table.getColumnCount() != table2.getColumnCount()) return false;
		for (int i = 0; i < table.size(); i++) {			
			for (int j = 0; j < table.getRowCount(); j++)
				if (!OEMisc.equals(table.getValueAt(i,j), table2.getValueAt(i,j))) return false;
				
		}
		return true;		
		
	}
	/*
	public Vector componentNames() {
		//return GenericHashtableToHashtableStructure.toVector(componentTable.keys());
		return table.getColumnNames();	}
	public Object get (String componentName) {
		return table.getValueAt (rowIndex, componentName);
	}	public boolean isReadOnly (String componentName) {
		return table.isCellEditable (rowIndex, componentName);
	}
	public Object set (String componentName, Object value, CommandListener commandListener) {
		table.setValueAt(value, rowIndex, componentName,  commandListener);
		return new Integer(0);
	}	
	public Object set (String componentName, Object value) {
		table.setValueAt (value, rowIndex, componentName);
		return new Integer(0);
		
	}
	public void setTarget(Object theTargetObject) {
		row = theTargetObject;
	}
		public boolean preRead (String componentName) {
		return true;		
	}	public boolean preWrite (String componentName) {
		return false;
	}
	public Class componentType(String componentName) {
		return Object.class;
	}
	*/
	
}