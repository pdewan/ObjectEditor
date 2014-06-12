package bus.uigen.trace;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import util.trace.Traceable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.sadapters.BeanToRecord;
import bus.uigen.sadapters.BeanToRecordFactory;
import bus.uigen.sadapters.RecordStructure;

public class ATraceableQuery implements TraceableQuery{
	protected Class expectedClass;
	protected Map<String, Object> propertyToExpectedValue = new HashMap();
	
	public ATraceableQuery(Class anExpectedClass) {
		expectedClass = anExpectedClass;
	}
	public ATraceableQuery(Class anExpectedClass, Map<String, Object> aPropertyToExpectedValue) {
		expectedClass = anExpectedClass;
		propertyToExpectedValue = aPropertyToExpectedValue;
	}
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
	public boolean matchesProperties (Traceable aTraceable) {
		
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
