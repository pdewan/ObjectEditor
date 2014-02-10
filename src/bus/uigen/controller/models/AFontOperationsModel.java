package bus.uigen.controller.models;

import javax.swing.JSplitPane;

import util.annotations.Visible;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.controller.Selectable;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.ForwardBackwardListener;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AFontOperationsModel implements FrameModel {
	uiFrame frame;
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {
		frame = theFrame;
	}	
	public void fontName(String fontName) {
		
	}
	
	
	

}
