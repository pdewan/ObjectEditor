package bus.uigen.controller.models;

import java.io.Serializable;

import util.annotations.Visible;
import bus.uigen.uiFrame;
import bus.uigen.oadapters.ObjectAdapter;

public interface FrameModel extends Serializable {
	@Visible(false)
	public void init (uiFrame frame, Object theObject, ObjectAdapter theObjectAdapter);

}
