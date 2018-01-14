package util.trace.uigen;

import util.trace.ObjectInfo;

public class ShapeAddedToDrawingPanel extends ObjectInfo {	
	
	public ShapeAddedToDrawingPanel(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static ShapeAddedToDrawingPanel newCase(Object anObject, Object aFinder) {
		String aMessage = "Added to drawing panel;" + anObject;
		ShapeAddedToDrawingPanel retVal = new ShapeAddedToDrawingPanel(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
