package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ClassPropertyWarning extends ClassWarning {
	String property;
	public ClassPropertyWarning(String aMessage, String aProperty, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		property = aProperty;
	}
	
	public String getProperty() {
		return property;
	}	
	
}
