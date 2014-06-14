package bus.uigen.query;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.trace.Traceable;
import bus.uigen.ObjectEditor;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.sadapters.BeanToRecordFactory;
import bus.uigen.sadapters.RecordStructure;

public class ABeanQuery implements BeanQuery{
	protected Class expectedClass;
	protected Object expectedObject;
	protected Map<String, Object> propertyToExpectedValue = new HashMap();
	protected Map<String, Object> propertyToNotExpectedValue = new HashMap();

	protected String[] matchedObjectProperties;
	protected String[] unmatchedObjectProperties;
	
	public ABeanQuery(Class anExpectedClass) {
		expectedClass = anExpectedClass;
	}
	public ABeanQuery(Object anExpectedObject) {
		expectedObject = anExpectedObject;
	}
	public ABeanQuery(Class anExpectedClass, 
			Map<String, Object> aPropertyToExpectedValue) {
		expectedClass = anExpectedClass;
		propertyToExpectedValue = aPropertyToExpectedValue;
	}
	public ABeanQuery(Object anExpectedObject, String[] aMatchedProperties) {
		expectedObject = anExpectedObject;
		matchedObjectProperties = aMatchedProperties;
		propertyToExpectedValue = ObjectAdapter.beanToPropertyMap(anExpectedObject, aMatchedProperties);
		
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
	
	public Class getExpectedClass() {
		return expectedClass;
	}
	public void setExpectedClass(Class expectedClass) {
		this.expectedClass = expectedClass;
	}
	public Map<String, Object> getPropertyToExpectedValue() {
		return propertyToExpectedValue;
	}
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
	public boolean matchesProperties (Object aObject) {
		
		if (propertyToExpectedValue == null || propertyToExpectedValue.size() == 0)
			return true;
		 Set<String> aProperties = propertyToExpectedValue.keySet();
		 

		 Map<String, Object> propertyToActualValue = ObjectAdapter.beanToPropertyMap(aObject, aProperties);
		 return matchesProperties(propertyToActualValue, propertyToExpectedValue);
				 
//				 propertyToExpectedValue.equals(propertyToActualValue);
		
	}
	public boolean matches (Object aObject) {
		return matchesClass(aObject) && matchesProperties(aObject);
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
