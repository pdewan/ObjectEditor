package bus.uigen.trace;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.FieldProxy;

public abstract class ClassFieldError extends ClassError {
	FieldProxy field;
	public ClassFieldError(String aMessage, FieldProxy aField, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		field = aField;
	}
	
	public FieldProxy getField() {
		return field;
	}	
	
}
