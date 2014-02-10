package bus.uigen.controller.models;

import util.misc.Common;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.KeyShortCuts;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;

public class AnUndoRedoModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setLabel(AnUndoRedoModel.class, uiFrame.EDIT_MENU_NAME + "'");
//		ObjectEditor.setAttribute(AnUndoRedoModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		ObjectEditor.setAttribute(AnUndoRedoModel.class, AttributeNames.IS_UNDOABLE, false);	
		ObjectEditor.setMethodsVisible(HistoryUndoerListener.class,  false);
		KeyShortCuts.put(Common.control('z'), "Edit>Undo");
		KeyShortCuts.put(Common.control('y'), "Edit>Redo");
		  //ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "open", "Open..");
		  //ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "load", "Load..");
		  return null;
	}

}
