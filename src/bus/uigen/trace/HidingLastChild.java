package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.MethodProxy;

public class HidingLastChild extends TraceableWarning {
	
	public HidingLastChild(String aMessage, Object aFinder) {
		super(aMessage, aFinder);
	}
	
	
	public static void newCase(Object aParent, Object aFinder) {
		String aMessage = "Cannot hide last item of parent:" + aParent;
		new HidingLastChild(aMessage, aFinder);
	}

}
