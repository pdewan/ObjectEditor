package bus.uigen.trace;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class SelectionIndexOutOfBounds extends ObjectIndexError {
	public SelectionIndexOutOfBounds(String aMessage, int anIndex, Object aTarget, Object aFinder) {
		super(aMessage, anIndex, aTarget, aFinder);	
	}
	public static void newCase(int anIndex, Object aTarget, Object aFinder) {
		String aMessage = "Selected index " + anIndex + " of object "  + aTarget + " out of bounds.";
		new SelectionIndexOutOfBounds(aMessage, anIndex, aTarget, aFinder);
	}

}
