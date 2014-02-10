package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;
import bus.uigen.view.SaveAsListener;

public class AFileOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setLabel(AFileOperationsModel.class, uiFrame.FILE_MENU_NAME + "'");
//		ObjectEditor.setAttribute(AFileOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		ObjectEditor.setAttribute(AFileOperationsModel.class, AttributeNames.IS_UNDOABLE, false);	 
		ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "open", uiFrame.OPEN_FILE_COMMAND);
		  ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "load", uiFrame.LOAD_FILE_COMMAND);
		  ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "saveAs", uiFrame.SAVE_AS_FILE_COMMAND);
		  ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "saveTextAs", uiFrame.SAVE_TEXT_AS_FILE_COMMAND);
		  ObjectEditor.setMethodsVisible(SaveAsListener.class,  false);
		  return null;
	}

}
