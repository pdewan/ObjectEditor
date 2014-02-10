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
import bus.uigen.undo.CommandListener;

public class ANestedTupleModel {
	RecordStructure tuple;
	uiFrame frame;
	Vector filterIn = new Vector(); 
	int index;
	ObjectAdapter parentAdapter;
	DescriptorViewSupport descriptorViewSupport; 
	public ANestedTupleModel (RecordStructure theRecordStructure, String keyProperty, int theIndex, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		descriptorViewSupport = new DescriptorViewSupport();
		ClassProxy c = theRecordStructure.getTargetClass();
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
		addMethods (theRecordStructure.getTargetObject(), cd.getMethodDescriptors());
		
		init(theRecordStructure, keyProperty, theIndex, theFrame, theParentAdapter);
		/*
		tuple = theRecordStructure;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		filterIn = (Vector) tuple.componentNames().clone();
		index = theIndex;
		filterIn(keyProperty, false);
		*/
	}
	protected void init (RecordStructure theRecordStructure, String keyProperty, int theIndex, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		tuple = theRecordStructure;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		filterIn.clear(); 
		filterIn = (Vector) tuple.componentNames().clone();
		index = theIndex;
		filterIn(keyProperty, false);
		
	}
	protected int getIndex() {
		return index;
	}
	protected RecordStructure getRecord() {
		return tuple;
	}
	protected void filterIn(String propertyName, boolean filter) {
		if (!tuple.componentNames().contains(propertyName))
			return;
		if (filterIn.contains(propertyName)) {
			if (!filter) 
				filterIn.remove(propertyName);
		} else {
			if (filter)
				filterIn.add(propertyName);
		}
	}
	
	public String getVirtualClass() {
		return tuple.getTargetClass().getName();
	}
	
	public String[] getDynamicProperties() {
		Vector allProperties = tuple.componentNames();
		String[] retVal = new String[filterIn.size()];
		filterIn.copyInto(retVal);
		return retVal;
	}
	public void setDynamicProperty(String name, Object newVal) {
		//tuple.set(name, newVal, parentAdapter);
		tuple.set(name, newVal);
	}
	public Object getDynamicProperty(String name) {
		return tuple.get(name);
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
