package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;

public class FlatTableBuildingStarted extends MajorStepInfo {	
	
	public FlatTableBuildingStarted(String aMessage, ObjectAdapter anObjectAdapter, Object aFinder) {
		super(aMessage, anObjectAdapter, aFinder);
	}	
	public static FlatTableBuildingStarted newCase(ObjectAdapter anObjectAdapter, Object aFinder) {
		String aMessage = "Building flat table for object:" + anObjectAdapter.getRealObject();
		FlatTableBuildingStarted retVal = new FlatTableBuildingStarted(aMessage, anObjectAdapter, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
