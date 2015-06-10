package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;

public class SliderUnderflow extends TraceableWarning {
	public SliderUnderflow(String aMessage, Object aFinder) {
		super(aMessage, aFinder);	
	}
	
	public static SliderUnderflow newCase(int value, int minValue, Object aFinder) {
    	String aMessage = "New slider value: " + value + " < " + "the min value: " + minValue + '\n' + "Decreasing the min value.";
    	SliderUnderflow retVal = new SliderUnderflow(aMessage, aFinder);
    	retVal.announce();
    	return retVal;
	}

}
