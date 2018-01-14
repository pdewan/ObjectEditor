package util.trace.uigen;

import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.FieldProxy;

public abstract class ClassFieldWarning extends ClassWarning {
	FieldProxy field;
	public ClassFieldWarning(String aMessage, FieldProxy aField, ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		field = aField;
	}
	
	public FieldProxy getField() {
		return field;
	}	
	
}
