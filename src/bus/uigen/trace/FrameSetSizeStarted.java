package bus.uigen.trace;

import util.trace.ObjectInfo;

public class FrameSetSizeStarted extends ObjectInfo {	
	
	public FrameSetSizeStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static FrameSetSizeStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Frame Visibility Change Started: " + anObject;
		FrameSetSizeStarted retVal = new FrameSetSizeStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
