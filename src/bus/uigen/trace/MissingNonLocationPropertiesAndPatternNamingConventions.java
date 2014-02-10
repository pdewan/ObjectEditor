package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public class MissingNonLocationPropertiesAndPatternNamingConventions extends ClassWarning {
	public MissingNonLocationPropertiesAndPatternNamingConventions(String aMessage, ClassProxy aClass, Object aFinder) {
		super(aMessage, aClass, aFinder);	
	}
	
	
	
	
	public static void newCase(ClassProxy aClass, Object aFinder) {
    	String aMessage = aClass.getName() + " has X and Y coordinates.\n  " +
//				"If it defines an atomic geometric object, the name of "+ c.getName() + " or one of its super types should contain the words Arc, Curve, Line, Rectangle, Point, or Oval" +
				"If it defines an atomic geometric object, use the naming or annotation rules for such a shape." +
				"\n  Otherwise use a Bean, List or some other pattern annotation, or " + util.annotations.IsAtomicShape.class.getSimpleName() + " (false) for the class "; 			
    			
   	    new MissingNonLocationPropertiesAndPatternNamingConventions(aMessage, aClass, aFinder);
	}

}
