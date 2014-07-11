package bus.uigen.query;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.models.EqualPropertiesDefiner;
import util.trace.Traceable;
import bus.uigen.ObjectEditor;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.sadapters.BeanToRecordFactory;
import bus.uigen.sadapters.RecordStructure;

public class AnObjectQuery implements ObjectQuery{
	public final String CLASS_PROPERTY_NAME = "Class";
	protected Class expectedClass;
	protected Object expectedObject;
	protected Map<String, Object> propertyToExpectedValue = new HashMap();
	protected Map<String, Object> propertyToNotExpectedValue = new HashMap();

	protected String[] matchedObjectProperties;
	protected String[] unmatchedObjectProperties;
	
	public AnObjectQuery(Class anExpectedClass) {
		expectedClass = anExpectedClass;
	}
	public AnObjectQuery(Object anExpectedObject) {
		expectedObject = anExpectedObject;
	}
	public AnObjectQuery(EqualPropertiesDefiner anEqualPropertiesDefiner) {
		this(anEqualPropertiesDefiner, anEqualPropertiesDefiner.equalProperties().toArray(new String[anEqualPropertiesDefiner.equalProperties().size()]) );
	}
	public AnObjectQuery(Class anExpectedClass, 
			Map<String, Object> aPropertyToExpectedValue) {
		expectedClass = anExpectedClass;
		propertyToExpectedValue = aPropertyToExpectedValue;
	}
	public AnObjectQuery(Object anExpectedObject, String[] aMatchedProperties) {
		expectedObject = anExpectedObject;
		matchedObjectProperties = aMatchedProperties;
		propertyToExpectedValue = ObjectAdapter.beanToPropertyMap(anExpectedObject, aMatchedProperties);
		propertyToExpectedValue.put(CLASS_PROPERTY_NAME, anExpectedObject.getClass());
	}
	public AnObjectQuery(Object anExpectedObject, List<String> aMatchedProperties) {
		expectedObject = anExpectedObject;
		matchedObjectProperties = aMatchedProperties.toArray(new String[aMatchedProperties.size()]);
		propertyToExpectedValue = ObjectAdapter.beanToPropertyMap(anExpectedObject, aMatchedProperties);
		
	}
	public AnObjectQuery(Class anExpectedClass, Object anExpectedObject, String[] aMatchedProperties) {
		expectedClass = anExpectedClass;
		expectedObject = anExpectedObject;
		matchedObjectProperties = aMatchedProperties;
		propertyToExpectedValue = ObjectAdapter.beanToPropertyMap(anExpectedObject, aMatchedProperties);
		
	}
	public static final String EXPECTED_CLASS = "Expected Class";
	public static final String EXPECTED_PROPERTIES = "Properties";
	public static final String EXPECTED_VALUES = "Values";

