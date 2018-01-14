package util.trace.uigen;

import javax.swing.Icon;

import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.widgets.VirtualMenuComponent;
import util.trace.ObjectInfo;

public class ToobarButtonAdded extends ObjectInfo {	
	
	public ToobarButtonAdded(String aMessage, Object anObject, String aLabel, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static ToobarButtonAdded newCase(Object targetObject, String label,   Icon icon, MethodProxy method, String place_toolbar, int pos, Object aFinder) {
		//toolBar.setVisible(true);, Object aFinder) {
		String aMessage = "Toobar button:" + label + "added for object:" + targetObject;
		ToobarButtonAdded retVal = new ToobarButtonAdded(aMessage, targetObject, label, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
