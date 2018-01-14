package util.trace.uigen;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;

public class LogicalStructureNodeCreated extends ObjectInfo {	
	
	public LogicalStructureNodeCreated(String aMessage, ObjectAdapter anObjectAdapter, Object aFinder) {
		super(aMessage, anObjectAdapter, aFinder);
	}	
	public static LogicalStructureNodeCreated newCase(ObjectAdapter anObjectAdapter, Object aFinder) {
		String aMessage = anObjectAdapter.getClass().getSimpleName() + "  node created for object:" + anObjectAdapter.getRealObject();
		LogicalStructureNodeCreated retVal = new LogicalStructureNodeCreated(aMessage, anObjectAdapter, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
