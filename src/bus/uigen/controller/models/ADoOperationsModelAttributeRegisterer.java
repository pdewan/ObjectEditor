package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.KeyShortCuts;
import bus.uigen.controller.SelectionListener;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;
import bus.uigen.view.SaveAsListener;

public class ADoOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setLabel(AFileOperationsModel.class, uiFrame.FILE_MENU_NAME + "'");
//		ObjectEditor.setAttribute(ADoOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		ObjectEditor.setAttribute(ADoOperationsModel.class, AttributeNames.IS_UNDOABLE, false);	
		//ObjectEditor.setMethodAttribute(ADoOperationsModel.class, "cut", AttributeNames.IMPLICIT_REFRESH, false);
		ObjectEditor.setMethodsVisible(SelectionListener.class,  false);		
		KeyShortCuts.put((char) 127, uiFrame.EDIT_MENU+ '>' + uiFrame.DELETE_COMMAND);
		KeyShortCuts.put(util.misc.Common.control('x'), uiFrame.EDIT_MENU+ '>' + uiFrame.CUT_COMMAND);
		KeyShortCuts.put(util.misc.Common.control('c'), uiFrame.EDIT_MENU+ '>' + uiFrame.COPY_COMMAND);
		KeyShortCuts.put(util.misc.Common.control('v'), uiFrame.EDIT_MENU+ '>' + uiFrame.PASTE_COMMAND);
		KeyShortCuts.put(util.misc.Common.control('i'), uiFrame.EDIT_MENU+ '>' + uiFrame.PASTE_AFTER);
		  return null;
	}

}
