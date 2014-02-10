package bus.uigen.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import bus.uigen.uiFrame;
import bus.uigen.controller.menus.VirtualMethodMenuItem;

public class AKeyListener implements KeyListener {
	uiFrame frame;
	public void AKeyListener(uiFrame theFrame) {
		frame = theFrame;
	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		VirtualMethodMenuItem menuItem = frame.getKeyShortCuts().getVirtualMethodMenuItem(arg0.getKeyChar()); 
		if (frame == null) return;
		menuItem.processMethodMenuItem();
	}

}
