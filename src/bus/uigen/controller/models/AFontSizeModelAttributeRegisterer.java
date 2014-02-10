package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;

public class AFontSizeModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		  ObjectEditor.setLabel(AFontSizeModel.class, AttributeNames.VIEW_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.FONT_SIZE_MENU_NAME);
//		  ObjectEditor.setAttribute(AFontSizeModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
		  ObjectEditor.setMethodAttribute(AFontSizeModel.class, "demoFontSize", AttributeNames.METHOD_MENU_NAME, AttributeNames.VIEW_MENU);
		  ObjectEditor.setMethodAttribute(AFontSizeModel.class, "demoFontSize", AttributeNames.LABEL, uiFrame.DEMO_FONT_SIZE);
		  ObjectEditor.setMethodAttribute(AFontSizeModel.class, "getFontSize", AttributeNames.VISIBLE, false);
		  //ObjectEditor.setAttribute(ACustomizeOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
		  return null;
	}

}
