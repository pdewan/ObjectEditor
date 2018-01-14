package util.trace.uigen;

import util.trace.Tracer;
import util.trace.TraceableWarning;

public class ProgressBarOverflow extends TraceableWarning {
	public ProgressBarOverflow(String aMessage, Object aFinder) {
		super(aMessage, aFinder);	
	}
	
	public static ProgressBarOverflow newCase(int value, int maxValue, Object aFinder) {
    	String aMessage = "New progressBar value: " + value + " > " + "the max value: " + maxValue + '\n' + "Increasing the max value.";
    	ProgressBarOverflow retVal = new ProgressBarOverflow(aMessage, aFinder);
    	retVal.announce();
    	return retVal;
	}

}
