package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;
import bus.uigen.view.SaveAsListener;

public class ASelectionOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setLabel(AFileOperationsModel.class, uiFrame.FILE_MENU_NAME + "'");
		ObjectEditor.setAttribute(ASelectionOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
		ObjectEditor.setAttribute(ASelectionOperationsModel.class, AttributeNames.IS_UNDOABLE, false);	
		return null;
	}

}
