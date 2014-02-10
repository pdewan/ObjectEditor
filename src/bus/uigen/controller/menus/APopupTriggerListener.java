package bus.uigen.controller.menus;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.widgets.awt.AWTComponent;

public class APopupTriggerListener implements MouseListener {
	ObjectAdapter adapter;
	public APopupTriggerListener(ObjectAdapter theAdapter) {
		adapter = theAdapter;
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		maybeShowPopup(arg0);
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		maybeShowPopup(arg0);
		
	}
	/* public */void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			/*
			Object obj = adapter.getRealObject();
			if (obj == null)
				return;
				*/
			// RightMenu menu = RightMenuCache.getRightMenu(obj.getClass());
			RightMenu menu = RightMenuManager.getRightMenu(adapter);
			if (menu != null) {
				 menu.checkPre();
				//menu.checkPre(obj);
				
				//menu.configure(adapter.getUIFrame().getFrame(), obj);
				menu.getPopup().show(AWTComponent.virtualComponent(e.getComponent()), e.getX(), e.getY());
			}
			// System.out.println("pop up");
			// return true;
		} // else return false;
	}


}
