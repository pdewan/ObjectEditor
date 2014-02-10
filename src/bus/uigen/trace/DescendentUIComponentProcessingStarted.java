package bus.uigen.trace;

import util.trace.ObjectInfo;

public class DescendentUIComponentProcessingStarted extends MajorStepInfo {	
	
	public DescendentUIComponentProcessingStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static DescendentUIComponentProcessingStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Synthesizing UI Components of Tree Rooted by:" + anObject;
		DescendentUIComponentProcessingStarted retVal = new DescendentUIComponentProcessingStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
