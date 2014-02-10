package bus.uigen.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bus.uigen.uiFrame;
import bus.uigen.controller.menus.RightMenu;
import bus.uigen.controller.menus.RightMenuManager;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.view.WidgetShell;
import bus.uigen.widgets.awt.AWTComponent;

public class AGenericWidgetMouseAdapter extends MouseAdapter implements
java.io.Serializable{
	ObjectAdapter adapter;
	WidgetShell widgetShell;
	public AGenericWidgetMouseAdapter(WidgetShell theWidgetShell) {
		adapter = theWidgetShell.getObjectAdapter();
		widgetShell = theWidgetShell;
		
	}
	uiFrame getUIFrame() {
		return widgetShell.getUIFrame();
		
	}
	void expand() {
		widgetShell.expand();
	}
	void elide() {
		widgetShell.elide();
	}
	void toggleElide() {
		widgetShell.toggleElide();
	}
	void hideElideHandle() {
		widgetShell.hideElideHandle();
	}
	void showElideHandle() {
		widgetShell.showElideHandle();
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

	void elideEvent() {

		if (SelectionManager.getCurrentSelection() == adapter)
			expand();
		else if (
		// listener instanceof uiContainerAdapter &&
		!adapter.isLeafAdapter()
				&& adapter != ObjectAdapter.getTopAdapter(adapter))
			elide();

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
			/*
			 * if (uiSelectionManager.getCurrentSelection() == listener)
			 * expand();
			 */
			/*
			 * else if ( //listener instanceof uiContainerAdapter &&
			 * !listener.isLeaf() && listener !=
			 * uiObjectAdapter.getTopAdapter(listener)) elide();
			 */

		}
		//maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		//maybeShowPopup(e);
	}

	/* public */void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			Object obj = adapter.getRealObject();
			if (obj == null)
				return;
			// RightMenu menu = RightMenuCache.getRightMenu(obj.getClass());
			RightMenu menu = RightMenuManager.getRightMenu(adapter);
			if (menu != null) {
				// menu.checkPre();
				menu.checkPre(obj);
				/*
				menu.configure((Frame) (getUIFrame().getFrame()
						.getPhysicalComponent()), obj);
				menu.getPopup().show(e.getComponent(), e.getX(), e.getY());
				*/
				//menu.configure(getUIFrame().getFrame(), obj);
//				menu.configure(getUIFrame(), obj);
				menu.getPopup().show(AWTComponent.virtualComponent(e.getComponent()), e.getX(), e.getY());
			}
			// System.out.println("pop up");
			// return true;
		} // else return false;
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
