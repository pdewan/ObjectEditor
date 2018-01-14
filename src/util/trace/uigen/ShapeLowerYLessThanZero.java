package util.trace.uigen;

import util.trace.ObjectWarning;
import bus.uigen.oadapters.ObjectAdapter;

public class ShapeLowerYLessThanZero extends ObjectWarning {
	public ShapeLowerYLessThanZero(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}
	public static ShapeLowerYLessThanZero newCase(ObjectAdapter anAdapter, Object aTarget, int aY,  Object aFinder) {
		String aMessage = "";
		if (anAdapter != null)
		   aMessage = "Lower Y = " + aY + " < 0 of " + anAdapter.getPath() + " (" + aTarget + ")";
		else
			aMessage = "Lower Y = " + aY + " < 0 of " + "(" + aTarget + ")";
		ShapeLowerYLessThanZero retVal = new ShapeLowerYLessThanZero(aMessage, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}
}
