package bus.uigen.trace;

import util.trace.ObjectInfo;

public class MenuAdditionEnded extends ObjectInfo {	
	
	public MenuAdditionEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static MenuAdditionEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Adding menus to UI created for: " + anObject;
		MenuAdditionEnded retVal = new MenuAdditionEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
