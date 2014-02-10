package bus.uigen.sadapters;
import java.util.*;
import java.lang.reflect.*;

import util.trace.Tracer;

import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

//public class uiVectorAdapter extends uiContainerAdapterpublic class GenericTableToVectorStructure extends AbstractDynamicStructure  implements  VectorStructure  {
	  transient MethodProxy getColumnCountMethod = null;
	  transient MethodProxy getRowCountMethod = null; 
	  transient MethodProxy getColumnNameMethod = null;
	  ///transient Method getIndexMethod = null;
	  transient MethodProxy getValueAtMethod = null;
	  transient MethodProxy setValueAtMethod = null;
	  transient MethodProxy isCellEditableMethod = null;
	  transient Vector nullVector = new Vector();
	  //transient Method getUserObjectMethod = null;
	  //transient Method setUserObjectMethod = null;
	  String[] excludeProperties = {"columnCount", "rowCount" , "columnNames"};
		 String[] excludeProperties() {
			//String[] retVal = {};
			return excludeProperties;
		}
		 
		 public boolean excluded (String property) {
		 	Vector excludedPropertiesVector = uiGenerator.arrayToVector(excludeProperties());
		 	return(excludedPropertiesVector.contains(property));
		 }
		
		 boolean excludeFields () {
		 	return true;
		 }
		 // as the object implements the table intarface, its properties should probbaly be ignored. Make this an attribute?
		 public Vector componentNames() {
				//return GenericHashtableToHashtableStructure.toVector(componentTable.keys());
				return nullVector;
			}
	 
   public void setMethods(ClassProxy c) {
   		  super.setMethods(c);
		  getColumnCountMethod = IntrospectUtility.getTableGetColumnCountMethod(c);
		  getRowCountMethod = IntrospectUtility.getTableGetRowCountMethod(c);
		  getColumnNameMethod = IntrospectUtility.getTableGetColumnNameMethod(c);
		  getValueAtMethod = IntrospectUtility.getTableGetValueAtMethod(c);
		  setValueAtMethod = IntrospectUtility.getTableSetValueAtMethod(c);
		  isCellEditableMethod = IntrospectUtility.getTableIsCellEditableMethod(c);
		  //getUserObjectMethod = uiBean.getTreeGetUserObjectMethod(c);
		  //setUserObjectMethod = uiBean.getTreeSetUserObjectMethod(c);
   }
		  
   public boolean isGenericTable() {
   	 return getColumnCountMethod != null &&
	  getRowCountMethod != null &&	  
	  getColumnNameMethod != null &&
	  getValueAtMethod != null;
   }	
	public GenericTableToVectorStructure (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
	
	public void setTarget(ClassProxy newTargetClass, Object theTargetObject) {
		super.setTarget(newTargetClass, theTargetObject);
		if (this.isGenericTable())
		 set();
		
	}
	Vector rows;
	Vector colNames;
	public void set() {
		rows = new Vector();
		for (int i = 0; i < size(); i++) {
			rows.addElement(new RowToRecord(this, i));
			
		}
		colNames = new Vector();
		for (int i = 0; i < getColumnCount(); i++) {
			colNames.addElement(getColumnName(i));
		}
		
	}
	
		Object[] nullArgs = {};
	public int getColumnCount() {		try {
						//Object retVal = getColumnCountMethod.invoke(targetObject, nullArgs);
			Object retVal = MethodInvocationManager.invokeMethod(targetObject, getColumnCountMethod, nullArgs);
			return ((Integer) retVal).intValue();
		} catch (Exception e) {			return -1;
		}	}
	void resetIfRowsChanged () {
		try {
		//Object retVal = getRowCountMethod.invoke(targetObject, nullArgs);
		Object retVal = MethodInvocationManager.invokeMethod(targetObject, getRowCountMethod, nullArgs);
		int intRetVal =  ((Integer) retVal).intValue();
		if (intRetVal != rows.size()) {
			set();
		}
		} catch (Exception e) {
			
		}
	
	}
	public int getRowCount() {
		try {			
			//Object retVal = getRowCountMethod.invoke(targetObject, nullArgs);
			Object retVal = MethodInvocationManager.invokeMethod(targetObject, getRowCountMethod, nullArgs);
			return ((Integer) retVal).intValue();
		} catch (Exception e) {
			return -1;
		}
	}	public int size() {
		return getRowCount();
	}
	
	public Object getValueAt(int rowIndex, int colIndex) {
		try {			
			Object[] args = {new Integer(rowIndex), new Integer(colIndex)};
			//return getValueAtMethod.invoke(targetObject, args);
			return MethodInvocationManager.invokeMethod(targetObject, getValueAtMethod, args);
		} catch (Exception e) {
			return null;
		}		
	}
	public Object getValueAt(int rowIndex, String colName) {
		try {			
			return getValueAt (rowIndex, colNames.indexOf(colName));
		} catch (Exception e) {
			return null;
		}		
	}
	public Object getColumnName(int colIndex) {
		try {			
			Object[] args = { new Integer(colIndex)};
			//return getColumnNameMethod.invoke(targetObject, args);
			return MethodInvocationManager.invokeMethod(targetObject,getColumnNameMethod, args);
		} catch (Exception e) {
			return null;
		}		
	}
	public Vector getColumnNames() {
		return colNames;
	}
	public boolean isCellEditable (int rowIndex, int colIndex) {
		try {			
			Object[] args = {new Integer(rowIndex), new Integer(colIndex)};
			//return ((Boolean) isCellEditableMethod.invoke(targetObject, args)).booleanValue();
			return ((Boolean) MethodInvocationManager.invokeMethod(targetObject, isCellEditableMethod, args)).booleanValue();
		} catch (Exception e) {
			return false;
		}		
	}
	public boolean isEditable (int rowIndex) {
		return isCellEditable (rowIndex, 0);
	}
	public boolean isCellEditable (int rowIndex, String colName) {
		try {			
			return isCellEditable(rowIndex, colNames.indexOf(colName));
		} catch (Exception e) {
			return false;
		}		
	}
	public void setValueAt(Object newVal, int rowIndex, int colIndex) {
		try {			
			Object[] args = {newVal, new Integer(rowIndex), new Integer(colIndex)};
		     //setValueAtMethod.invoke(targetObject, args);
		     MethodInvocationManager.invokeMethod(targetObject,setValueAtMethod, args);
		} catch (Exception e) {
			return ;
		}		
	}
	public void setValueAt(Object newVal, int rowIndex, int colIndex, CommandListener commandListener) {
		try {			
			Object[] args = {newVal, new Integer(rowIndex), new Integer(colIndex)};
			 Tracer.warning("GenericTableToVectorStructure-Should get duplicate invocations");
		     //setValueAtMethod.invoke(targetObject, args);
			 MethodInvocationManager.invokeMethod(targetObject, setValueAtMethod, args);
		     frame.getUndoer().execute (
					new SetGetFirstCommand(commandListener, 
						setValueAtMethod, 
						targetObject,
						args,											   
						getValueAtMethod//,
						//elementAtMethodParams
						));
		} catch (Exception e) {
			return ;
		}		
	}
	public void setValueAt(Object newVal, int rowIndex, String colName) {
		try {			
			setValueAt (newVal, rowIndex, colNames.indexOf(colName));
		} catch (Exception e) {
			return ;
		}		
	}
	public void setValueAt(Object newVal, int rowIndex, String colName, CommandListener commandListener) {
		try {			
			setValueAt (newVal, rowIndex, colNames.indexOf(colName), commandListener);
		} catch (Exception e) {
			return ;
		}		
	}
	
	public Object elementAt(int i) {
		resetIfRowsChanged ();
		return rows.elementAt(i);
	}
		
	public void setElementAt(Object newVal, int pos) {
		rows.setElementAt(newVal, pos);
	}
		
	public ClassProxy addableElementType () {
		return null;	}
	
	public boolean hasDeleteChildMethod() {
		return false;
	}
	public boolean hasSetChildMethod() {
		return true;
	}
	public boolean hasInsertChildMethod() {
		return true;
	}
	public boolean hasAddChildMethod() {
		return true;
	}
	
	public void setElementAt(Object element, int pos, CommandListener commandListener) {
		setElementAt(element, pos);
		
	}
	public void removeElementAt(int index, CommandListener commandListener) {
		
	}
	void addElementAt (Object element, int index, CommandListener commandListener) {
		
	}
	public void removeElement(int index, Object element, CommandListener commandListener) {		removeElementAt(index, commandListener);
	}	public void addElement(Object element, CommandListener commandListener){
		addElementAt(element, size(), commandListener);	}
	/*
	public Vector componentNames() {
		return new Vector();
	}
	public Object get (String componentName) {
		return null;
	}
	public Class componentType (String componentName) {return null;}
		
	public boolean isReadOnly (String componentName) {
		return false;		
	}
	public boolean preRead (String componentName) {
		return true;
	}
	public boolean preWrite (String componentName) {
		return true;		
	}
	public Object set (String componentName, Object value, CommandListener commandListener) {
		return null;		
	}
	public Object set (String componentName, Object value) {
		return null;		
	}
	*/
	public GenericTableToVectorStructure () {
	}
	/*
	public Object getUserObject () {
		return null;
	}
	public void setUserObject (Object newVal) {
		
	}
	public boolean hasUserObject() {
		return false;
	}
	public boolean hasEditableUserObject() {
		return false;
	}
	*/
	public Object getUserObject (Object o) {
		try {
		//getUserObjectMethod = uiBean.getTreeGetUserObjectMethod(o.getClass());
		if (getUserObjectMethod == null) return null;
		Object[] nullObjectArgs = {};
		//return getUserObjectMethod.invoke(o, nullObjectArgs);
		return MethodInvocationManager.invokeMethod(o, getUserObjectMethod, nullObjectArgs);
		} catch (Exception e) {
			return null;
		}
		
	}
	public Object getUserObject () {
		return getUserObject(targetObject);
		
	}
	public void setUserObject (Object o, Object newVal) {
		try {

		Class[] userObjectArgs = {Object.class};
		//setUserObjectMethod = uiBean.getTreeSetUserObjectMethod(o.getClass());		
		if (setUserObjectMethod == null) return ;
		Object[] objectArgs = {newVal};
		//setUserObjectMethod.invoke(o, objectArgs);
		MethodInvocationManager.invokeMethod(o, setUserObjectMethod, objectArgs);
		} catch (Exception e) {
			return ;
		}
		
	}
	public void setUserObject (Object newVal) {
		setUserObject (targetObject, newVal);
		
	}
	public boolean hasUserObject() {
		return getUserObjectMethod != null;
	}
	public boolean hasEditableUserObject() {
		return setUserObjectMethod != null;
	}
	public void insertElementAt (Object element, int pos, CommandListener commandListener) {
		addElement(element, commandListener);
		
	}
	public boolean validateRemoveElement(Object element) {
		return true;
	}
	public boolean validateRemoveElementAt(int index) {
		return true;
	}
	public boolean validateElementAt(int index) {
		return true;
	}
	public boolean validateAddElement(Object element) {
		return true;
	}
	public boolean validateSetElementAt(Object element, int pos) {
		return true;
	}
	public boolean validateInsertElementAt (Object element, int pos) {
		return true;
	}

	@Override
	public boolean hasValidateInsertElementAt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasValidateAddElement() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preAddElement() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preElementAt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preInsertElementAt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preRemoveElement() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preRemoveElementAt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preSetElementAt() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
