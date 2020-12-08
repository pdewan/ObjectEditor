package bus.uigen.controller.menus;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import util.trace.Tracer;

import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;

public class MenuDescriptorRegistry {
	static Hashtable <String, MenuDescriptorSetter> registry = new Hashtable();
	public static  MenuDescriptorSetter put (String name, MenuDescriptorSetter setter) {
		return registry.put (name, setter);
	}
	public static  MenuDescriptorSetter putDefault (String name, MenuDescriptorSetter setter) {
		if (registry.get(name) == null)
		return registry.put (name, setter);
		else return null;
	}
	public static MenuDescriptorSetter get (String name) {
		return registry.get(name);
	}
	public static MenuDescriptorSetter remove (String name) {
		return registry.remove(name);
	}	
	public static Enumeration<String> keys() {
		return registry.keys();
	}
	public static Enumeration<MenuDescriptorSetter> elements() {
		return registry.elements();
	}
	public static void clear() {
		registry.clear();
	}
	public static void setAll(AMenuDescriptor menuDescriptor, uiFrame theFrame) {
		try {
			Enumeration<MenuDescriptorSetter> elements = elements();
			while (elements.hasMoreElements()) {
				MenuDescriptorSetter setter = elements.nextElement();
				setter.init(menuDescriptor, theFrame);
			}
		} catch (Exception e) {
			System.err.println("MenuDescriptorRegistry: Register All: " + e);
		}
	}
	public static void setMenus(AMenuDescriptor menuDescriptor, uiFrame theFrame, String[] menus) {
		try {
			for (String menu:menus) {
				MenuDescriptorSetter setter = registry.get(menu);
				if (setter == null) {
					Tracer.error("No predefined menu named:" + menu);
				}
				else {
					setter.init(menuDescriptor, theFrame);
				}
			}
			
		} catch (Exception e) {
			System.err.println("MenuDescriptorRegistry: Register All: " + e);
		}
	}
//	public static void setCommon(AMenuDescriptor menuDescriptor, uiFrame theFrame) {
//		try {
//			MenuDescriptorSetter setter = registry.get(AttributeNames.COMMON_MENU);
//			setter.init(menuDescriptor, theFrame);
//		} catch (Exception e) {
//			System.err.println("MenuDescriptorRegistry: Register All: " + e);
//		}
//	}
	static {
		registerMenuDescriptorSetters();
	}
	public static void registerMenuDescriptorSetters() {
		//if (menuDescriptorSetterRegistered) return;
		putDefault(AttributeNames.FILE_MENU, new AFileMenuDescriptorSetter());
		putDefault(uiFrame.EDIT_MENU, new AnEditMenuDescriptorSetter());
		putDefault(AttributeNames.VIEW_MENU, new AViewMenuDescriptorSetter());
		putDefault(uiFrame.CUSTOMIZE_MENU, new ACustomizeMenuDescriptorSetter());
		putDefault(uiFrame.HELP_MENU_NAME, new AHelpMenuDescriptorSetter());
		putDefault(AttributeNames.COMMON_MENU, new ACommonMenuDescriptorSetter());
		//menuDescriptorSetterRegistered = true;
		
	}
}
