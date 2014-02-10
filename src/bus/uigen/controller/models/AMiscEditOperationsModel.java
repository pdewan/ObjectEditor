package bus.uigen.controller.models;

import java.awt.Frame;

import util.annotations.Visible;

import bus.uigen.ObjectEditor;
import bus.uigen.myLockManager;
import bus.uigen.uiFrame;
import bus.uigen.uiGenerator;
import bus.uigen.attributes.AttributeEditor;
import bus.uigen.controller.MethodParameters;
import bus.uigen.controller.SelectionManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.visitors.UpdateAdapterVisitor;

public class AMiscEditOperationsModel extends AnAbstractOperationsModel implements FrameModel {
//	uiFrame frame;
//	@Visible(false)
//	public void init (uiFrame theFrame, Object theObject) {
//		frame = theFrame;
//	}
	public void update() {
		MiscOperations.update(frame);		
	}
	
	public void settings() {
		MiscOperations.settings();
		
	}
	public void selection() {
		MiscOperations.selection();
		
	}
	
	
}
