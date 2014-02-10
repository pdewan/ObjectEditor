package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualComponent;

public class RowPanelAdded extends UIComponentAdded {	
	
	public RowPanelAdded(String aMessage, VirtualComponent aParent, VirtualComponent aChild, Object aFinder) {
		super(aMessage, aParent, aChild, aFinder);
	}	
	public static RowPanelAdded newCase(VirtualComponent aParent, VirtualComponent aChild , Object aFinder) {
		String aMessage = deriveMessage(aParent, aChild, aFinder);
		RowPanelAdded retVal = new RowPanelAdded(aMessage, aParent, aChild, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
