package bus.uigen.trace;

import bus.uigen.reflect.ClassProxy;
import util.trace.ObjectInfo;

public class NoAdapterCreated extends ObjectInfo {	
	
	public NoAdapterCreated(String aMessage, Object anObject, ClassProxy inputClass, String path,  boolean shapeRequired, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	
	public static NoAdapterCreated newCase(Object anObject, ClassProxy inputClass, String path,  boolean shapeRequired,  Object aFinder) {
		
		String aMessage = "No adapter created for element "  + path + " of type " + inputClass;
		if (shapeRequired) {
			aMessage += "Either do  not require shape anotations or put a shape annotaion for the element or its type";
		}
		NoAdapterCreated retVal = new NoAdapterCreated(aMessage, anObject, inputClass,  path, shapeRequired, aFinder);
		retVal.announce();		
		return retVal;
	}

}
