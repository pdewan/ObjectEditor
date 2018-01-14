package util.trace.uigen;

import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.widgets.VirtualMenuComponent;
import util.trace.ObjectInfo;

public class MenuItemCreated extends ObjectInfo {	
	
	public MenuItemCreated(String aMessage, Object theTargetObject, 
			  String methodName, MethodProxy m, MethodDescriptorProxy theMD, Object aFinder) {
		super(aMessage, theTargetObject, aFinder);
	}	
	public static MenuItemCreated newCase (Object theTargetObject, 
			  String methodName, MethodProxy m, MethodDescriptorProxy theMD, Object aFinder) {
		String aMessage = "Menu item:" + methodName + "created for method: " + m + " of object:" + theTargetObject;
		MenuItemCreated retVal = new MenuItemCreated(aMessage, theTargetObject, methodName, m, theMD, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
