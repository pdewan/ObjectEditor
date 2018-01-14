package util.trace.uigen;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.FieldProxy;

public class PublicVariable extends ClassFieldWarning {
	FieldProxy field;
	public PublicVariable(String aMessage, FieldProxy aField, ClassProxy aClass, Object aFinder) {
		super(aMessage, aField, aClass, aFinder);
	}	
	public static PublicVariable newCase(FieldProxy aField, ClassProxy aClass, Object aFinder) {
    	String aMessage = "Found public variable:" + aField.getName() + " in class: " + aClass + " Will lead to same object displayed twice if it is also a property.";
    	PublicVariable retVal = new PublicVariable(aMessage, aField, aClass, aFinder);
    	retVal.announce();
    	return retVal;
	}

}
