package bus.uigen.trace;

import util.trace.ObjectInfo;

public class UIComponentConnectionStarted extends MajorStepInfo {	
	
	public UIComponentConnectionStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static UIComponentConnectionStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Connecting UI Components of tree rooted by:" + anObject;
		UIComponentConnectionStarted retVal = new UIComponentConnectionStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
