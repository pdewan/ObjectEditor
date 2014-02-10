package bus.uigen.viewgroups;

import java.beans.PropertyDescriptor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.uiFrame;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteTypeRegistry;
import bus.uigen.sadapters.HashtableStructure;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;

public class ForFutureUseRelation {
	HashtableStructure hashtable;
	RecordStructure record;
	Hashtable<Object, AnUnNestedRecordHTTupleList> contents;
	Hashtable<Object, ForFutureUseTuple> tupleModelCache = new Hashtable();
	Hashtable<Object, AnUnNestedRecordHTTupleList> tupleListModelCache = new Hashtable();
	Hashtable<Object, BeanToRecord> recordCache = new Hashtable();
	String keyProperty;
	uiFrame frame;
	ClassProxy relationClass;
	ObjectAdapter parentAdapter;
	DescriptorViewSupport descriptorViewSupport; 
	public ForFutureUseRelation (RecordStructure theRecordStructure, HashtableStructure theHashtable, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		hashtable = theHashtable;
		frame = theFrame;
		relationClass = theHashtable.getTargetClass();
		parentAdapter = theParentAdapter;
		descriptorViewSupport = new DescriptorViewSupport();
	}
	/*
	
	protected void setKey(String theProperty) {
		keyProperty = theProperty;
		makeTable();
	}
	
	public boolean makeTable() {
		
		Vector keys = hashtable.keys();
		
		for (int i=0; i < hashtable.size(); i++) {
			//Object element = hashtable.elementAt(i);
			Object element = keys.elementAt(i);
			
			BeanToRecord record = createConcreteType(ClassSelector.getClass(element), element,frame);
			if (record == null) {
				System.out.println("E**" + relationClass + " does not have properties");
				return false;
			}
			Vector componentNames =  record.componentNames();
			if (componentNames.size() <= 1) {
				System.out.println("E**" + relationClass + " has only one property");
				return false;
			}
			if (!componentNames.contains(keyProperty)) {
				System.out.println("E**" + relationClass + " does not have desired key property: " + keyProperty);
				return false;
			}
			//Object keyObject = record.get(keyProperty);
			Object keyObject = record.get(keyProperty);
			//ATupleModel tuple = new ATupleModel(record, keyProperty, i, frame, parentAdapter);
			AnUnNestedRecordHTTuple tuple = makeTupleModel(record, keyProperty, i, frame, parentAdapter);
			AnUnNestedRecordHTTupleList tupleList = contents.get(keyObject);
			if (tupleList == null) {
				//tupleList = new ATupleListModel(this, relation, frame, parentAdapter);
				tupleList = makeTupleList(keyObject, this, hashtable, frame, parentAdapter);
				contents.put(keyObject, tupleList);
			}
			tupleList.addElementBasic(tuple);
		}
			 
		return true;
	}
	
	public Object get (Object key) {
		return contents.get(key);
	}
	public Object put (Object key, AnUnNestedRecordHTTupleList tupleList) {
		for (int i = 0; i < tupleList.size(); i++) {
			//ForFutureUseTuple tupleModel = tupleList.elementAt(i);
			AnUnNestedRecordHTTuple tupleModel = tupleList.elementAt(i);
			//int originalPos = tupleModel.getIndex();
			RecordStructure record = tupleModel.getRecord();
			//hashtable.insertElementAt(record.getTargetObject(), originalPos, null);
			hashtable.put(key, record.getTargetObject());
		}
		
		return contents.put(key, tupleList);
	}
	
	public Enumeration keys() {
		return contents.keys();
	}
	public Enumeration elements() {
		return contents.elements();
	}
	public int size() {
		return contents.size();
	}
	
	public Object remove (Object key) {
		
		AnUnNestedRecordHTTupleList tuples = contents.get(key);
		for (int i = tuples.size() - 1; i >= 0; i--) {
			ForFutureUseTuple tuple = tuples.elementAt(i);
			hashtable.removeElement(tuple.getIndex(), hashtable.elementAt(tuple.getIndex()), null);
		}
		
		return contents.remove(key);
	}
	
	protected void addProperties (Object theTargetObject, PropertyDescriptor[] theProperties) {
		
		descriptorViewSupport.addProperties(theTargetObject, theProperties);
	}
	public String[] getDynamicProperties() {
		return descriptorViewSupport.getDynamicProperties();
		//return propertyNames;
	}
	public void setDynamicProperty(String name, Object newVal) {
		descriptorViewSupport.setDynamicProperty(name, newVal);
	}
	public Object getDynamicProperty(String name) {
		return descriptorViewSupport.getDynamicProperty(name);
	}
	public  void isRelation() {
			
	}
	protected ForFutureUseTuple makeTupleModel (RecordStructure theRecordStructure, String keyProperty, int theIndex, uiFrame theFrame, uiObjectAdapter theParentAdapter) {
		Object targetObject = theRecordStructure.getTargetObject();
		//boolean createNew = false;
		ForFutureUseTuple tupleModel = null;
		if (targetObject != null)
			//createNew = true;
			tupleModel = tupleModelCache.get(targetObject);
		if (tupleModel == null) {
			tupleModel =	 new ForFutureUseTuple(theRecordStructure, keyProperty, theIndex, theFrame, theParentAdapter);			
			if (targetObject != null)
			tupleModelCache.put(targetObject, tupleModel);
		} else
			tupleModel.init(theRecordStructure, keyProperty, theIndex, theFrame, theParentAdapter);
		// no point removing the element from the cache perhaps
		return tupleModel;
	}
	protected AnUnNestedRecordHTTupleList makeTupleList (Object key, ForFutureUseRelation theNestedRelation, HashtableStructure theRelation, uiFrame theFrame, uiObjectAdapter theParentAdapter) {
		AnUnNestedRecordHTTupleList tupleListModel = tupleListModelCache.get(key);
		if (tupleListModel == null) {
			tupleListModel = new AnUnNestedRecordHTTupleList(record, theRelation, theFrame, theParentAdapter);
			tupleListModelCache.put(key, tupleListModel);
		}
		else
			tupleListModel.init(theNestedRelation, theRelation, theFrame, theParentAdapter);
		return tupleListModel;
	}
	public void refreshView() {
		contents.clear();
		makeTable(keyProperty);
	}
	public BeanToRecord createConcreteType (ClassProxy c, Object element, uiFrame frame ) {
		if (element == null)
			return null;
		BeanToRecord retVal = recordCache.get(element);
		if (retVal == null) {	
	
			ConcreteType concreteType= ConcreteTypeRegistry.createConcreteType(ClassSelector.getClass(element), element,frame);
			if (!(concreteType instanceof BeanToRecord)) {
				//System.out.println("E**" + relationClass + " does not have properties");
				return null;
			}
			retVal = (BeanToRecord) concreteType;
			recordCache.put(element, retVal);
		} 
		return retVal;
	
	}
	*/
	
}
