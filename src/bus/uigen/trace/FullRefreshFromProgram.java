package bus.uigen.trace;

import util.trace.ObjectWarning;
import util.trace.TraceableWarning;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class FullRefreshFromProgram extends ObjectWarning {
	public FullRefreshFromProgram(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}	

	

	public static FullRefreshFromProgram newCase(Object aTarget, Object aFinder) {
		String aMessage = "Refreshing complete object: " + aTarget + ". If you know them, announce property and/or list events.";
		FullRefreshFromProgram retVal = new FullRefreshFromProgram(aMessage, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
