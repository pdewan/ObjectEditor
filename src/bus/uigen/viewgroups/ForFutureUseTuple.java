package bus.uigen.viewgroups;

import java.beans.MethodDescriptor;
import java.util.Vector;

import bus.uigen.uiFrame;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;
import bus.uigen.undo.CommandListener;

public class ForFutureUseTuple {
	RecordStructure record;
	VectorStructure rowObject;
	uiFrame frame;
	Vector filterIn = new Vector(); 
	int row, column;
	ObjectAdapter parentAdapter;
	DescriptorViewSupport descriptorViewSupport; 
	String keyProperty;
	Object keyValue;
	public ForFutureUseTuple (RecordStructure theRecordStructure, String theKeyProperty, Object theKeyValue, int theRow, int theColumn, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		descriptorViewSupport = new DescriptorViewSupport();
		ClassProxy c = theRecordStructure.getTargetClass();
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
		addMethods (theRecordStructure.getTargetObject(), cd.getMethodDescriptors());
		
		init(theRecordStructure, theKeyProperty, theKeyValue, theRow, theColumn, theFrame, theParentAdapter);
		/*
		tuple = theRecordStructure;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		filterIn = (Vector) tuple.componentNames().clone();
		index = theIndex;
		filterIn(keyProperty, false);
		*/
	}
	protected void init (RecordStructure theRecordStructure, String theKeyProperty, Object theKeyValue, int theRow, int theColumn, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		record = theRecordStructure;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		filterIn.clear(); 
		filterIn = (Vector) record.componentNames().clone();
		row = theRow;
		column = theColumn;
		keyProperty = theKeyProperty;
		
	}
	protected int getRow() {
		return row;
	}
	protected int getColumn() {
		return column;
	}
	protected RecordStructure getRecord() {
		return record;
	}
	
	
	public String getVirtualClass() {
		return record.getTargetClass().getName();
	}
	
	public String[] getDynamicProperties() {
		Vector<String> allProperties = record.componentNames();
		return add(keyProperty, allProperties);
		/*
		String[] retVal = new String[filterIn.size()];
		filterIn.copyInto(retVal);
		return retVal;
		*/
	}
	protected String[] add (String firstElement, Vector<String> list) {
		String[] retVal = new String[list.size() + 1];
		retVal[0] = firstElement;
		for (int i = 1; i < retVal.length; i++) {
			retVal[i] = list.get(i-1);
		}
		return retVal;
	}
	public void setDynamicProperty(String name, Object newVal) {
		//tuple.set(name, newVal, parentAdapter);
		if (name.toLowerCase().equals(keyProperty.toLowerCase()))
			return;
		record.set(name, newVal);
	}
	public Object getDynamicProperty(String name) {
		if (name.toLowerCase().equals(keyProperty.toLowerCase()))
			return keyValue;
		return record.get(name);
	}
	protected void addMethods (Object theTargetObject, MethodDescriptorProxy[] theMethods) {
		descriptorViewSupport.addMethods(theTargetObject, theMethods);
		/*
		for (int i = 0; i < theMethods.size(); i++) {
			virtualMethods.addElement(VirtualMethodDescriptor.getVirtualMethod(theMethods.elementAt(i)).moveFromObject(theTargetObject));
		}
		*/
		
		
	}
	public Vector<MethodProxy> getVirtualMethods() {
		return descriptorViewSupport.getVirtualMethods();
		/*
		return virtualMethods;
		*/
	}
}
