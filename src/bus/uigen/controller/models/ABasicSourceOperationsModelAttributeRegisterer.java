package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;

public class ABasicSourceOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		  ObjectEditor.setLabel(ABasicProblemOperationsModel.class, AttributeNames.COMMON_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.PROBLEMS_MENU);
//		  ObjectEditor.setAttribute(ASourceOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		  ObjectEditor.setMethodAttribute(ASourceOperationsModel.class, "allSource", AttributeNames.METHOD_MENU_NAME, AttributeNames.FILE_MENU);
//		  ObjectEditor.setMethodAttribute(ASourceOperationsModel.class, "allSource", AttributeNames.LABEL, uiFrame.ALL_SOURCE_NAME);
		  //ObjectEditor.setAttribute(ACustomizeOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
		  return null;
	}

}
