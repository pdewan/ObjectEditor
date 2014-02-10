package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;

public class AnElideOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setLabel(AnElideOperationsModel.class, uiFrame.VIEW_MENU_NAME + "'");
//		ObjectEditor.setAttribute(AnElideOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		ObjectEditor.setAttribute(AnElideOperationsModel.class, AttributeNames.IS_UNDOABLE, false);	
		ObjectEditor.setMethodVisible(AnElideOperationsModel.class, "deepElide", false);
		ObjectEditor.setMethodDisplayName(AnElideOperationsModel.class, "elide", uiFrame.ELIDE_COMMAND);
		ObjectEditor.setMethodDisplayName(AnElideOperationsModel.class, "elideChildren", uiFrame.ELIDE_CHILDREN_COMMAND);
		  ObjectEditor.setMethodDisplayName(AnElideOperationsModel.class, "handles", uiFrame.ELIDE_HANDLE);
		  return null;
	}

}
