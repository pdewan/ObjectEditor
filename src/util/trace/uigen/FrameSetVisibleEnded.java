package util.trace.uigen;

import util.trace.ObjectInfo;

public class FrameSetVisibleEnded extends ObjectInfo {	
	
	public FrameSetVisibleEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static FrameSetVisibleEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Frame Visibility Change Ended: " + anObject;
		FrameSetVisibleEnded retVal = new FrameSetVisibleEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
