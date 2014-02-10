package bus.uigen.trace;

import util.trace.ObjectError;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ObjectPropertyError extends ObjectError {
	String property;
	public ObjectPropertyError(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		property = aProperty;
	}
	
	public String getProperty() {
		return property;
	}
	
	

}
