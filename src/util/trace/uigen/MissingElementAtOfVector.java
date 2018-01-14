package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class MissingElementAtOfVector extends ClassError {

	public MissingElementAtOfVector(String aMessage,  ClassProxy aTarget, Object aFinder) {
		super(aMessage, aTarget,  aFinder);
	}
	


	public static MissingElementAtOfVector newCase( ClassProxy aTarget, Object aFinder) {
		String aMessage = "Expecting in class " + aTarget.getName() + " a read method with header: public <T> elementAt(int <parameter name>)";
				
		
		MissingElementAtOfVector retVal = new MissingElementAtOfVector(aMessage, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
