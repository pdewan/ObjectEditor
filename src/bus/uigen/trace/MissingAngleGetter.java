package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public class MissingAngleGetter extends ClassWarning {
	public MissingAngleGetter(String aMessage, ClassProxy aClass, Object aFinder) {
		super(aMessage, aClass, aFinder);	
//		target = aClass;
	}
	
	
	
	public static void newCase(ClassProxy aClass, Object aFinder) {
    	String aMessage = "Class: " + aClass.getName() + " should have int getters for both startAngle and endAngle";
   	    new MissingAngleGetter(aMessage, aClass, aFinder);
	}

}
