package bus.uigen.trace;

import util.annotations.Explanation;
import util.annotations.WebDocuments;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
@WebDocuments(
		{"Lectures/DocumentationAnnotations.pdf", 
		"Lectures/DocumentationAnnotations.pptx"}
		)
public class UndeclaredEditableProperty extends ClassPropertyWarning {
//	String property;
//	ClassProxy target;
	public UndeclaredEditableProperty(String aMessage, String aProperty, ClassProxy aTarget, Object aFinder) {
		super(aMessage,aProperty,  aTarget,  aFinder);	
//		property = aProperty;
//		target = aTarget;
	}
	
//	public String getProperty() {
//		return property;
//	}
//
//	public ClassProxy getTarget() {
//		return target;
//	}

	public static void newCase(String aProperty, ClassProxy aTarget, Object aFinder) {
		String aMessage = "Implicitly editable property: " + aProperty +   "of class " + aTarget + " not considered editable as it is not in editable property names list. \n";
		new UndeclaredEditableProperty(aMessage, aProperty, aTarget, aFinder);
	}

}
