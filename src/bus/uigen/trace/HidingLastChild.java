package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.MethodProxy;

public class HidingLastChild extends TraceableWarning {
	
	public HidingLastChild(String aMessage, Object aFinder) {
		super(aMessage, aFinder);
	}
	
	
	public static HidingLastChild newCase(Object aParent, Object aFinder) {
		String aMessage = "Cannot hide last item of parent:" + aParent;
		HidingLastChild retVal = new HidingLastChild(aMessage, aFinder);
		retVal.announce();
		return retVal;
	}

}
