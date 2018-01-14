package util.trace.uigen;

import util.trace.ObjectInfo;

public class LogicalStructureLeafNodesCollectionEnded extends ObjectInfo {	
	
	public LogicalStructureLeafNodesCollectionEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static LogicalStructureLeafNodesCollectionEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Finished collecting leaf nodes in tree rooted by: " + anObject;
		LogicalStructureLeafNodesCollectionEnded retVal = new LogicalStructureLeafNodesCollectionEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
