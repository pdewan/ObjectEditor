package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualComponent;

public class WidgetInstantiated extends ObjectInfo {	
	
	public WidgetInstantiated(String aMessage, ObjectAdapter anObjectAdapter, VirtualComponent aComponent, Object aFinder) {
		super(aMessage, anObjectAdapter, aFinder);
	}	
	public static WidgetInstantiated newCase(ObjectAdapter anObjectAdapter, VirtualComponent aComponent,  Object aFinder) {
		String aMessage = "Created widget:" + aComponent  + " for object: " + anObjectAdapter.getRealObject();
		WidgetInstantiated retVal = new WidgetInstantiated(aMessage, anObjectAdapter, aComponent, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
