package bus.uigen.controller.menus;

import java.util.Hashtable;



import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AnAttributeTable;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.AnUndoRedoModel;
import bus.uigen.controller.models.FrameModel;
import bus.uigen.controller.models.FrameModelRegistry;
import bus.uigen.introspect.AttributeRegistry;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;

public class AMenuDescriptor {
	Hashtable<String, AnAttributeTable> contents = new Hashtable();
	public void setAttribute (String menu, String attr, Object value) {
		AnAttributeTable attributes = contents.get(menu);
		if (attributes == null) {
			attributes = new AnAttributeTable();
			contents.put(menu, attributes);
		}
		attributes.setValue(attr, value);
	}
	public void setAttribute (String[] menus, String attr, Object[] values) {
		if (menus.length != values.length) {
			System.out.println("setAttribute: length menus[] != length values");
			return;
		}
		for (int i = 0; i < menus.length; i++) {
			setAttribute (menus[i], attr, values[i]);
		}				
	}
	public void setSubMenuOrder (String parentMenuFullName, String[] childMenus) {
		for (int i = 0; i < childMenus.length; i++) {
			if (parentMenuFullName == "")
				setAttribute (childMenus[i], AttributeNames.POSITION, i);
			else
				setAttribute (parentMenuFullName + uiFrame.MENU_NESTING_DELIMITER + childMenus[i], AttributeNames.POSITION, i);
		}
	}
	public void putLabelBelow (String parentMenuFullName, String[] childMenus, String label) {
		for (int i = 0; i < childMenus.length; i++) {
			if (parentMenuFullName == "")
				setAttribute (childMenus[i], AttributeNames.LABEL_BELOW, label);
			else
				setAttribute (parentMenuFullName + uiFrame.MENU_NESTING_DELIMITER + childMenus[i], AttributeNames.LABEL_BELOW, label);
		}
	}
	public void putLineBelow (String parentMenuFullName, String[] childMenus) {
		putLabelBelow(parentMenuFullName, childMenus, "-");
	}
	public void setRootMenuOrder (String parentMenuFullName, String[] childMenus) {
		setSubMenuOrder ("", childMenus);
	}
	public Object getAttribute (String menu, String attr) {
		AnAttributeTable attributes = contents.get(menu);
		if (attributes == null) return null;
		return attributes.getValue(attr);
	}
	
	public void setMenuModels (String menuName, FrameModel[] menuModels) {
		for (int i = 0; i < menuModels.length; i++) {
			AttributeRegistry.registerAttributeRegistrer(ACompositeLoggable.getTargetClass(menuModels[i]), null);
			ObjectEditor.setLabel(menuModels[i].getClass(), menuName);
			FrameModelRegistry.putDefault(menuModels[i].getClass().getSimpleName(), menuModels[i]);
			
		}
	}
	public void setMenuModels (String menuName, ClassProxy[] menuModels, uiFrame theFrame) {
		for (int i = 0; i < menuModels.length; i++) {
			//registerAttributeRegistrer(menuModels[i]);
			ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptorAndRegisterAttributes(menuModels[i], theFrame);
			if (cd.getAttribute(AttributeNames.LABEL) == null)
				cd.setAttribute(AttributeNames.LABEL, menuName);		
			
			//ObjectEditor.setLabel(menuModels[i], menuName);
			FrameModelRegistry.putDefault(menuModels[i].getSimpleName(), menuModels[i]);
			
		}
	}
	

}
