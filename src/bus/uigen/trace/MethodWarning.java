package bus.uigen.trace;

import util.trace.TraceableWarning;
import bus.uigen.reflect.MethodProxy;

public abstract class MethodWarning extends TraceableWarning {
	MethodProxy targetMethod;
	public MethodWarning(String aMessage, MethodProxy aMethod, Object aFinder) {
		super(aMessage, aFinder);	
		targetMethod = aMethod;
	}
	
	public MethodProxy getTargetMethod() {
		return targetMethod;
	}

}
