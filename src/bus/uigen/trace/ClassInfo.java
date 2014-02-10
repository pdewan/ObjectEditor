package bus.uigen.trace;

import util.trace.TraceableInfo;
import bus.uigen.reflect.ClassProxy;

public abstract class ClassInfo extends TraceableInfo{
	ClassProxy targetClass;
	public ClassInfo(String aMessage, ClassProxy aClass, Object aFinder) {
		super(aMessage,  aFinder);	
		targetClass = aClass;
	}
	
	public ClassProxy getTargetClass() {
		return targetClass;
	}

}
