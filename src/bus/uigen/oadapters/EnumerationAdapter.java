// We can derive primitive adaptors by extending
// This class
package bus.uigen.oadapters;
import java.lang.reflect.*;
import java.rmi.RemoteException;
import java.util.*;import java.beans.*;

import util.models.RemotePropertyChangeListener;
import util.trace.Tracer;
import bus.uigen.*;import bus.uigen.ars.*;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.*;import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.sadapters.ConcreteEnumeration;import bus.uigen.sadapters.ConcreteType;
import java.util.List;
public class EnumerationAdapter extends PrimitiveAdapter  implements PrimitiveAdapterInterface
//implements ObjectValueChangedListener, RemotePropertyChangeListener 
{
	public EnumerationAdapter() throws RemoteException {
		
	}	/*
	transient Method readMethod ;	transient Method writeMethod ; 
	//transient boolean vectorChoices = true;	transient Method choicesMethod;
	transient Field choicesField;	transient Class valueClass;	transient Class choicesClass;	transient Method choiceAtMethod;	transient Method choicesSizeMethod;
		public Method getReadMethod() {
		return readMethod;
	}
	public void setReadMethod(Method newVal) {
		readMethod = newVal;
	}
		public Method getWriteMethod() {
		return writeMethod;
	}	public void setWriteMethod(Method newVal) {
		writeMethod = newVal;
	}
		public Method getChoicesMethod() {
		return choicesMethod;
	}	public void setChoicesMethod(Method newVal) {
		choicesMethod = newVal;
	}	public Method getChoiceAtMethod() {
		return choiceAtMethod;
	}	public void setChoiceAtMethod(Method newVal) {
		choiceAtMethod = newVal;
	}	public Method getChoicesSizeMethod() {
		return choicesSizeMethod;
	}	public void setChoicesSizeMethod(Method newVal) {
		choicesSizeMethod = newVal;
	}
	public Field getChoicesField() {
		return choicesField;
	}	public void setChoicesField(Field newVal) {
		choicesField = newVal;
	}	public void setValueClass(Class newVal) {
		valueClass = newVal;
	}	public void setChoicesClass(Class newVal) {
		choicesClass = newVal;
	}	*/
	/*	public String getChildSelectableIndex(Selectable child) {
    return "";
	}	*/
	Object choiceValue = null;
	
	public  void setChoiceValue(Object newVal) {
//		if (newVal == null) {
//			System.out.println("newVal is null");
//		}
		choiceValue = newVal;
	}
	public void setChoiceInfo() {
		/*
		if (isEnum())
			return;
			*/
		//Object curChoiceValue = getConcreteEnumeration().getValue();
		//setChoiceValue(curChoiceValue);
		setChoiceValue();
		setChoices();
		
	}
	public void setChoiceValue() {
		setChoiceValue(getConcreteEnumeration().getValue());
	}
	public void resetChoiceInfo() {
		setChoiceValue();
		if (isEnum())
			return;
		setChoices();
	}
	public Object getChoiceValue() {
		if (choiceValue == null)
			return getValue();
		else
			return choiceValue;
	}
	/*
	int choicesSize;
	public int getChoiceSize() {
		if (isEnum())
			return getConcreteEnumeration().choicesSize();
		else
			return choicesSize;
	}
	public void setChoicesSize(int newSize) {
		choicesSize = newSize;
	}
	*/
	List choices;
	public void setChoices(List newList) {
		choices = newList;
	}
	public void setChoices(Object loggableOrReal) {
		try {
		if (loggableOrReal instanceof List)
			setChoices((List) loggableOrReal);
		else if (loggableOrReal instanceof ACompositeLoggable) {
			setChoices ((List) ((ACompositeLoggable)loggableOrReal).getRealObject());
			
		} else {
			Tracer.error("choices notification is not a list:" + loggableOrReal);
		}
		} catch (Exception e) {
			Tracer.error("choices notification is not a list: " + ((ACompositeLoggable)loggableOrReal).getRealObject());
			e.printStackTrace();
		}
		
		
			
	}
	public void setChoices() {
		//if (!isEnum())
			choices = getConcreteEnumeration().getChoices();
		
	}
	public List getChoices() {
		/*
		if (isEnum())
			return getConcreteEnumeration().getChoices();
		else
		*/
			return choices;
	}
	public Object choiceAt(int i) {
		/*
		if (isEnum())
			return getConcreteEnumeration().choiceAt(i);
		else
		*/
			return getChoices().get(i);
	}
	public int choicesSize() {
		/*
		if (isEnum())
			return getConcreteEnumeration().choicesSize();
		else
		*/
			return choices.size();
	}
	
	public int getIndexOfSelection() {
		/*
	  	if (isEnum())
	  		return getConcreteEnumeration().getIndexOfSelection();
	  		*/
	  	
		Object selectedItem = getChoiceValue();
		
	  		return getChoices().indexOf(selectedItem);
	  	
	}
	
	public void refreshUIComponentOfSameClass (Object newValue) {
		resetChoiceInfo();
		super.refreshUIComponentOfSameClass(newValue);
		
		  
	  }
	public void subPropertyChange(PropertyChangeEvent evt){
		//uiObjectAdapter editedObjectAdapter = getEditedObjectAdapter(evt.getPropertyName());
		if (isEnum() || evt.getSource() != getRealObject()) {
			super.subPropertyChange(evt);
			return;
		}
			
		haveReceivedNotification();
		if (evt.getPropertyName().equals("choices")) {
			setChoices(evt.getNewValue());
			// why return, let us update the UI
//			return;
		} else if (evt.getPropertyName().equals("value")){
			setChoiceValue(evt.getNewValue());
		} else {
			//super.subPropertyChange(evt);
			return;
		}
		super.refreshUIComponentOfSameClass(getChoiceValue());	
		//refreshUIComponentOfSameClass(evt.getNewValue());
		getUIFrame().validate();
	}
	public void registerAsListener (Object obj ) {
		super.registerAsListener(obj);
		// listen to dynamic enum messages
		maybeAddPropertyChangeListener(obj, this);
		maybeAddRemotePropertyChangeListener(obj, this);
	}
	
	public boolean isString() {
		if (getConcreteEnumeration() == null)
			return false;
		//Object enumValue = getConcreteEnumeration().getValue();
		Object enumValue = getValue();
		if (enumValue == null) return false;	
		return enumValue.getClass() == String.class;
		//return getConcreteEnumeration().getValue().getClass() == String.class;
	}
	
	public String getString(Object newVal) {		return getConcreteEnumeration().getValue().toString();		/*
		try {		if (isString()) {
			Object[] params = {};			//return (String) readMethod.invoke(getRealObject(), params);
			return (String) readMethod.invoke(newVal, params);		} else return "";		} catch (Exception e) {
			return "";		}		*/
	}
	public String getDirection() {
		String retVal = getDefinedDirection();
		if (retVal != null)
			return retVal;
		else 
			return AttributeNames.VERTICAL;
		//return (String) getMergedAttributeValue(AttributeNames.DIRECTION);
		//return (String) getMergedOrTempAttributeValue(AttributeNames.DIRECTION);
		
	}
	public void setUserObject(Object object)  {
	  inputText = (String) object;
	  getConcreteEnumeration().setValue(valuePart(inputText), this);
	  uiComponentValueChanged();
	  //System.out.println("New Input:" + inputText);
	}	public  Object getValueOrRealObject() {
		return getValue();	}
	public void setConcreteEnumeration (ConcreteEnumeration newVal) {
		//vectorStructure = newVal;
		setConcreteObject(newVal);	}
	public ConcreteEnumeration getConcreteEnumeration() {		//return vectorStructure;		return (ConcreteEnumeration) getConcreteObject();
	}
	
	public void setValueOfAtomicOrPrimitive(Object newValue1) {

		refreshConcreteObject(computeViewObject(newValue1));
		super.setValueOfAtomicOrPrimitive(newValue1);
	}
	
	public void setConcreteObject(ConcreteType obj) {
		if (getConcreteObject() == obj)
			return;
		super.setConcreteObject(obj);
		setChoiceInfo();
		/*
		if (!isEnum())
			choiceValue = getConcreteEnumeration().getValue();
			*/
			
	}	
	/*
	public void initBeanInfo(ViewInfo cdesc) {
		PropertyDescriptors properties[] = cdesc.getPropertyDescriptors();		for (int i = 0; i < properties.length; i++ ) {
			PropertyDescriptor p = properties[i];
			if (p.getName().equals("value")) {
				setReadMethod(p.getReadMethod());				setWriteMethod(p.getWriteMethod());
				setValueClass(p.getPropertyType());			} else if (p.getName().equals("choices")) {
				setChoicesMethod(p.getReadMethod());
				Class choicesClass = p.getPropertyType();
				vectorChoices = choicesClass.equals(Vector.class);			}
					}		
	}
	*/
	/*	public  uiObjectAdapter createAdapter (Container containW,
										   Object obj, 											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  uiObjectAdapter adaptor,
											  boolean textMode) {
	    return createEnumerationAdapter(containW, obj, obj1, parentObject, name, inputClass, propertyFlag, adaptor, textMode);
	}
	*/
	/*	public static uiEnumerationAdapter createEnumerationAdapter(ConcreteType concreteObject,												Container containW,
																Object obj, 																Object obj1, 																Object parentObject, 																String name, 																Class inputClass, 																boolean propertyFlag, 																uiObjectAdapter parentAdapter,
																boolean textMode) {		
		uiEnumerationAdapter newInstance = new uiEnumerationAdapter();		
		setAdapterAttributes(newInstance, obj, parentObject, name);	  
		newInstance.setPropertyClass(inputClass);	  
		newInstance.setPropertyName(name);
		if (propertyFlag) {
			newInstance.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
			linkPropertyToAdapter(parentObject, name, newInstance);
		}
		newInstance.setParentAdapter((uiContainerAdapter) parentAdapter);		newInstance.setUIFrame(parentAdapter.getUIFrame());	    
		newInstance.setRealObject(obj1);		newInstance.setViewObject(obj);		newInstance.setConcreteObject(concreteObject);
		newInstance.processAttributeList();	
		//newInstance.setValue(obj);		
		return newInstance;	}	*/
	/* Old one based on properties	public static uiEnumerationAdapter createEnumerationAdapter(Object obj, 																Object obj1, 																Object parentObject, 																String name, 																Class inputClass, 																boolean propertyFlag, 																uiObjectAdapter parentAdapter) {		ViewInfo cdesc = ClassDescriptorCache.getClassDescriptor(inputClass);		Class choicesClass = null;
		Class valueClass = null;		Method choicesMethod = null;
		Method readMethod = null;
		Method writeMethod = null;		Field choicesField = null;
		Method choiceAtMethod = null;
		Method choicesSizeMethod = null;		
		FeatureDescriptor features[] = cdesc.getFeatureDescriptors();		if (features.length != 2 && features.length != 3) return null;
		for (int i = 0; i < features.length; i++) {			FeatureDescriptor f = features[i];			if (f instanceof PropertyDescriptor) {
				PropertyDescriptor p = (PropertyDescriptor) f;
				if (p.getName().equals("choices")) {					choicesClass = p.getPropertyType();					if (choicesClass != Enumeration.class && !choicesClass.isArray())
						return null;
					else
						choicesMethod = p.getReadMethod();
				} else if (p.getName().equals("value")) {
					readMethod = p.getReadMethod();
					writeMethod = p.getWriteMethod();					valueClass = p.getPropertyType();
				} 
			} else if (f instanceof FieldDescriptor) {
				choicesField = ((FieldDescriptor)f).getField();				choicesClass = choicesField.getType();
				if (!choicesField.getName().equals("CHOICES") || !(choicesClass.isArray())) {					return null;								}
			}		}
		uiEnumerationAdapter newInstance = new uiEnumerationAdapter();
		newInstance.setChoicesMethod(choicesMethod);		newInstance.setChoicesField(choicesField);		newInstance.setChoicesClass(choicesClass);
		newInstance.setReadMethod(readMethod);
		newInstance.setWriteMethod(writeMethod);		newInstance.setValueClass(valueClass);
		uiGenerator.setAdapterAttributes(newInstance, obj, parentObject, name);	  
		newInstance.setPropertyClass(inputClass);	  
		newInstance.setPropertyName(name);
		if (propertyFlag) {
			newInstance.setAdapterType(uiObjectAdapter.PROPERTY_TYPE);
			uiGenerator.linkPropertyToAdapter(parentObject, name, newInstance);
		}
		newInstance.setParentAdapter((uiContainerAdapter) parentAdapter);		newInstance.setUIFrame(parentAdapter.getUIFrame());	    
		newInstance.setRealObject(obj1);		newInstance.setViewObject(uiGenerator.getViewObject(obj1, false));
		newInstance.processAttributeList();			
		return newInstance;	}	*/
/*					  
public void uiComponentValueChanged() {		
				this.getUIFrame().doImplicitRefresh();}	*/	void processNameChild(Object newVal) {	  if (isNameChild) {	  ((CompositeAdapter) getParentAdapter()).nameChildChanged(getString(newVal));	}
  }	public  void init (ConcreteType concreteObject,													   /*Container containW,*/
													   Object obj, 											  Object obj1, 											  Object parentObject, 
											  int posn,											  String name, 											  ClassProxy inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor,
											  boolean textMode) {
	  	  super.init(concreteObject,													//containW, 
													obj, 
									obj1, 									parentObject, 
									posn,									name, 									inputClass, 									propertyFlag, 									 adaptor,
									 textMode);
	  if (propertyFlag)
			this.setAdapterType(ObjectAdapter.PROPERTY_TYPE);
	}	public boolean uiChanged (Object newValue) {
		return true;
			
	}
	

}



