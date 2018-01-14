package util.trace.uigen;

import util.trace.Tracer;
import util.trace.TraceableWarning;
import bus.uigen.reflect.MethodProxy;

public class SeparateThreadRequest extends MethodWarning {
	public SeparateThreadRequest(String aMessage, MethodProxy aMethod, Object aFinder) {
		super(aMessage, aMethod, aFinder);
	}
	
	
	public static SeparateThreadRequest newCase(MethodProxy aMethod, Object aFinder) {
		String aMessage = "Using a separate ObjectEditor thread for invoking: " + aMethod + 
				".\n   If you know threads, you might want to create a thread yourself.";
		SeparateThreadRequest retVal = new SeparateThreadRequest(aMessage, aMethod, aFinder);
		retVal.announce();
		return retVal;
	}

}
