package bus.uigen.trace;

import util.trace.ObjectInfo;

public class ElideStarted extends MajorStepInfo {	
	
	public ElideStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static ElideStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Eliding components of tree rooted by:" + anObject;
		ElideStarted retVal = new ElideStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
