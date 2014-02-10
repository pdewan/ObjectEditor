package bus.uigen.trace;

import util.trace.ObjectInfo;

public class AttributeSynthesisStarted extends MajorStepInfo {	
	
	public AttributeSynthesisStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static AttributeSynthesisStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Synthesizing attributes of tree rooted by: " + anObject;
		AttributeSynthesisStarted retVal = new AttributeSynthesisStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
