package bus.uigen.trace;

import util.trace.TraceableBus;
import util.trace.Tracer;
import util.trace.TraceableWarning;

public class IllegalComponentAddPosition extends TraceableWarning {
	public IllegalComponentAddPosition(String aMessage , Object aFinder) {
		super(aMessage, aFinder);		
	}
	
	public static IllegalComponentAddPosition newExample(int pos, Object aFinder) {
		String aMessage =	"Illegal position to add:" + pos
		+ " adding to end";	
		IllegalComponentAddPosition retVal = new IllegalComponentAddPosition(aMessage, aFinder);
		retVal.announce();
		return retVal;
	}

}
