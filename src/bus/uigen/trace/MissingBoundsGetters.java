package bus.uigen.trace;

import util.annotations.IsAtomicShape;
import util.annotations.IsCompositeShape;
import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public class MissingBoundsGetters extends ClassWarning {
	public MissingBoundsGetters(String aMessage, ClassProxy aClass, Object aFinder) {
		super(aMessage, aClass, aFinder);	
	}
	
	public ClassProxy getTarget() {
		return targetClass;
	}
	
	public static void newCase(ClassProxy aClass, Object aFinder) {
    	String aMessage = aClass.getName() + " has X and Y coordinates the naming conventionas indicate an atomic shape. " +
    			"\n  If it is indeed an atomic shape it should have int getters for both width and height." + 
    			"\n  Otherwise, associate class with annotation @" + IsAtomicShape.class.getSimpleName() + "(false)" +
    			"\n  Or the annotation @" + IsCompositeShape.class.getSimpleName() + "(true), depending on whether it is a shape or not";
   	    new MissingBoundsGetters(aMessage, aClass, aFinder);	}

}
