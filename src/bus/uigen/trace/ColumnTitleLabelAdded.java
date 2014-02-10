package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualLabel;

public class ColumnTitleLabelAdded extends ObjectInfo {	
	
	public ColumnTitleLabelAdded(String aMessage, ObjectAdapter anObjectAdapter, VirtualLabel label, Object aFinder) {
		super(aMessage, anObjectAdapter, aFinder);
	}	
	public static ColumnTitleLabelAdded newCase(ObjectAdapter anObjectAdapter, VirtualLabel label, Object aFinder) {
		String aMessage = "Created column title label:" + label + " for object:" + anObjectAdapter.getRealObject();
		ColumnTitleLabelAdded retVal = new ColumnTitleLabelAdded(aMessage, anObjectAdapter, label, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
