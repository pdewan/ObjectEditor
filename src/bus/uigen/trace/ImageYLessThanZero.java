package bus.uigen.trace;

import util.trace.ObjectWarning;
import bus.uigen.oadapters.ObjectAdapter;

public class ImageYLessThanZero extends ObjectWarning {
	int y;
	public ImageYLessThanZero(String aMessage, Object aTarget, int aY, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}
	public static ImageYLessThanZero newCase(ObjectAdapter aTarget, int aY, Object aFinder) {
		String aMessage = "The Y property of " +  aTarget.getPath() + " = " + aY + " is < 0. Parts of it may not be visible";
		ImageYLessThanZero retVal = new ImageYLessThanZero(aMessage, aTarget.getRealObject(), aY, aFinder);
		retVal.announce();
		return retVal;
	}

}
