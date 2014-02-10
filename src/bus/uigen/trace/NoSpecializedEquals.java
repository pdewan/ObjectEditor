package bus.uigen.trace;

import bus.uigen.reflect.ClassProxy;

public class NoSpecializedEquals extends ClassWarning {
	public NoSpecializedEquals(String aMessage, ClassProxy aClass, Object aFinder) {
		super(aMessage, aClass, aFinder);	
//		target = aClass;
	}
	
	public static void newCase(ClassProxy aClass, Object aFinder) {
    	String aMessage = "equals() method of Object not overriden in: " + aClass + 
    	". Please override it when you learn about it to make refresh work more reliably and efficiently";

    	new NoSpecializedEquals(aMessage, aClass, aFinder);
	}

}
