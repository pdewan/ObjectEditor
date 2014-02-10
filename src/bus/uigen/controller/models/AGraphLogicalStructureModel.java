package bus.uigen.controller.models;

import javax.swing.JFrame;

import util.annotations.Explanation;
import bus.uigen.jung.ALogicalStructureDisplayer;
import bus.uigen.oadapters.ObjectAdapter;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AGraphLogicalStructureModel extends AnAbstractOperationsModel implements FrameModel {
//	JFrame jframe;

	@Explanation ("Toggle command: Displays a graph window to shows the entire (textual and graphical) logical object structure graphically, is visible")
	public void graphLogicalStructure() {
//		if (jframe == null) {
		    JFrame jFrame = new JFrame();
			ObjectAdapter topAdapter = frame.getTopAdapter();
			Object topObject = topAdapter.getRealObject();
			ALogicalStructureDisplayer.createLogicalStructureDisplay(topObject, jFrame);
		}
		
		
//	}

	
}
