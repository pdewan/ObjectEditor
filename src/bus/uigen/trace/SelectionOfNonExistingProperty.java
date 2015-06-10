package bus.uigen.trace;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class SelectionOfNonExistingProperty extends ObjectPropertyError {
	public SelectionOfNonExistingProperty(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aProperty, aTarget, aFinder);	
	}	

	

	public static SelectionOfNonExistingProperty newCase(String aProperty, Object aTarget, Object aFinder) {
		String aMessage = "Selection of non existing:" + aProperty + " of object:" + aTarget;
		SelectionOfNonExistingProperty retVal = new SelectionOfNonExistingProperty(aMessage, aProperty, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
