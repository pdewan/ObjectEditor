package bus.uigen.trace;

import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.widgets.VirtualMenuComponent;
import util.trace.ObjectInfo;

public class MenuGroupCreated extends ObjectInfo {	
	
	public MenuGroupCreated(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static MenuGroupCreated newCase(String theFullName, VirtualMenuComponent theMenu, AMenuDescriptor theMenuDescriptor, Object aFinder) {
		String aMessage = "Menu group:" + theFullName + "created: ";
		MenuGroupCreated retVal = new MenuGroupCreated(aMessage, theFullName, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
