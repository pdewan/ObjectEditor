package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;

public class ProgressBarUnderflow extends TraceableWarning {
	public ProgressBarUnderflow(String aMessage, Object aFinder) {
		super(aMessage, aFinder);	
	}
	
	public static ProgressBarUnderflow newCase(int value, int minValue, Object aFinder) {
    	String aMessage = "New progressBar value: " + value + " < " + "the min value: " + minValue + '\n' + "Decreasing the min value.";
    	ProgressBarUnderflow retVal = new ProgressBarUnderflow(aMessage, aFinder);
    	retVal.announce();
    	return retVal;
	}

}
