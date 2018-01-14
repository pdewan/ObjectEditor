package util.trace.uigen;

import util.trace.TraceableError;
import bus.uigen.reflect.MethodProxy;

public abstract class MethodError extends TraceableError {
	MethodProxy targetMethod;
	public MethodError(String aMessage, MethodProxy aMethod, Object aFinder) {
		super(aMessage, aFinder);	
		targetMethod = aMethod;
	}
	
	public MethodProxy getTargetMethod() {
		return targetMethod;
	}

}
