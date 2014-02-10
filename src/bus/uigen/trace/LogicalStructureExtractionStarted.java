package bus.uigen.trace;

import util.trace.ObjectInfo;

public class LogicalStructureExtractionStarted extends MajorStepInfo {	
	
	public LogicalStructureExtractionStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static LogicalStructureExtractionStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Extracting logical tree structure of: " + anObject;
		LogicalStructureExtractionStarted retVal = new LogicalStructureExtractionStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
