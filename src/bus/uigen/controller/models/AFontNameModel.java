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
public class AFontNameModel extends AnAbstractOperationsModel implements FrameModel {
//	uiFrame frame;
	String[] fontNames = {"Times", "SansSerif"};
//	@Visible(false)
//	public void init (uiFrame theFrame, Object theObject) {
//		frame = theFrame;
//	}	
	public String[] getDynamicCommands() {
		return fontNames;
		//return frame.getCustomizeClassNames();
	}
	public void invokeDynamicCommand (String className) {
		
	}	

}
