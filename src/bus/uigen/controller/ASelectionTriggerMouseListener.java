package bus.uigen.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bus.uigen.uiFrame;
import bus.uigen.controller.menus.RightMenu;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.WidgetShell;
import bus.uigen.widgets.awt.AWTComponent;

public class ASelectionTriggerMouseListener extends MouseAdapter implements
java.io.Serializable{
	ObjectAdapter adapter;
	//WidgetShell widgetShell;
	public ASelectionTriggerMouseListener(ObjectAdapter theObjectAdapter) {
		adapter = theObjectAdapter;
		//widgetShell = theWidgetShell;
		
	}
	
	
	public void mouseClicked(MouseEvent e) {
		// System.out.println("Mouse clicked");

	}

	

	public void mousePressed(MouseEvent e) {
		
		

		if (checkMask(e, e.BUTTON1_MASK) &&
				(adapter != null) && 
				(adapter.getWidgetAdapter() == null || 
				adapter.getWidgetAdapter().delegateSelectionToWidgetShell())) {
			// do not know why prevSelction was used
			// uiObjectAdapter prevSelection = (uiObjectAdapter)
			// uiSelectionManager.getCurrentSelection();

			// System.out.println("BUTTON1_MASK");
			// listener.uiComponentFocusGained();
			if (checkMask(e, e.CTRL_MASK)) {
				adapter.addSelectionEvent();
			} else if (checkMask(e, e.SHIFT_MASK))
				adapter.extendSelectionEvent();
			else
				adapter.replaceSelectionsEvent();
			

		}
		//maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		//maybeShowPopup(e);
	}

	
	public static boolean checkMask(MouseEvent e, int mask) {
		int modifiers;
		// System.out.println("modifiers" + e.getModifiers() + "mask" + mask
		// +":");
		modifiers = e.getModifiers();
		if (modifiers == 0)
			modifiers = e.BUTTON1_MASK;
		return (modifiers & mask) == mask;
	}


}
