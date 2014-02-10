package bus.uigen.trace;

import util.trace.ObjectWarning;
import util.trace.TraceableWarning;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class StringShapeYLessThanZero extends ObjectWarning {
	public StringShapeYLessThanZero(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}
	public static void newCase(ObjectAdapter aTarget, Object aFinder) {
		String aMessage = "The Y property  + height of " +  aTarget.getPath() + " is < 0. It will not be visible";
		new StringShapeYLessThanZero(aMessage, aTarget.getRealObject(), aFinder);
	}

}
