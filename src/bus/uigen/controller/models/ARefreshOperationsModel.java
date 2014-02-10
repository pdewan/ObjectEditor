package bus.uigen.controller.models;

import java.awt.Cursor;
import java.util.Enumeration;

import util.annotations.Explanation;
import util.annotations.Visible;

import bus.uigen.uiFrame;
import bus.uigen.controller.SelectionManager;
import bus.uigen.editors.ShapesAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.visitors.RecomputeAttributesAdapterVisitor;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ARefreshOperationsModel extends ABasicRefreshOperationsModel implements FrameModel {
//	uiFrame frame;
	//boolean autoRefresh =   true;
	//boolean autoRefreshAll = false;
//	@Visible(false)
//	public void init (uiFrame theFrame, Object theObject) {
//		frame = theFrame;
//	}
//	@util.annotations.Explanation ("Refreshes allobjects in this window without attempting any optimizations.")
//	public void refresh() {
//		/*
//		uiObjectAdapter topAdapter = frame.getBrowser().getDefaultAdapter();
//		if (topAdapter != null)
//			(new RecomputeAttributesAdapterVisitor(topAdapter)).traverse();
//			*/
//		frame.doRefresh();
//		
//		
//	}
	@Explanation ("Toggle command: Determines if method invocation causes an optimized refresh in this wondow")
	public void autoRefresh() {
		frame.setAutoRefresh(!frame.autoRefresh());
		if (!frame.autoRefresh())
			frame.setAutoRefreshAll(false);
	}
	
	@Explanation ("Toggle command: Determines if method invocation causes an optimized refresh in all object editor wondows")
	public void autoRefreshAllFrames() {
		frame.setAutoRefreshAll (!frame.autoRefreshAll());   
		if (frame.autoRefreshAll())
			frame.setAutoRefresh (true);		
	}
	@Explanation ("Toggle command: Determines if setter size and position methods are called on a graphics object when the mouse is dragged or when it is released")
	public void incrementalRefresh() {
		ShapesAdapter.toggleIncremental();  	
	}
			
}
