package util.trace.uigen;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public class MissingNonLocationProperties extends ClassWarning {
	String pattern;
	public MissingNonLocationProperties(String aMessage, ClassProxy aClass, String aPattern, Object aFinder) {
		super(aMessage, aClass, aFinder);	
		pattern = aPattern;
	}
	
	
	public String getPattern() {
		return pattern;
	}
	
	
	public static MissingNonLocationProperties newCase(ClassProxy aClass, String aPattern, Object aFinder) {
    	String aMessage = aClass.getName() + " has X and Y coordinates and follows the naming convention of " + aPattern +
//				"If it defines an atomic geometric object, the name of "+ c.getName() + " or one of its super types should contain the words Arc, Curve, Line, Rectangle, Point, or Oval" +
				"\n  If it defines an atomic geometric object, use the rules for such a shape." +
				"\n  Otherwise use a Bean, List or some other pattern annotation, or " + util.annotations.IsAtomicShape.class.getSimpleName() + " (false) for the class "; 			
    			
    	MissingNonLocationProperties retVal = new MissingNonLocationProperties(aMessage, aClass, aPattern, aFinder);
    	retVal.announce();
    	return retVal;
    	
	}

}
