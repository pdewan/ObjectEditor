package bus.uigen.trace;

import util.trace.TraceableWarning;
import bus.uigen.reflect.MethodProxy;

public class PreHasArguments extends MethodWarning {
	MethodProxy pre;
	MethodProxy subject;
	public PreHasArguments(String aMessage, MethodProxy aPre, MethodProxy aSubject, Object aFinder) {
		super(aMessage, aPre, aFinder);	
		pre = aPre;	
		subject = aSubject;
	}	
	
	public MethodProxy getSubject() {
		return subject;
	}	

	public static PreHasArguments newCase(MethodProxy aPre, MethodProxy aSubject,  Object aFinder) {
		String aMessage = aPre + " not recognized as an enabling method of + " + aSubject + " because it takes arguments";
		PreHasArguments retVal = new PreHasArguments(aMessage, aPre, aSubject, aFinder);
		retVal.announce();
		return retVal;
	}

}
