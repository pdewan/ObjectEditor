package util.trace.uigen;

import util.trace.ObjectInfo;

public class MenuAdditionStarted extends ObjectInfo {	
	
	public MenuAdditionStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static MenuAdditionStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Finished adding menus to UI created for: " + anObject;
		MenuAdditionStarted retVal = new MenuAdditionStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
