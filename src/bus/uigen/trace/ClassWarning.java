package bus.uigen.trace;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ClassWarning extends TraceableWarning {
	ClassProxy targetClass;
	public ClassWarning(String aMessage, ClassProxy aClass, Object aFinder) {
		super(aMessage, aFinder);	
		targetClass = aClass;
	}
	
	public ClassProxy getTargetClass() {
		return targetClass;
	}

}
