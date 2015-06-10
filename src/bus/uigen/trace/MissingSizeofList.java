package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class MissingSizeofList extends ClassError {

	public MissingSizeofList(String aMessage,  ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget,  aFinder);
	}
	


	public static MissingSizeofList newCase( ClassProxy aTarget, Object aFinder) {
		String aMessage = "Expecting in class: " +  aTarget.getName() + " the size method with header: public int size()";	
		
		MissingSizeofList retVal = new MissingSizeofList(aMessage, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
