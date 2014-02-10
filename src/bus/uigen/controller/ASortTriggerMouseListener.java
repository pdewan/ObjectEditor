package bus.uigen.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bus.uigen.oadapters.CompositeAdapter;
import bus.uigen.oadapters.ObjectAdapter;

public class ASortTriggerMouseListener extends MouseAdapter implements 
java.io.Serializable{
	ObjectAdapter columnAdapter;
	CompositeAdapter vectorAdapter;
	//WidgetShell widgetShell;
	public ASortTriggerMouseListener(CompositeAdapter theVectorAdapter, ObjectAdapter theColumnAdapter ) {
		vectorAdapter = theVectorAdapter;
		columnAdapter = theColumnAdapter;
		//widgetShell = theWidgetShell;
		
	}
	
	
	public void mouseClicked(MouseEvent e) {
		// System.out.println("Mouse clicked");

	}

	

	public void mousePressed(MouseEvent e) {
		

		if (checkMask(e, e.BUTTON1_MASK) ) {
			// do not know why prevSelction was used
			// uiObjectAdapter prevSelection = (uiObjectAdapter)
			// uiSelectionManager.getCurrentSelection();

			CompositeAdapter.sort(vectorAdapter, columnAdapter);

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
