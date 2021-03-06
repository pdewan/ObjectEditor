package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.MethodProxy;

public class MissingObserverRegistrarAnnotation extends MethodWarning {
	public MissingObserverRegistrarAnnotation(String aMessage, MethodProxy aMethod, Object aFinder) {
		super(aMessage,aMethod, aFinder);	
	}
	

	

	public static MissingObserverRegistrarAnnotation newCase(MethodProxy aMethod, String aListenerType, Class anAnnotation, Object aFinder) {
		String aMessage = aMethod
		+ " should be associated with annotation: " + "@" + anAnnotation.getSimpleName() + "(" + aListenerType + ")";
		MissingObserverRegistrarAnnotation retVal = new MissingObserverRegistrarAnnotation(aMessage, aMethod, aFinder);
		retVal.announce();
		return retVal;
	}

}
