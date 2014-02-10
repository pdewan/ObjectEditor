package bus.uigen.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bus.uigen.uiFrame;
import bus.uigen.controller.menus.RightMenu;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.WidgetShell;
import bus.uigen.widgets.awt.AWTComponent;

public class AnElideHandleMouseListener extends MouseAdapter implements
java.io.Serializable{
	ObjectAdapter adapter;
	WidgetShell widgetShell;
	public AnElideHandleMouseListener(WidgetShell theWidgetShell) {
		adapter = theWidgetShell.getObjectAdapter();
		widgetShell = theWidgetShell;
		
	}
	uiFrame getUIFrame() {
		return widgetShell.getUIFrame();
		
	}
	
	void toggleElide() {
		widgetShell.toggleElide();
	}
	void hideElideHandle() {
		widgetShell.hideElideHandle();
	}
	/*
	void showElideHandle() {
		widgetShell.showElideHandle();
	}
	*/
	public void mouseClicked(MouseEvent e) {
		// System.out.println("Mouse clicked");


		// if (listener != null)
		// listener.uiComponentFocusGained();
	}

	

	public void mousePressed(MouseEvent e) {
		// System.out.print("Mouse pressed on " + listener);
		
		if (e.getSource() == widgetShell.getElideHandle().getPhysicalComponent()) {
			if (checkMask(e, e.BUTTON1_MASK)) {
				toggleElide();
				// return;
			} else
				// if (checkMask(e, e.SHIFT_MASK)) {
				hideElideHandle();
			/*
			 * container.remove(elideHandle); elideHandleIsVisible = false;
			 * getUIFrame().validate ();
			 */
			// }
			return;

		}

		
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
