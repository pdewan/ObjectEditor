package util.trace.uigen;

import util.trace.ObjectInfo;

public class ConstantsMenuAdditionEnded extends ObjectInfo {	
	
	public ConstantsMenuAdditionEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static ConstantsMenuAdditionEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Constants menu addition ended: " + anObject;
		ConstantsMenuAdditionEnded retVal = new ConstantsMenuAdditionEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
