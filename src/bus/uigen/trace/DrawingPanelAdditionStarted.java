package bus.uigen.trace;

import util.trace.ObjectInfo;

public class DrawingPanelAdditionStarted extends ObjectInfo {	
	
	public DrawingPanelAdditionStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static DrawingPanelAdditionStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Drawing Panel Creation Started: " + anObject;
		DrawingPanelAdditionStarted retVal = new DrawingPanelAdditionStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
