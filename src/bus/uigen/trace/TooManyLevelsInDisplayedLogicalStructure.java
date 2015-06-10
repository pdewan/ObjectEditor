package bus.uigen.trace;

import util.trace.TraceableError;
import util.trace.Tracer;
import bus.uigen.oadapters.ObjectAdapter;

public class TooManyLevelsInDisplayedLogicalStructure extends TraceableError {
	ObjectAdapter objectAdapter;
	int maxNodes;
	public TooManyLevelsInDisplayedLogicalStructure(String aMessage, ObjectAdapter anAdapter, int aNodeLimit, Object aFinder) {
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


	
	
	public static TooManyLevelsInDisplayedLogicalStructure newCase(ObjectAdapter anAdapter, int aNestingLimit, Object aFinder) {
		String aMessage = "Number of nodes > max nodes: " + aNestingLimit +  " while creating node with path:" + anAdapter.getPath() 
				+ "\n Use @Visible(false) annotation for getter of the property causing the node explosition or ask the professor for a larger node limit.";
		TooManyLevelsInDisplayedLogicalStructure retVal =  new TooManyLevelsInDisplayedLogicalStructure(aMessage, anAdapter, aNestingLimit, aFinder);
		retVal.announce();
		return retVal;
	}

}
