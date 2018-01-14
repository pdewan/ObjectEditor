package util.trace.uigen;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public class MissingControlGetter extends ClassWarning {
	public MissingControlGetter(String aMessage, ClassProxy aClass, Object aFinder) {
		super(aMessage, aClass, aFinder);	
	}
	
	public ClassProxy getTarget() {
		return targetClass;
	}
	
	public static MissingControlGetter newCase(ClassProxy aClass, Object aFinder) {
    	String aMessage = "Class: " + aClass.getName() + " should have int controls for both ControlX and ContrtolY";
    	MissingControlGetter retVal = new MissingControlGetter(aMessage, aClass, aFinder);
    	retVal.announce();
    	return retVal;
	}

}
