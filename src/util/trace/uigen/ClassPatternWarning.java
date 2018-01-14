package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ClassPatternWarning extends ClassWarning {
	String pattern;
	public ClassPatternWarning(String aMessage, String aPattern, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		pattern = aPattern;
	}	
	public String getPattern() {
		return pattern;
	}	
	
}
