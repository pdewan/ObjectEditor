package util.trace.uigen;

import util.trace.ObjectInfo;

public class ConstantsMenuAdditionStarted extends ObjectInfo {	
	
	public ConstantsMenuAdditionStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static ConstantsMenuAdditionStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Constants menu addition started: " + anObject;
		ConstantsMenuAdditionStarted retVal = new ConstantsMenuAdditionStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
