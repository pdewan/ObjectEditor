package bus.uigen.trace;

import java.beans.PropertyChangeEvent;

import util.trace.ObjectWarning;
import util.trace.TraceableWarning;
import util.trace.Tracer;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class IllegalPropertyNotification extends ObjectWarning {
	PropertyChangeEvent propertyChangeEvent;
	public IllegalPropertyNotification(String aMessage, PropertyChangeEvent aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
		propertyChangeEvent = aProperty;
	}	
	
	public PropertyChangeEvent getPropertyChangeEvent() {
		return propertyChangeEvent;
	}

	

	public static void newCase(PropertyChangeEvent aProperty, Object aTarget, Object aFinder) {
		String aMessage = "Received illegal property notification "
						+ aProperty + " of object: " + aTarget + ". Check the property name and type of new value. Updating complete object.";
//		Tracer.warning("Received notification for unknown property: "
//				+ changedPrpertyName + " of object " + evt.getSource() + ". Updating complete object.");
		new IllegalPropertyNotification(aMessage, aProperty, aTarget, aFinder);
	}

}
