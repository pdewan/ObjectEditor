package bus.uigen.trace;

import util.trace.ObjectError;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class SelectionOfIndexOfNonIndexedObject extends ObjectError {
	public SelectionOfIndexOfNonIndexedObject(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}
	public static void newCase(Object aTarget, Object aFinder) {
		String aMessage = "Selection of index of non indexed collection: " + aTarget;
		new SelectionOfIndexOfNonIndexedObject(aMessage, aTarget, aFinder);
	}

}
