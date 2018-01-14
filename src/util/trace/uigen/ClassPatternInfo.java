package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ClassPatternInfo extends ClassInfo {
	String pattern;
	public ClassPatternInfo(String aMessage, String aPattern, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		pattern = aPattern;
	}	
	public String getPattern() {
		return pattern;
	}	
}
