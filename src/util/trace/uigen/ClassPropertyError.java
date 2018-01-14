package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ClassPropertyError extends ClassError {
	String property;
	public ClassPropertyError(String aMessage, String aProperty, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		property = aProperty;
	}
	
	public String getProperty() {
		return property;
	}	
	
}
