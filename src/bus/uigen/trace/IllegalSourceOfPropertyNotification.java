package bus.uigen.trace;

import java.beans.PropertyChangeEvent;

import util.trace.ObjectWarning;
import util.trace.TraceableWarning;
import util.trace.Tracer;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class IllegalSourceOfPropertyNotification extends ObjectWarning {
	PropertyChangeEvent propertyChangeEvent;
	public IllegalSourceOfPropertyNotification(String aMessage, PropertyChangeEvent aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		propertyChangeEvent = aProperty;
	}	
	
	public PropertyChangeEvent getPropertyChangeEvent() {
		return propertyChangeEvent;
	}

	

	public static void newCase(PropertyChangeEvent anEvent, Object aTarget, Object aFinder) {
		String aMessage = "Source of property notification: "
						+ anEvent + "\n\tnot the same as the expected obervable:" + aTarget +
						".\n\tPerhaps addPropertyChangeListener() of: " + aTarget + " registered its listener with other objects such as its children, which should be done in another kind of method.";
//		Tracer.warning("Received notification for unknown property: "
//				+ changedPrpertyName + " of object " + evt.getSource() + ". Updating complete object.");
		new IllegalSourceOfPropertyNotification(aMessage, anEvent, aTarget, aFinder);
	}

}
