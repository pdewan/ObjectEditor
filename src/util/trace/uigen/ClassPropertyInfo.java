package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ClassPropertyInfo extends ClassInfo {
	String property;
	public ClassPropertyInfo(String aMessage, String aProperty, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		property = aProperty;
	}
	
	public String getProperty() {
		return property;
	}	
	
}
