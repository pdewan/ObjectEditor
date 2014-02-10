package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualComponent;

public class BoundComponentPanelAdded extends UIComponentAdded {	
	
	public BoundComponentPanelAdded(String aMessage, VirtualComponent aParent, VirtualComponent aChild, Object aFinder) {
		super(aMessage, aParent, aChild, aFinder);
	}	
	public static BoundComponentPanelAdded newCase(VirtualComponent aParent, VirtualComponent aChild , Object aFinder) {
		String aMessage = deriveMessage(aParent, aChild, aFinder);
		BoundComponentPanelAdded retVal = new BoundComponentPanelAdded(aMessage, aParent, aChild, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
