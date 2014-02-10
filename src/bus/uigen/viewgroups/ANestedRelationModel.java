package bus.uigen.viewgroups;

import java.beans.PropertyDescriptor;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.uiFrame;
import bus.uigen.introspect.PropertyDescriptorProxy;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.sadapters.ConcreteType;
import bus.uigen.sadapters.ConcreteTypeRegistry;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;

public class ANestedRelationModel {
	VectorStructure relation;
	Hashtable<Object, ANestedTupleListModel> contents;
	Hashtable<Object, ANestedTupleModel> tupleModelCache = new Hashtable();
	Hashtable<Object, ANestedTupleListModel> tupleListModelCache = new Hashtable();
	Hashtable<Object, BeanToRecord> recordCache = new Hashtable();
	String keyProperty;
	uiFrame frame;
	ClassProxy relationClass;
	ObjectAdapter parentAdapter;
	DescriptorViewSupport descriptorViewSupport; 
	public ANestedRelationModel (VectorStructure theRelation, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		relation = theRelation;
		frame = theFrame;
		relationClass = theRelation.getTargetClass();
		parentAdapter = theParentAdapter;
		descriptorViewSupport = new DescriptorViewSupport();
	}
	
	
	protected void setKey(String theProperty) {
		keyProperty = theProperty;
		makeTable(theProperty);
	}
	
	public boolean makeTable(String theProperty) {
		keyProperty = theProperty;
		contents = new Hashtable();
		for (int i=0; i < relation.size(); i++) {
			Object element = relation.elementAt(i);
			/*
			ConcreteType concreteType= ConcreteTypeRegistry.createConcreteType(element.getClass(), element,frame);
			if (!(concreteType instanceof BeanToRecord)) {
				System.out.println("E**" + relationClass + " does not have properties");
				return false;
			}
			BeanToRecord record = (BeanToRecord) concreteType;
			*/
			BeanToRecord record = createConcreteType(RemoteSelector.getClass(element), element,frame);
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
			ANestedTupleModel tuple = makeTupleModel(record, keyProperty, i, frame, parentAdapter);
			ANestedTupleListModel tupleList = contents.get(keyObject);
			if (tupleList == null) {
				//tupleList = new ATupleListModel(this, relation, frame, parentAdapter);
				tupleList = makeTupleListModel(keyObject, this, relation, frame, parentAdapter);
				contents.put(keyObject, tupleList);
			}
			tupleList.addElementBasic(tuple);
		}
			 
		return true;
	}
	
	public Object get (Object key) {
		return contents.get(key);
	}
	public Object put (Object key, ANestedTupleListModel tupleList) {
		if (contents.get(key) == tupleList)
			return null;
		for (int i = 0; i < tupleList.size(); i++) {
			ANestedTupleModel tupleModel = tupleList.elementAt(i);
			int originalPos = tupleModel.getIndex();
			RecordStructure record = tupleModel.getRecord();
			relation.insertElementAt(record.getTargetObject(), originalPos, null);
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
		ANestedTupleListModel tuples = contents.get(key);
		for (int i = tuples.size() - 1; i >= 0; i--) {
			ANestedTupleModel tuple = tuples.elementAt(i);
			relation.removeElement(tuple.getIndex(), relation.elementAt(tuple.getIndex()), null);
		}
		return contents.remove(key);
	}
	
	protected void addProperties (Object theTargetObject, PropertyDescriptorProxy[] theProperties) {
		
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
	protected ANestedTupleModel makeTupleModel (RecordStructure theRecordStructure, String keyProperty, int theIndex, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		Object targetObject = theRecordStructure.getTargetObject();
		//boolean createNew = false;
		ANestedTupleModel tupleModel = null;
		if (targetObject != null)
			//createNew = true;
			tupleModel = tupleModelCache.get(targetObject);
		if (tupleModel == null) {
			tupleModel =	 new ANestedTupleModel(theRecordStructure, keyProperty, theIndex, theFrame, theParentAdapter);			
			if (targetObject != null)
			tupleModelCache.put(targetObject, tupleModel);
		} else
			tupleModel.init(theRecordStructure, keyProperty, theIndex, theFrame, theParentAdapter);
		// no point removing the element from the cache perhaps
		return tupleModel;
	}
	protected ANestedTupleListModel makeTupleListModel (Object key, ANestedRelationModel theNestedRelation, VectorStructure theRelation, uiFrame theFrame, ObjectAdapter theParentAdapter) {
		ANestedTupleListModel tupleListModel = tupleListModelCache.get(key);
		if (tupleListModel == null) {
			tupleListModel = new ANestedTupleListModel(theNestedRelation, theRelation, theFrame, theParentAdapter);
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
	
			ConcreteType concreteType= ConcreteTypeRegistry.createConcreteType(RemoteSelector.getClass(element), element,frame);
			if (!(concreteType instanceof BeanToRecord)) {
				//System.out.println("E**" + relationClass + " does not have properties");
				return null;
			}
			retVal = (BeanToRecord) concreteType;
			recordCache.put(element, retVal);
		} 
		return retVal;
	
	}
	
}
