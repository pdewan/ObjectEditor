package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.MethodProxy;

public class ValidateHasWrongArguments extends MethodWarning {
	MethodProxy subject;
	public ValidateHasWrongArguments(String aMessage, MethodProxy aPre, MethodProxy aSubject, Object aFinder) {
		super(aMessage, aPre, aFinder);	
		subject = aSubject;
	}	
	
	public MethodProxy getSubject() {
		return subject;
	}	

	public static ValidateHasWrongArguments newCase(MethodProxy aValidate, MethodProxy aSubject,  Object aFinder) {
		String aMessage = aValidate + " not recognized as validate method of " +  aSubject + " as their parameters do not match";
		ValidateHasWrongArguments retVal = new ValidateHasWrongArguments(aMessage, aValidate, aSubject, aFinder);
		retVal.announce();
		return retVal;
	}

}
