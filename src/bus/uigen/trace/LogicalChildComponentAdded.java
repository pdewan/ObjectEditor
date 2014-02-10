package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualComponent;

public class LogicalChildComponentAdded extends UIComponentAdded {	
	
	public LogicalChildComponentAdded(String aMessage, VirtualComponent aParent, VirtualComponent aChild, Object aFinder) {
		super(aMessage, aParent, aChild, aFinder);
	}	
	public static LogicalChildComponentAdded newCase(VirtualComponent aParent, VirtualComponent aChild , Object aFinder) {
		String aMessage = deriveMessage(aParent, aChild, aFinder);
		LogicalChildComponentAdded retVal = new LogicalChildComponentAdded(aMessage, aParent, aChild, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
