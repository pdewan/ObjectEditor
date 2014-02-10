package bus.uigen.controller.models;

import javax.swing.JFrame;

import util.annotations.Explanation;
import bus.uigen.ObjectEditor;
import bus.uigen.jung.ALogicalStructureDisplayer;
import bus.uigen.oadapters.ObjectAdapter;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AGraphWindowLogicalStructureModel extends AnAbstractOperationsModel implements FrameModel {
//	JFrame jframe;

	@Explanation ("Toggle command: Determines if the graph window, which shows the window structure graphically, is visible")
	public void graphWindowLogicalStructure() {
//		if (jframe == null) {
		    JFrame jFrame = new JFrame();
			ObjectAdapter topAdapter = frame.getTopAdapter();
			Object windowTree = frame.getComponentTree();

			ALogicalStructureDisplayer.createLogicalStructureDisplay(windowTree, jFrame);
		}
		
		
//	}

	
}
