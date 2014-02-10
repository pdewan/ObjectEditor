package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class CompositePropertyBind extends ObjectPropertyError {
	public CompositePropertyBind(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aProperty, aTarget, aFinder);	
	}	

	

	public static void newCase(String aProperty, Object aTarget, Object aFinder) {
		String aMessage = "Property " + aProperty + "of object " + aTarget + " is not primitive";
		new CompositePropertyBind(aMessage, aProperty, aTarget, aFinder);
	}

}
