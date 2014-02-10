package bus.uigen.trace;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualLabel;

public class ColumnSuffixLabelAdded extends ObjectInfo {	
	
	public ColumnSuffixLabelAdded(String aMessage, ObjectAdapter anObjectAdapter, VirtualLabel label, Object aFinder) {
		super(aMessage, anObjectAdapter, aFinder);
	}	
	public static ColumnSuffixLabelAdded newCase(ObjectAdapter anObjectAdapter, VirtualLabel label, Object aFinder) {
		String aMessage = "Created column suffix label:" + label + " for object:" + anObjectAdapter.getRealObject();
		ColumnSuffixLabelAdded retVal = new ColumnSuffixLabelAdded(aMessage, anObjectAdapter, label, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
