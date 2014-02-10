package bus.uigen.controller.models;

import util.annotations.Visible;
import bus.uigen.uiFrame;
import bus.uigen.oadapters.ObjectAdapter;

public class AnAbstractOperationsModel implements FrameModel {
	uiFrame frame;
	Object obj;
	ObjectAdapter objectAdapter;
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {
		frame = theFrame;
		obj = theObject;
		objectAdapter = theObjectAdapter;
	}	
}
