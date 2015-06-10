package bus.uigen.trace;

import util.trace.Tracer;
import util.trace.TraceableWarning;

public class LeafAdapterInconsistency extends TraceableWarning {
	public LeafAdapterInconsistency(String aMessage, Object aFinder) {
		super(aMessage, aFinder);	
	}
	
	public static LeafAdapterInconsistency newExample(int pos, int size, Object aFinder) {
		String aMessage = "Leaf adapter inconsistency, accessing:" + pos + "size:" + size + "\n Hide the main panel to continue working, and report this error if you have not received the multiple visit error. This error does not change the behavior of your program.";
		LeafAdapterInconsistency retVal = new LeafAdapterInconsistency(aMessage, aFinder);
		retVal.announce();
		return retVal;
	}

}
