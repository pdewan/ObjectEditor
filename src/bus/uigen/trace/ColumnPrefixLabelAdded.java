package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualLabel;

public class ColumnPrefixLabelAdded extends ObjectInfo {	
	
	public ColumnPrefixLabelAdded(String aMessage, ObjectAdapter anObjectAdapter, VirtualLabel label, Object aFinder) {
		super(aMessage, anObjectAdapter, aFinder);
	}	
	public static ColumnPrefixLabelAdded newCase(ObjectAdapter anObjectAdapter, VirtualLabel label, Object aFinder) {
		String aMessage = "Created column prefix label:" + label + " for object:" + anObjectAdapter.getRealObject();
		ColumnPrefixLabelAdded retVal = new ColumnPrefixLabelAdded(aMessage, anObjectAdapter, label, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
