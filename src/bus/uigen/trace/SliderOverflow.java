package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;

public class SliderOverflow extends TraceableWarning {
	public SliderOverflow(String aMessage, Object aFinder) {
		super(aMessage, aFinder);	
	}
	
	public static void newCase(int value, int maxValue, Object aFinder) {
    	String aMessage = "New slider value: " + value + " > " + "the max value: " + maxValue + '\n' + "Increasing the max value.";
		new SliderOverflow(aMessage, aFinder);
	}

}
