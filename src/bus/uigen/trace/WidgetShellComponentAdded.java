package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualComponent;

public class WidgetShellComponentAdded extends UIComponentAdded {	
	
	public WidgetShellComponentAdded(String aMessage, VirtualComponent aParent, VirtualComponent aChild, Object aFinder) {
		super(aMessage, aParent, aChild, aFinder);
	}	
	public static WidgetShellComponentAdded newCase(VirtualComponent aParent, VirtualComponent aChild , Object aFinder) {
		String aMessage = deriveMessage(aParent, aChild, aFinder);
		WidgetShellComponentAdded retVal = new WidgetShellComponentAdded(aMessage, aParent, aChild, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
