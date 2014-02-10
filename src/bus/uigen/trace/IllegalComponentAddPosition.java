package bus.uigen.trace;

import util.trace.TraceableBus;
import util.trace.Tracer;
import util.trace.TraceableWarning;

public class IllegalComponentAddPosition extends TraceableWarning {
	public IllegalComponentAddPosition(String aMessage , Object aFinder) {
		super(aMessage, aFinder);		
	}
	
	public static void newExample(int pos, Object aFinder) {
		String aMessage =	"Illegal position to add:" + pos
		+ " adding to end";	
		new IllegalComponentAddPosition(aMessage, aFinder);
	}

}
