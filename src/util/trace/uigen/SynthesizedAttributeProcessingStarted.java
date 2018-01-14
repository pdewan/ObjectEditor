package util.trace.uigen;

import util.trace.ObjectInfo;

public class SynthesizedAttributeProcessingStarted extends MajorStepInfo {	
	
	public SynthesizedAttributeProcessingStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static SynthesizedAttributeProcessingStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Processing sythesized attributes of tree rooted by: " + anObject;
		SynthesizedAttributeProcessingStarted retVal = new SynthesizedAttributeProcessingStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
