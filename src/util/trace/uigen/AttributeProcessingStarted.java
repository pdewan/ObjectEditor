package util.trace.uigen;

import util.trace.ObjectInfo;

public class AttributeProcessingStarted extends MajorStepInfo {	
	
	public AttributeProcessingStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static AttributeProcessingStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Processing attributes of tree rooted by: " + anObject;
		AttributeProcessingStarted retVal = new AttributeProcessingStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
