package bus.uigen.trace;

import util.trace.ObjectWarning;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class StringShapeTextNull extends ObjectWarning {
	public StringShapeTextNull(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}
	public static StringShapeTextNull newCase(Object aTarget, Object aFinder) {
		String aMessage = "The text property of String shape " + aTarget + " is null" ;
		StringShapeTextNull retVal = new StringShapeTextNull(aMessage, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
