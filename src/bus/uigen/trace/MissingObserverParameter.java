package bus.uigen.trace;

import javax.swing.JTextArea;

import util.annotations.DisplayToString;
import util.annotations.PreferredWidgetClass;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
//@DisplayToString(true)
@PreferredWidgetClass(JTextArea.class)
@DisplayToString(true)
public class MissingObserverParameter extends MethodWarning {
//	MethodProxy method;
	ClassProxy listenerType;
	public MissingObserverParameter(String aMessage, MethodProxy aMethod, ClassProxy aListenerType, Object aFinder) {
		super(aMessage, aMethod, aFinder);
		listenerType = aListenerType;
//		method = aMethod;		
	}
	
	
	public ClassProxy getListenerType() {
		return listenerType;
	}
	
//	public String toString() {
//		return super.toString();
//	}

	

	public static void newCase(MethodProxy aMethod, ClassProxy aListenerType,  Object aFinder) {
		String aMessage = "Use annotation @" + util.annotations.ObserverRegisterer.class.getSimpleName() + "("
				+ aListenerType
				+ ") for method " + aMethod;
		new MissingObserverParameter(aMessage, aMethod, aListenerType, aFinder);
	}

}
