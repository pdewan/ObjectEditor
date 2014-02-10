package bus.uigen;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import bus.uigen.misc.OEMisc;
import bus.uigen.oadapters.ObjectAdapter;
//not used anymore, was used in drawEdit in ObjectEditor at one time
public class UIFrameWindowAdapter extends WindowAdapter {
	uiFrame frame;
	public UIFrameWindowAdapter(uiFrame aUIFrame) {
		frame = aUIFrame;
	}
//	public void windowClosing(WindowEvent e) {
//		if (frame.isTopFrame())
//			System.exit(0);
//		else {
//			uiFrameList.removeFrame(frame);
//			frame.setVisible(false);
//			frame.dispose();
//		}
//	}
	public void windowClosing(WindowEvent e) {
		ObjectAdapter topAdapter = frame.getBrowser().getOriginalAdapter();
		boolean autoSave = topAdapter.getAutoSave();
		if (autoSave)
			OEMisc.saveState(topAdapter.getRealObject());
		if (frame.isTopFrame() && frame.exitEnabled)
			System.exit(0);
		else {
			uiFrameList.removeFrame(frame);
			frame.setVisible(false);
			frame.dispose();
		}
	}


}
