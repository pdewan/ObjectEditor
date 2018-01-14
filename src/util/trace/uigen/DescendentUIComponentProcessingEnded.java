package util.trace.uigen;

import util.trace.ObjectInfo;

public class DescendentUIComponentProcessingEnded extends MajorStepInfo {	
	
	public DescendentUIComponentProcessingEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static DescendentUIComponentProcessingEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Finished Synthesizing UI components of tree rooted by:" + anObject;
		DescendentUIComponentProcessingEnded retVal = new DescendentUIComponentProcessingEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