	public  String classToString() {
		return expectedClass == null?"": EXPECTED_CLASS + "(" + 
				expectedClass.getName() + ")";
		
	}
	public  String propertiesToString() {
		if (propertyToExpectedValue == null) return null;
		Set<String> aPropertyNameSet = propertyToExpectedValue.keySet();
		String[] aPropertyNames = aPropertyNameSet.toArray(
				new String[aPropertyNameSet.size()]);
		String aPropertyNamesString = EXPECTED_PROPERTIES + "(";
		String aPropertyValuesString = EXPECTED_VALUES + "(";
		for (int i = 0; i < aPropertyNames.length; i++) {
			if (i != 0) {
				aPropertyNamesString += ", ";
				aPropertyValuesString += ", ";
			}
			aPropertyNamesString += aPropertyNames[i];
			aPropertyValuesString += propertyToExpectedValue.get(aPropertyNames[i]);						
		}
		aPropertyNamesString += ")";
		aPropertyValuesString += ")";
		return aPropertyNamesString + " " + aPropertyValuesString;		
		
	}
	public String toString() {
		return classToString() + " " + propertiesToString();	
	}
	public static ObjectQuery toBeanQuery(String aString) {
		Class aClass = null;
		List<String> anExpectedClassArgs = Traceable.getArgs(aString, EXPECTED_CLASS);
		if (anExpectedClassArgs != null || anExpectedClassArgs.size() != 0) {
			try {
				aClass = Class.forName(anExpectedClassArgs.get(0));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> aPropertyToExpectedValues = null;
		List<String> aPropertyNames = Traceable.getArgs(aString, EXPECTED_PROPERTIES);
		if (aPropertyNames != null || aPropertyNames.size() != 0) {
			aPropertyToExpectedValues = new HashMap<String, Object>();
			List<String> aPropertyValues = Traceable.getArgs(aString, EXPECTED_VALUES);
			for (int i = 0; i < aPropertyNames.size(); i++) {
				aPropertyToExpectedValues.put(aPropertyNames.get(i), aPropertyValues.get(i));
				
			}			
		}
		return new AnObjectQuery(aClass, aPropertyToExpectedValues);		
	}
	@Override
	public boolean isClassQuery() {
		return expectedClass != null && (propertyToExpectedValue == null || propertyToExpectedValue.size() == 0);
	}
	@Override
	public boolean isObjectQuery() {
		return expectedClass == null && (propertyToExpectedValue == null || propertyToExpectedValue.size() == 0) && (expectedObject != null);
	}
	
	
//	public static  Map<String, Object> beanToExpectedValues (Object aBean, String[] aProperties) {
//		ObjectAdapter aTopAdapter = ObjectEditor.toObjectAdapter(aBean);
//		Map<String, Object> retVal = new HashMap();
//		for (String aProperty:aProperties) {
//			ObjectAdapter aChildAdapter = aTopAdapter.pathToObjectAdapter(aProperty);
//			if (aChildAdapter == null )
//				retVal.put(aProperty, null);
//			else 
//				retVal.put(aProperty, aChildAdapter.getValue());		
//			
//		}
//		
//		return retVal;	
//		
//	}
	@Override
	public Object getExpectedObject() {
		return expectedObject;
	}
	@Override
	public Class getExpectedClass() {
		return expectedClass;
	}
	@Override
	public void setExpectedClass(Class expectedClass) {
		this.expectedClass = expectedClass;
	}
	@Override
	public Map<String, Object> getPropertyToExpectedValue() {
		return propertyToExpectedValue;
	}
	@Override
	public void setPropertyToExpectedValue(
			Map<String, Object> propertyToExpectedValue) {
		this.propertyToExpectedValue = propertyToExpectedValue;
	}
	public boolean matchesClass (Object aObject) {
		if (expectedClass == null) return true;
		return expectedClass.isAssignableFrom(aObject.getClass());
	}
	public boolean matchesObject (Object aObject) {
		if (expectedObject == null) return true;
		return false;
//		return expectedClass.isAssignableFrom(aTraceable.getClass());
	}
	public boolean matchesPropertiesSAdapter (Object aObject) {
		
		if (propertyToExpectedValue == null || propertyToExpectedValue.size() == 0)
			return true;
		ClassProxy aClassProxy = AClassProxy.classProxy(aObject.getClass());
		RecordStructure aRecord = new BeanToRecord (aClassProxy, aObject, null);	
		Set<String> aPoperties = propertyToExpectedValue.keySet();
		for (String aProperty:aPoperties) {
			Object anExpectedValue = propertyToExpectedValue.get(aProperty);
			Object anActualValue = aRecord.get(aProperty);
			if (anExpectedValue.equals(anActualValue)) return false;
		}
		return true;		
	}
	// eager evaluation
	public static boolean matchesProperties (Map<String, Object> anActualMap, Map<String, Object> anExpectedMap) {
		Set<String> aProperties = anActualMap.keySet();
		boolean retVal = true;
		for (String aProperty:aProperties) {
			Object anActualValue = anActualMap.get(aProperty);
			Object anExpectedValue = anExpectedMap.get(aProperty);
			if (!anActualValue.equals(anExpectedValue)) {
				retVal = false;
				// send trace message
			}
		}
		return retVal;
		
	}
	public boolean matchesProperties (Object anObject) {
		
		if (propertyToExpectedValue == null || propertyToExpectedValue.size() == 0) {
			if (expectedObject != null)
				return expectedObject.equals(anObject);
		
			return true;
		}
		 Set<String> aProperties = propertyToExpectedValue.keySet();
		 

		 Map<String, Object> propertyToActualValue = ObjectAdapter.beanToPropertyMap(anObject, aProperties);
		 propertyToActualValue.put(CLASS_PROPERTY_NAME, anObject.getClass());
		 return matchesProperties(propertyToActualValue, propertyToExpectedValue);
				 
//				 propertyToExpectedValue.equals(propertyToActualValue);
		
	}
	public boolean matches (Object aObject) {
		return matchesClass(aObject) && 
				matchesProperties(aObject);
//		if (!(expectedClass.isAssignableFrom(aTraceable.getClass())))
//				return false;
//		if (propertyToExpectedValue == null || propertyToExpectedValue.size() == 0)
//			return true;
//		ClassProxy aClassProxy = AClassProxy.classProxy(aTraceable.getClass());
//		RecordStructure aRecord = new BeanToRecord (aClassProxy, aTraceable, null);	
//		Set<String> aPoperties = propertyToExpectedValue.keySet();
//		for (String aProperty:aPoperties) {
//			Object anExpectedValue = propertyToExpectedValue.get(aProperty);
//			Object anActualValue = aRecord.get(aProperty);
//			if (anExpectedValue.equals(anActualValue)) return false;
//		}
//		return true;

		
		
	}
	
	
	
	

}
