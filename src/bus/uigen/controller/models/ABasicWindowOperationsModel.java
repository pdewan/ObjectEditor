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
public class ABasicWindowOperationsModel extends ATreeWindowOperationsModel implements FrameModel {

	
	@Explanation ("Displays, in a separate window, the complete widget structure, including scrollpanes, in this frame")
	public void displayCompleteWidgetTree() {
		ObjectEditor.treeEdit(frame.getComponentTree());
	}

	
}
