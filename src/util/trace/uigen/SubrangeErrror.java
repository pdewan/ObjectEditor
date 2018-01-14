package util.trace.uigen;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.MethodProxy;

public class SubrangeErrror extends TraceableWarning {
	Object value;
	String path;
	public Object getValue() {
		return value;
	}


	public String getPath() {
		return path;
	}


	public SubrangeErrror(String aMessage, Object aValue, String aPath, Object aFinder) {
		super(aMessage, aFinder);
		value = aValue;
		path = aPath;
	}
	
	
	public static SubrangeErrror newCase(Object aValue, String aPath, Object aFinder) {
		String aMessage = "Value: " + aValue + " of " + aPath + " not in range.";
		SubrangeErrror retVal = new SubrangeErrror(aMessage, aValue, aPath, aFinder);
		retVal.announce();
		return retVal;
	}

}
