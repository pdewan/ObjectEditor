package util.trace.uigen;

import util.trace.ObjectWarning;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ObjectPropertyWarning extends ObjectWarning {
	String property;
	public ObjectPropertyWarning(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		property = aProperty;
	}
	
	public String getProperty() {
		return property;
	}
	
	

}
