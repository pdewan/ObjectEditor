package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class UnknownPropertyBind extends ObjectPropertyError {
	public UnknownPropertyBind(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aProperty, aTarget, aFinder);	
	}	

	

	public static UnknownPropertyBind newCase(String aProperty, Object aTarget, Object aFinder) {
		String aMessage = "Object " + aTarget + " does not have property " + aProperty;
		UnknownPropertyBind retVal = new UnknownPropertyBind(aMessage, aProperty, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
