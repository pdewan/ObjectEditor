package bus.uigen.trace;

import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class UndeclaredProperty extends ClassPropertyWarning {
//	String property;
//	ClassProxy target;
	public UndeclaredProperty(String aMessage, String aProperty, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aProperty, aTarget, aFinder);	
//		property = aProperty;
//		target = aTarget;
	}
	
//	public String getProperty() {
//		return property;
//	}
//
//	public ClassProxy getTarget() {
//		return target;
//	}

	

	public static void newCase(String aProperty, ClassProxy aTarget, Object aFinder) {
		String aMessage = "Implicit property: " + aProperty + " of class " + aTarget +  " ignored as it is not in property names list. \n" + 
				" Associate annotation @Visible(false) with its getter.";
		new UndeclaredProperty(aMessage, aProperty, aTarget, aFinder);
	}

}
