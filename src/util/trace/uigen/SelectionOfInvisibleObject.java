package util.trace.uigen;

import util.trace.ObjectError;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class SelectionOfInvisibleObject extends ObjectError {
	public SelectionOfInvisibleObject(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}
	public static SelectionOfInvisibleObject newCase(Object aTarget, Object aFinder) {
		String aMessage = "Selected object: " + aTarget + " not displayed";
		SelectionOfInvisibleObject retVal = new SelectionOfInvisibleObject(aMessage, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
