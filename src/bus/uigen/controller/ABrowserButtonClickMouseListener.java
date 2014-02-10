package bus.uigen.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bus.uigen.oadapters.ObjectAdapter;

public class ABrowserButtonClickMouseListener extends MouseAdapter {
	ObjectAdapter adapter;
	public ABrowserButtonClickMouseListener(ObjectAdapter theAdapter) {
		adapter = theAdapter;
	}
	public void mouseClicked(MouseEvent e) {
		if (ASelectionTriggerMouseListener.checkMask(e, e.CTRL_MASK))
			adapter.addSelectionEvent();
		//uiComponentFocusGained();
		else if (ASelectionTriggerMouseListener.checkMask(e, e.SHIFT_MASK))
			adapter.extendSelectionEvent();
		else
			adapter.replaceSelectionsEvent();
		
	}

}
