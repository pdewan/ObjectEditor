package util.trace.uigen;

import util.trace.ObjectInfo;

public class LogicalStructureLeafNodesCollectionStarted extends ObjectInfo {	
	
	public LogicalStructureLeafNodesCollectionStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static LogicalStructureLeafNodesCollectionStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Collecting leaf nodes in tree rooted by: " + anObject;
		LogicalStructureLeafNodesCollectionStarted retVal = new LogicalStructureLeafNodesCollectionStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
