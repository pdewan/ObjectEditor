package bus.uigen.trace;

import util.trace.TraceableError;
import bus.uigen.reflect.ClassProxy;

public abstract class ClassError extends TraceableError {
	ClassProxy targetClass;
	public ClassError(String aMessage, ClassProxy aClass, Object aFinder) {
		super(aMessage, aFinder);	
		targetClass = aClass;
	}
	
	public ClassProxy getTargetClass() {
		return targetClass;
	}

}
