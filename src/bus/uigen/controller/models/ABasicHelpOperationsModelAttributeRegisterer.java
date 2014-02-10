package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;
import bus.uigen.view.ForwardBackwardListener;
import bus.uigen.view.SaveAsListener;

public class ABasicHelpOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
//		ObjectEditor.setLabel(ABasicHelpOperationsModel.class, AttributeNames.COMMON_MENU + uiFrame.MENU_NESTING_DELIMITER + "Documents");
		ObjectEditor.setLabel(ABasicHelpOperationsModel.class, AttributeNames.COMMON_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.DOCUMENTS_MENU);

		ObjectEditor.setMethodAttribute(ABasicHelpOperationsModel.class, "aboutObjectEditor", AttributeNames.METHOD_MENU_NAME, AttributeNames.COMMON_MENU);

//		ObjectEditor.setAttribute(ABasicNewEditorOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		ObjectEditor.setAttribute(ABasicNewEditorOperationsModel.class, AttributeNames.IS_UNDOABLE, false);	
//		ObjectEditor.setMethodsVisible(ForwardBackwardListener.class,  false);
		
		  return null;
	}

}
