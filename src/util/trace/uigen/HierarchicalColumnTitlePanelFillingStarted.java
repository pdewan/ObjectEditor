package util.trace.uigen;

import util.trace.ObjectInfo;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.VirtualContainer;

public class HierarchicalColumnTitlePanelFillingStarted extends ObjectInfo {	
	
	public HierarchicalColumnTitlePanelFillingStarted(String aMessage, ObjectAdapter anObjectAdapter, VirtualContainer label, Object aFinder) {
		super(aMessage, anObjectAdapter, aFinder);
	}	
	public static HierarchicalColumnTitlePanelFillingStarted newCase(ObjectAdapter anObjectAdapter, VirtualContainer aContainer, Object aFinder) {
		String aMessage = "Filling hierarchical column title panel: " + " for:"+  anObjectAdapter.getRealObject();
		HierarchicalColumnTitlePanelFillingStarted retVal = new HierarchicalColumnTitlePanelFillingStarted(aMessage, anObjectAdapter, aContainer, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
