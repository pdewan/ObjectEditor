package bus.uigen.trace;

import util.trace.TraceableWarning;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class MissingGetterOfProperty extends ClassPropertyError {

	public MissingGetterOfProperty(String aMessage, String aProperty, ClassProxy aTarget, Object aFinder) {
		super(aMessage,aProperty,  aTarget,  aFinder);
		
	}
	
	


	public static void newCase(String aProperty, ClassProxy aTarget, Object aFinder) {
		String aMessage = "For property: "
				+ aProperty
				+ " in  property names, please define a getter with the header:\n\t"
				+ IntrospectUtility.toGetterSignature(aProperty);
		new MissingGetterOfProperty(aMessage,  aProperty,  aTarget, aFinder);
	}

}
