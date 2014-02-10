package bus.uigen.trace;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class SelectionOfNonExistingProperty extends ObjectPropertyError {
	public SelectionOfNonExistingProperty(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aProperty, aTarget, aFinder);	
	}	

	

	public static void newCase(String aProperty, Object aTarget, Object aFinder) {
		String aMessage = "Selection of non existing:" + aProperty + " of object:" + aTarget;
		new SelectionOfNonExistingProperty(aMessage, aProperty, aTarget, aFinder);
	}

}
