package bus.uigen.trace;

import util.trace.ObjectInfo;

public class MenuTreeDisplayEnded extends ObjectInfo {	
	
	public MenuTreeDisplayEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static MenuTreeDisplayEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Menu tree display ended: " + anObject;
		MenuTreeDisplayEnded retVal = new MenuTreeDisplayEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
