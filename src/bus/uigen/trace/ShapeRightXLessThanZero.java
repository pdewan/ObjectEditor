package bus.uigen.trace;

import util.trace.ObjectWarning;
import bus.uigen.oadapters.ObjectAdapter;

public class ShapeRightXLessThanZero extends ObjectWarning {
	public ShapeRightXLessThanZero(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}	

	

	public static void newCase(ObjectAdapter anAdapter, Object aTarget, int anX, Object aFinder) {
//		String aMessage = "X < 0 of " + anAdapter.getPath() + " (" + aTarget + ")";
		String aMessage = "Right X  = " + anX + " < 0 of " + anAdapter.getPath() + " (" + aTarget + ")";
		new ShapeRightXLessThanZero(aMessage, aTarget, aFinder);
	}

}
