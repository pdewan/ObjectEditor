package bus.uigen.trace;

import util.annotations.Explanation;
import util.annotations.WebDocuments;
import util.trace.ImageFileMissing;
import util.trace.TraceableWarning;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
@WebDocuments({"Lectures/DocumentationAnnotations.pdf"})
@Explanation("Error when an property listed using the annotation @EditableProperty() does not have a propert setter")
public class MissingSetterOfEditableProperty extends ClassPropertyError {
	MethodProxy getter;

	public MissingSetterOfEditableProperty(String aMessage, String aProperty, MethodProxy aGetter, ClassProxy aTarget, Object aFinder) {
		super(aMessage,aProperty,  aTarget,  aFinder);
		getter = aGetter;
	}
	
	public MethodProxy getGetter() {
		return getter;
	}
	


	public static MissingSetterOfEditableProperty newCase(String aProperty, MethodProxy aGetter, ClassProxy aTarget, Object aFinder) {
		String aMessage = "For editable property " + aProperty + " of " + aTarget.getSimpleName() + " please define a setter with the header:\n\t" +
						IntrospectUtility.toSetterSignature(aProperty, aGetter);
		MissingSetterOfEditableProperty retVal = new MissingSetterOfEditableProperty(aMessage,  aProperty, aGetter, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
