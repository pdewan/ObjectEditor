package util.trace.uigen;

import util.trace.ObjectWarning;
import bus.uigen.oadapters.ObjectAdapter;

public class ShapeRightXLessThanZero extends ObjectWarning {
	public ShapeRightXLessThanZero(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}	

	

	public static ShapeRightXLessThanZero newCase(ObjectAdapter anAdapter, Object aTarget, int anX, Object aFinder) {
//		String aMessage = "X < 0 of " + anAdapter.getPath() + " (" + aTarget + ")";
		String aMessage = "Right X  = " + anX + " < 0 of " + anAdapter.getPath() + " (" + aTarget + ")";
		ShapeRightXLessThanZero retVal = new ShapeRightXLessThanZero(aMessage, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
