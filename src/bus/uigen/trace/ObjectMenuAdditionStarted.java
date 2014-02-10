package bus.uigen.trace;

import util.trace.ObjectInfo;

public class ObjectMenuAdditionStarted extends ObjectInfo {	
	
	public ObjectMenuAdditionStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static ObjectMenuAdditionStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Adding menus for methods of: " + anObject;
		ObjectMenuAdditionStarted retVal = new ObjectMenuAdditionStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
