package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.KeyShortCuts;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;
import bus.uigen.view.SaveAsListener;

public class ARefreshOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setLabel(AFileOperationsModel.class, uiFrame.FILE_MENU_NAME + "'");
//		ObjectEditor.setAttribute(ARefreshOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		ObjectEditor.setAttribute(ARefreshOperationsModel.class, AttributeNames.IS_UNDOABLE, false);	
		KeyShortCuts.put(util.misc.Common.control('r'), AttributeNames.VIEW_MENU+ '>' + uiFrame.REFRESH_COMMAND);
		ObjectEditor.setMethodAttribute(ARefreshOperationsModel.class, "refresh", AttributeNames.IMPLICIT_REFRESH, false);
		  return null;
	}

}
