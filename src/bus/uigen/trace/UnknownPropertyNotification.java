package bus.uigen.trace;

import util.trace.TraceableWarning;
import util.trace.Tracer;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class UnknownPropertyNotification extends ObjectPropertyWarning {
	public UnknownPropertyNotification(String aMessage, String aProperty, Object aTarget, Object aFinder) {
		super(aMessage, aProperty, aTarget, aFinder);	
	}	

	

	public static UnknownPropertyNotification newCase(String aProperty, Object aTarget, Object aFinder) {
		String aMessage = "Received notification(s) for unknown (possibly invisible or unrecognized atomic-shape) property: "
						+ aProperty + " of object: " + aTarget 
//						+ ". Updating complete object.";
						+ ". Ignoring notification.";
//		Tracer.warning("Received notification for unknown property: "
//				+ changedPrpertyName + " of object " + evt.getSource() + ". Updating complete object.");
		UnknownPropertyNotification retVal = new UnknownPropertyNotification(aMessage, aProperty, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
