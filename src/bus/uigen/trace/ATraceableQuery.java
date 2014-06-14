package bus.uigen.trace;

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

public class ATraceableQuery implements TraceableQuery{
	protected Class expectedClass;
	protected Traceable expectedTraceable;
	protected Map<String, Object> propertyToExpectedValue = new HashMap();

	protected String[] matchedTraceableProperties;
	
	public ATraceableQuery(Class anExpectedClass) {
		expectedClass = anExpectedClass;
	}
	public ATraceableQuery(Traceable anExpectedTraceable) {
		expectedTraceable = anExpectedTraceable;
	}
	public ATraceableQuery(Class anExpectedClass, Map<String, Object> aPropertyToExpectedValue) {
		expectedClass = anExpectedClass;
		propertyToExpectedValue = aPropertyToExpectedValue;
	}
	public ATraceableQuery(Traceable anExpectedTraceable, String[] aMatchedProperties) {
		expectedTraceable = anExpectedTraceable;
		matchedTraceableProperties = aMatchedProperties;
		propertyToExpectedValue = ObjectAdapter.beanToPropertyMap(anExpectedTraceable, aMatchedProperties);
		
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
	public boolean matchesClass (Traceable aTraceable) {
		if (expectedClass == null) return true;
		return expectedClass.isAssignableFrom(aTraceable.getClass());
	}
	public boolean matchesTraceable (Traceable aTraceable) {
		if (expectedTraceable == null) return true;
		return false;
//		return expectedClass.isAssignableFrom(aTraceable.getClass());
	}
	public boolean matchesPropertiesSAdapter (Traceable aTraceable) {
		
		if (propertyToExpectedValue == null || propertyToExpectedValue.size() == 0)
			return true;
		ClassProxy aClassProxy = AClassProxy.classProxy(aTraceable.getClass());
		RecordStructure aRecord = new BeanToRecord (aClassProxy, aTraceable, null);	
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
	public boolean matchesProperties (Traceable aTraceable) {
		
		if (propertyToExpectedValue == null || propertyToExpectedValue.size() == 0)
			return true;
		 Set<String> aProperties = propertyToExpectedValue.keySet();
		 

		 Map<String, Object> propertyToActualValue = ObjectAdapter.beanToPropertyMap(aTraceable, aProperties);
		 return matchesProperties(propertyToActualValue, propertyToExpectedValue);
				 
//				 propertyToExpectedValue.equals(propertyToActualValue);
		
	}
	public boolean matches (Traceable aTraceable) {
		return matchesClass(aTraceable) && matchesProperties(aTraceable);
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
