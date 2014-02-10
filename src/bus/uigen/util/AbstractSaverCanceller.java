package bus.uigen.util;

import util.annotations.Visible;
import bus.uigen.uiFrame;
import bus.uigen.widgets.VirtualFrame;

public abstract class AbstractSaverCanceller {
	uiFrame frame;
	public abstract void save();
	public  void cancel() {
		if (frame != null)
		frame.close();
	}
	public void setFrame(uiFrame newVal) {
		frame = newVal;
	}
	@Visible(false)
	public uiFrame getFrame() {
		return frame;
	}
	

}
