package util.trace.uigen;

import util.trace.ObjectInfo;

public class UIComponentCreationStarted extends MajorStepInfo {	
	
	public UIComponentCreationStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static UIComponentCreationStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Creating UI components of nodes in tree rooted by: " + anObject;
		UIComponentCreationStarted retVal = new UIComponentCreationStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
