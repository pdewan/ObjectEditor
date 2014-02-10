package bus.uigen.trace;

import util.trace.ObjectInfo;

public class DrawingPanelDisplayEnded extends ObjectInfo {	
	
	public DrawingPanelDisplayEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static DrawingPanelDisplayEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Drawing Panel Displayed: " + anObject;
		DrawingPanelDisplayEnded retVal = new DrawingPanelDisplayEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
