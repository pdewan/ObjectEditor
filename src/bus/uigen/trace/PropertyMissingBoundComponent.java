package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class PropertyMissingBoundComponent extends ObjectPropertyError {
	public PropertyMissingBoundComponent(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aProperty, aTarget, aFinder);	
	}	

	

	public static PropertyMissingBoundComponent newCase(String aProperty, Object aTarget, Object aFinder) {
		String aMessage = "Please provide UI component for property: " + aProperty;
		PropertyMissingBoundComponent retVal = new PropertyMissingBoundComponent(aMessage, aProperty, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
