package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ClassPatternError extends ClassError {
	String pattern;
	public ClassPatternError(String aMessage, String aPattern, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		pattern = aPattern;
	}	
	public String getPattern() {
		return pattern;
	}	
	
}
