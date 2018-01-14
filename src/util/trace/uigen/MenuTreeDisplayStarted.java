package util.trace.uigen;

import util.trace.ObjectInfo;

public class MenuTreeDisplayStarted extends ObjectInfo {	
	
	public MenuTreeDisplayStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static MenuTreeDisplayStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Menu tree display started: " + anObject;
		MenuTreeDisplayStarted retVal = new MenuTreeDisplayStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
