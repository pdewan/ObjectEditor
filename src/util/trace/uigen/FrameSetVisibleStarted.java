package util.trace.uigen;

import util.trace.ObjectInfo;

public class FrameSetVisibleStarted extends ObjectInfo {	
	
	public FrameSetVisibleStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static FrameSetVisibleStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Frame Set Visible Started: " + anObject;
		FrameSetVisibleStarted retVal = new FrameSetVisibleStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
