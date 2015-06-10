package bus.uigen.trace;

import util.trace.TraceableError;
import util.trace.Tracer;
import bus.uigen.oadapters.ObjectAdapter;

public class TooManyNodesInDisplayedLogicalStructure extends TraceableError {
	ObjectAdapter objectAdapter;
	int maxNodes;
	public TooManyNodesInDisplayedLogicalStructure(String aMessage, ObjectAdapter anAdapter, int aNodeLimit, Object aFinder) {
		super(aMessage, aFinder);
		maxNodes = aNodeLimit;
		objectAdapter = anAdapter;
	}
	
	public ObjectAdapter getObjectAdapter() {
		return objectAdapter;
	}


	public int getMaxNodes() {
		return maxNodes;
	}


	
	
	public static TooManyNodesInDisplayedLogicalStructure newCase(ObjectAdapter anAdapter, int aNodeLimit, Object aFinder) {
		String aMessage = "Number of nodes > max nodes: " + aNodeLimit +  " while creating node with path:" + anAdapter.getPath() 
				+ "\n Use @Visible(false) annotation for getter of the property causing the node explosition or ask the professor for a larger node limit.";
		TooManyNodesInDisplayedLogicalStructure retVal = new TooManyNodesInDisplayedLogicalStructure(aMessage, anAdapter, aNodeLimit, aFinder);
		retVal.announce();
		return retVal;
	}

}
