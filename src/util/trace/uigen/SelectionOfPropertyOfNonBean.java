package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class SelectionOfPropertyOfNonBean extends ObjectPropertyError {
	public SelectionOfPropertyOfNonBean(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aProperty, aTarget, aFinder);	
	}	

	

	public static SelectionOfPropertyOfNonBean newCase(String aProperty, Object aTarget, Object aFinder) {
		String aMessage = "Selection of property of non Bean: " + aTarget;
		SelectionOfPropertyOfNonBean retVal = new SelectionOfPropertyOfNonBean(aMessage, aProperty, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
