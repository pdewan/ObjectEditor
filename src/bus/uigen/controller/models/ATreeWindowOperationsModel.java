package bus.uigen.controller.models;

import javax.swing.JSplitPane;

import util.annotations.Explanation;
import util.annotations.Visible;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.controller.Selectable;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.ForwardBackwardListener;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ATreeWindowOperationsModel extends AnAbstractOperationsModel implements FrameModel {
	public boolean preTree() {
		return !frame.isManualMainContainer();
	}
	@Explanation ("Toggle command: Determines if the tree window, which shows the entire (textual and graphical) logical object structure, is visible")
	public void tree() {
		frame.getTopViewManager().treePanel();
	}

	
}
