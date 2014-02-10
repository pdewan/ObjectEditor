package bus.uigen.trace;

import util.trace.ObjectWarning;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class StringShapeTextNull extends ObjectWarning {
	public StringShapeTextNull(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}
	public static void newCase(Object aTarget, Object aFinder) {
		String aMessage = "The text property of String shape " + aTarget + " is null" ;
		new StringShapeTextNull(aMessage, aTarget, aFinder);
	}

}
