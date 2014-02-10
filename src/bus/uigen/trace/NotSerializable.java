package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public class NotSerializable extends ClassWarning {
	public NotSerializable(String aMessage, ClassProxy aClass, Object aFinder) {
		super(aMessage, aClass, aFinder);	
//		target = aClass;
	}
	
	public static void newCase(ClassProxy aClass, Object aFinder) {
    	String aMessage = "Make class " + aClass + " serializable when you learn about this concept to make implicit refresh work reliably." ;
   	    new NotSerializable(aMessage, aClass, aFinder);
	}

}
