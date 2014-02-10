package bus.uigen.trace;

import util.trace.TraceableInfo;
import bus.uigen.reflect.MethodProxy;

public abstract class MethodInfo extends TraceableInfo {
	MethodProxy targetMethod;
	public MethodInfo(String aMessage, MethodProxy aMethod, Object aFinder) {
		super(aMessage, aFinder);	
		targetMethod = aMethod;
	}
	
	public MethodProxy getTargetMethod() {
		return targetMethod;
	}

}
