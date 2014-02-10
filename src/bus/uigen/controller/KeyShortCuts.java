package bus.uigen.controller;

import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.uiFrame;
import bus.uigen.controller.menus.VirtualMethodMenuItem;
import bus.uigen.widgets.VirtualComponent;

public class KeyShortCuts  implements KeyListener, KeyEventPostProcessor {
	Hashtable<String, VirtualMethodMenuItem> commandToAction = new Hashtable();
	//Hashtable<String, Character> commandToshortCut = new Hashtable();
	static Hashtable<Character, String> shortCutToCommand = new Hashtable();
	Vector<VirtualComponent> components = new Vector();
	
	uiFrame frame;
	public KeyShortCuts(uiFrame theFrame) {
		frame = theFrame;
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(this);
		
	}
	public void registerCommand (String theCommand, VirtualMethodMenuItem theMenuItem ) {
		commandToAction.put(theCommand, theMenuItem);
		
	}
	public static void put (char shortCut, String command) {
		shortCutToCommand.put(shortCut, command);
		//commandToshortCut.put(command, shortCut);
	}
	
	public static Enumeration keys() {
		return shortCutToCommand.keys();
	}
	public static String get(char theShortCut) {
		return shortCutToCommand.get(theShortCut);
	}
	public VirtualMethodMenuItem getVirtualMethodMenuItem(char theShortCut) {
		String command = get(theShortCut);
		if (command == null) return null;
		return commandToAction.get(command);
	}
	public void AKeyListener(uiFrame theFrame) {
		frame = theFrame;
	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public boolean execute (char c) {
		VirtualMethodMenuItem menuItem = getVirtualMethodMenuItem(c); 
		if (menuItem == null) return false;
		menuItem.processMethodMenuItem();
		return true;		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		VirtualMethodMenuItem menuItem = getVirtualMethodMenuItem(arg0.getKeyChar()); 
		//System.out.println("Key:" + (int) arg0.getKeyChar());
		if (menuItem == null) return;
		menuItem.processMethodMenuItem();
	}
	public void addKeyListener(VirtualComponent component) {
		KeyListener keyListener;
		if (components.contains(component))
			return;
		component.addKeyListener(this);
		components.add(component);
	}
	
	int lastEventProcessed;
	
	@Override
	public boolean postProcessKeyEvent(KeyEvent aKeyEvent) {
		if (aKeyEvent.getKeyCode() != aKeyEvent.VK_UNDEFINED) return false;
		char keyChar = aKeyEvent.getKeyChar();
		execute(keyChar);
//		lastEventProcessed = aKeyEvent.getID();
//		System.out.println(aKeyEvent.getID());
//		System.out.println("VK_UNDEFINED" + aKeyEvent.VK_UNDEFINED);
//		System.out.println("Key Code:" + aKeyEvent.getKeyCode());
//		System.out.println(aKeyEvent.isControlDown());
//		System.out.println("Key char:" + keyChar);
//		System.out.println("Control:" + Character.CONTROL);
//		System.out.println((int) keyChar);
//		System.out.println(keyChar - Character.CONTROL);
//		System.out.println("Key typed:" + aKeyEvent.KEY_TYPED);
		return false;
	}
	

}
