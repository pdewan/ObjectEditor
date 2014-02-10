package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualComponent;

public class UnboundButtonAdded extends UIComponentAdded {	
	
	public UnboundButtonAdded(String aMessage, VirtualComponent aParent, VirtualComponent aChild, Object aFinder) {
		super(aMessage, aParent, aChild, aFinder);
	}	
	public static UnboundButtonAdded newCase(VirtualComponent aParent, VirtualComponent aChild , Object aFinder) {
		String aMessage = deriveMessage(aParent, aChild, aFinder);
		UnboundButtonAdded retVal = new UnboundButtonAdded(aMessage, aParent, aChild, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
