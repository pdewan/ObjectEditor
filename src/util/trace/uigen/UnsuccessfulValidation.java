package util.trace.uigen;

import util.trace.TraceableWarning;
import bus.uigen.reflect.MethodProxy;

public class UnsuccessfulValidation extends MethodWarning {
	Object target;
	Object[] parameterList;
	public UnsuccessfulValidation(String aMessage, MethodProxy aMethod, Object aTarget, Object[] aParameterList, Object aFinder) {
		super(aMessage, aMethod, aFinder);	
		parameterList = aParameterList;
		target = aTarget;
	}
	

	public Object getTargetObject() {
		return target;
	}

	public Object[] getParameterList() {
		return parameterList;
	}

	public static UnsuccessfulValidation newCase(MethodProxy aMethod, Object aTarget, Object[] aParameterList, Object aFinder) {
		String aMessage = "Validation of method: " + aMethod
				+ "of object: " + aTarget + " with parameter values " + aParameterList + " returns false";
		UnsuccessfulValidation retVal = new UnsuccessfulValidation(aMessage, aMethod, aTarget, aParameterList, aFinder);
		retVal.announce();
		return retVal;
	}

}
