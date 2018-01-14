package util.trace.uigen;

import util.trace.ObjectInfo;

public class DrawingPanelCreationStarted extends ObjectInfo {	
	
	public DrawingPanelCreationStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static DrawingPanelCreationStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Drawing Panel Creation Started: " + anObject;
		DrawingPanelCreationStarted retVal = new DrawingPanelCreationStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
