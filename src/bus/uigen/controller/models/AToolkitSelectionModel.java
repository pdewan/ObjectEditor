package bus.uigen.controller.models;

import bus.uigen.widgets.VirtualToolkit;
import bus.uigen.widgets.awt.AWTToolkit;
import bus.uigen.widgets.swing.SwingToolkit;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AToolkitSelectionModel extends AnAbstractOperationsModel implements FrameModel {	
//	uiFrame frame;
//	@Override
//	@Visible(false)
//	public void init(uiFrame theFrame, Object theObject) {
//		// TODO Auto-generated method stub
//		frame = theFrame;
//
//	}
	public void AWT() {
		VirtualToolkit.setDefaultToolkit(new AWTToolkit());
	}
	public void swing() {
		VirtualToolkit.setDefaultToolkit(new SwingToolkit());

	}
	

}
