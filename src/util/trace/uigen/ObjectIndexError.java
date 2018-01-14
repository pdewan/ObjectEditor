package util.trace.uigen;

import util.trace.ObjectError;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;

public abstract class ObjectIndexError extends ObjectError {
	int index;
	public ObjectIndexError(String aMessage, int anIndex, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		index = anIndex;
	}	
	public int getIndex() {
		return index;
	}
}
