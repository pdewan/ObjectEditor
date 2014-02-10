package bus.uigen.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bus.uigen.uiFrame;
import bus.uigen.controller.menus.RightMenu;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.WidgetShell;
import bus.uigen.widgets.awt.AWTComponent;

public class ADoubleClickMouseListener extends MouseAdapter implements
java.io.Serializable{
	ObjectAdapter adapter;
//	WidgetShell widgetShell;
	uiFrame frame;
	public ADoubleClickMouseListener(ObjectAdapter theObjectAdapter, uiFrame theUIFrame) {
//		adapter = theWidgetShell.getObjectAdapter();
//		widgetShell = theWidgetShell;
		adapter = theObjectAdapter;
		frame = theUIFrame;
		
	}
	uiFrame getUIFrame() {
//		return widgetShell.getUIFrame();
		return frame;
		
	}
	
	public void mouseClicked(MouseEvent e) {
		// System.out.println("Mouse clicked");

		if ((e.getClickCount() == 2)) {
			if (!MethodInvocationManager.invokeDoubleClickMethod(adapter) && // this has side effects
					adapter.getOpenOnDoubleClick() &&
					(adapter.getWidgetAdapter() == null || adapter.getWidgetAdapter().delegateOpenToWidgetShell())
					)	{
				// internalElide();
				// System.out.println("uif =" + uiF);
				getUIFrame().replaceFrame(adapter);
			}
		}

		// if (listener != null)
		// listener.uiComponentFocusGained();
	}

	

	public void mousePressed(MouseEvent e) {
		// System.out.print("Mouse pressed on " + listener);
		
		
	}

	public void mouseReleased(MouseEvent e) {
		//maybeShowPopup(e);
	}

	


}
