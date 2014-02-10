package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class UnknownPropertyBind extends ObjectPropertyError {
	public UnknownPropertyBind(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aProperty, aTarget, aFinder);	
	}	

	

	public static void newCase(String aProperty, Object aTarget, Object aFinder) {
		String aMessage = "Object " + aTarget + " does not have property " + aProperty;
		new UnknownPropertyBind(aMessage, aProperty, aTarget, aFinder);
	}

}
