package bus.uigen.trace;

import util.trace.ObjectWarning;
import bus.uigen.oadapters.ObjectAdapter;

public class ImageYLessThanZero extends ObjectWarning {
	public ImageYLessThanZero(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}
	public static ImageYLessThanZero newCase(ObjectAdapter aTarget, Object aFinder) {
		String aMessage = "The Y property of " +  aTarget.getPath() + " is < 0. Parts of it may not be visible";
		ImageYLessThanZero retVal = new ImageYLessThanZero(aMessage, aTarget.getRealObject(), aFinder);
		retVal.announce();
		return retVal;
	}

}
