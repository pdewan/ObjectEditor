package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;

public class ACustomizeOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setLabel(AFileOperationsModel.class, uiFrame.FILE_MENU_NAME + "'");
		  //ObjectEditor.setMethodDisplayName(ACustomizeOperationsModel.class, "broadcast", uiFrame.BROADCAST);
		  ObjectEditor.setAttribute(ACustomizeOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
		  return null;
	}

}
