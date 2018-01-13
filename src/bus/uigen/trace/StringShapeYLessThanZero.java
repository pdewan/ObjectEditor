package bus.uigen.trace;

import util.trace.ObjectWarning;
import util.trace.TraceableWarning;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class StringShapeYLessThanZero extends ObjectWarning {
	public StringShapeYLessThanZero(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}
	public static StringShapeYLessThanZero newCase(ObjectAdapter aTarget, int aY, Object aFinder) {
		String aMessage = "The Y property  + height of " +  aTarget.getPath() + "= " + aY +  " is < 0. It will not be visible";
		StringShapeYLessThanZero retVal = new StringShapeYLessThanZero(aMessage, aTarget.getRealObject(), aFinder);
		retVal.announce();
		return retVal;
	}

}
