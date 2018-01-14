package util.trace.uigen;

import util.trace.ObjectInfo;

public class LinkingRootUIComponent extends ObjectInfo {	
	
	public LinkingRootUIComponent(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static LinkingRootUIComponent newCase(Object anObject, Object aFinder) {
		String aMessage = "Linking root panel of: " + anObject;
		LinkingRootUIComponent retVal = new LinkingRootUIComponent(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
