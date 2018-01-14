package util.trace.uigen;

import util.trace.Tracer;
import util.trace.TraceableWarning;

public class SliderOverflow extends TraceableWarning {
	public SliderOverflow(String aMessage, Object aFinder) {
		super(aMessage, aFinder);	
	}
	
	public static SliderOverflow newCase(int value, int maxValue, Object aFinder) {
    	String aMessage = "New slider value: " + value + " > " + "the max value: " + maxValue + '\n' + "Increasing the max value.";
    	SliderOverflow retVal = new SliderOverflow(aMessage, aFinder);
    	retVal.announce();
    	return retVal;
	}

}
