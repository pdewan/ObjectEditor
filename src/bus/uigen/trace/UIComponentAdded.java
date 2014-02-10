package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualComponent;

public class UIComponentAdded extends ObjectInfo {	
	
	public UIComponentAdded(String aMessage, VirtualComponent aParent, VirtualComponent aChild, Object aFinder) {
		super(aMessage, aChild, aFinder);
	}	
	public static String deriveMessage(VirtualComponent aParent, VirtualComponent aChild , Object aFinder) {
		return "Child UI component:" + aChild.getName() + " added to:" + aParent.getName();
	}
	public static UIComponentAdded newCase(VirtualComponent aParent, VirtualComponent aChild , Object aFinder) {
		String aMessage = deriveMessage(aParent, aChild, aFinder);
		UIComponentAdded retVal = new UIComponentAdded(aMessage, aParent, aChild, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
