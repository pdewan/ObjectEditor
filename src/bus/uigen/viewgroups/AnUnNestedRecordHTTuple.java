package bus.uigen.viewgroups;

import java.beans.MethodDescriptor;
import java.util.Vector;

import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.sadapters.HashtableStructure;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;
import bus.uigen.undo.CommandListener;

public class AnUnNestedRecordHTTuple {
	HashtableStructure namedHashtable;
	RecordStructure record;
	uiFrame frame;
	//Vector filterIn = new Vector(); 
	//int index;
	ObjectAdapter parentAdapter;
	DescriptorViewSupport descriptorViewSupport; 
	Object key, value;
	//Object value;
	//String nameProperty;
	//Object nameValue;
	ClassDescriptorInterface cd;
	ClassProxy hashtableClass;
	String keyLabel;
	String valueLabel;
	Vector<String> componentNames;	
	Vector<String> allNames;
	String[] allNamesArray;
	public AnUnNestedRecordHTTuple (RecordStructure theRecordStructure, HashtableStructure theHashtableStructure, Object theKey,  uiFrame theFrame, ObjectAdapter theParentAdapter) {
		descriptorViewSupport = new DescriptorViewSupport();
		ClassProxy c = theHashtableStructure.getTargetClass();
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
		// makes it look like a property descriptor!
		//addMethods (theRecordStructure.getTargetObject(), cd.getMethodDescriptors());
		
		init(theRecordStructure, theHashtableStructure, theKey, theFrame, theParentAdapter);
		/*
		tuple = theRecordStructure;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		filterIn = (Vector) tuple.componentNames().clone();
		index = theIndex;
		filterIn(keyProperty, false);
		*/
	}
	protected void init (RecordStructure theRecordStructure, HashtableStructure theHashtableStructure, Object theKey,  uiFrame theFrame, ObjectAdapter theParentAdapter) {
		namedHashtable = theHashtableStructure;
		hashtableClass = RemoteSelector.getClass(namedHashtable);
		cd = ClassDescriptorCache.getClassDescriptor(hashtableClass);
		key = theKey;
		value = namedHashtable.get(key);
		keyLabel = (String) cd.getPropertyAttribute(AttributeNames.ANY_KEY, AttributeNames.LABEL);
		if (keyLabel == null)
			keyLabel = AttributeNames.ANY_KEY;
		valueLabel = (String) cd.getPropertyAttribute(AttributeNames.ANY_VALUE, AttributeNames.LABEL);
		if (valueLabel == null)
			valueLabel = AttributeNames.ANY_VALUE;
		record = theRecordStructure;
		frame = theFrame;
		parentAdapter = theParentAdapter;
		componentNames = theRecordStructure.componentNames();
		/*
		allNames = (Vector) componentNames.clone();
		allNames.add(keyLabel);
		allNames.add(valueLabel);
		allNamesArray = new String[allNames.size()];
		allNames.copyInto(allNamesArray);
		*/
		allNames = getAllNames(cd, componentNames, keyLabel, valueLabel);
		allNamesArray = new String[allNames.size()];
		allNames.copyInto(allNamesArray);
		//filterIn.clear(); 
		//filterIn = (Vector) namedHashtable.componentNames().clone();
		//index = theRow;
		//column = theColumn;
		//nameProperty = theNameProperty;
		
	}
	protected Vector getAllNames(ClassDescriptorInterface theCD, Vector theComponentNames, String theKeyLabel, String theValueLabel) {
		Vector retVal =  new Vector();
		for (int i = 0; i < componentNames.size(); i++) {
			String compName = componentNames.elementAt(i);
			boolean visible = (Boolean) theCD.getPropertyAttribute(compName, AttributeNames.VISIBLE);
			if (visible)
				retVal.add(compName);	
		}
		retVal.add(theKeyLabel);
		retVal.add(theValueLabel);
		return retVal;
	}
	protected Object getKey() {
		return key;
	}
	protected Object getValue() {
		return value;
	}
	protected HashtableStructure getHashtable() {
		return namedHashtable;
	}
	
	protected RecordStructure getRecord() {
		return record;
	}
	
	
	public String getVirtualClass() {
		return namedHashtable.getTargetClass().getName();
	}
	
	public String[] getDynamicProperties() {
		
		return allNamesArray;
		/*
		String[] retVal = new String[filterIn.size()];
		filterIn.copyInto(retVal);
		return retVal;
		*/
	}
	/*
	protected String[] add (String firstElement, Vector<String> list) {
		String[] retVal = new String[list.size() + 1];
		retVal[0] = firstElement;
		for (int i = 1; i < retVal.length; i++) {
			retVal[i] = list.get(i-1);
		}
		return retVal;
	}
	*/
	public void setDynamicProperty(String name, Object newVal) {
		if (isValueLabel(name)) {
			value = newVal;
			 namedHashtable.put(key, newVal);
		} else if (isKeyLabel(name)) {
			Object val = namedHashtable.get(key);
			namedHashtable.remove(key, null);
			key = newVal;
			namedHashtable.put(key, val);
		} else
			 record.set(name, newVal);
	}
	boolean isKeyLabel(String name) {
		return keyLabel.toLowerCase().equals(name.toLowerCase());
	}
	boolean isValueLabel(String name) {
		return valueLabel.toLowerCase().equals(name.toLowerCase());
	}
	public Object getDynamicProperty(String name) {
		if (isValueLabel(name)) {
			return namedHashtable.get(key);
		} else if (isKeyLabel(name)) {
			return key;
		} else
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
