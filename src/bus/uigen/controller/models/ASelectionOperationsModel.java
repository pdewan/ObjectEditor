package bus.uigen.controller.models;

import util.annotations.Visible;
import bus.uigen.uiFrame;
import bus.uigen.controller.SelectionManager;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class ASelectionOperationsModel extends AnAbstractOperationsModel implements FrameModel {
//	uiFrame frame;
//	@Visible(false)
//	public void init (uiFrame theFrame, Object theObject) {
//		frame = theFrame;
//	}
	/*
	public void clear() {
		
	}
	*/
	public void selectPeers() {
		SelectionManager.selectColumn();
	}
	public void selectUp() {
		SelectionManager.selectUp();
	}
	public void selectDown() {
		SelectionManager.selectDown();
	}
	public void selectAll() {
		SelectionManager.select(frame.getBrowser().getAdapter());
	}
	
}
