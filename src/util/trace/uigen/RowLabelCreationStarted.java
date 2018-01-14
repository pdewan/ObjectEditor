package util.trace.uigen;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;

public class RowLabelCreationStarted extends ObjectInfo {	
	
	public RowLabelCreationStarted(String aMessage, ObjectAdapter anObjectAdapter, Object aFinder) {
		super(aMessage, anObjectAdapter, aFinder);
	}	
	public static RowLabelCreationStarted newCase(ObjectAdapter anObjectAdapter, Object aFinder) {
		String aMessage = "Creating row labels for object:" + anObjectAdapter.getRealObject();
		RowLabelCreationStarted retVal = new RowLabelCreationStarted(aMessage, anObjectAdapter, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
