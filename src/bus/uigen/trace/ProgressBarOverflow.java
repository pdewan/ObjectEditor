package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;

public class ProgressBarOverflow extends TraceableWarning {
	public ProgressBarOverflow(String aMessage, Object aFinder) {
		super(aMessage, aFinder);	
	}
	
	public static void newCase(int value, int maxValue, Object aFinder) {
    	String aMessage = "New progressBar value: " + value + " > " + "the max value: " + maxValue + '\n' + "Increasing the max value.";
		new ProgressBarOverflow(aMessage, aFinder);
	}

}
