package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;
import bus.uigen.view.ForwardBackwardListener;
import bus.uigen.view.SaveAsListener;

public class AToolkitSelectionModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setLabel(AToolkitSelectionModel.class, uiFrame.VIEW_MENU_NAME + uiFrame.MENU_NESTING_DELIMITER + uiFrame.TOOLKIT_SELECTION_MENU_NAME);
		ObjectEditor.setLabel(AToolkitSelectionModel.class, uiFrame.CUSTOMIZE_MENU + uiFrame.MENU_NESTING_DELIMITER + uiFrame.TOOLKIT_SELECTION_MENU_NAME);
//		ObjectEditor.setAttribute(AToolkitSelectionModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		ObjectEditor.setAttribute(AToolkitSelectionModel.class, AttributeNames.IS_UNDOABLE, false);	
		//ObjectEditor.setMethodAttribute(AToolkitSelectionModel.class, "AWT", AttributeNames.MENU_NAME, uiFrame.TOOLKIT_SELECTION_MENU_NAME);
		  ObjectEditor.setMethodAttribute(AToolkitSelectionModel.class, "AWT", AttributeNames.LABEL, uiFrame.AWT);
		  //ObjectEditor.setMethodAttribute(AToolkitSelectionModel.class, "swing", AttributeNames.MENU_NAME, uiFrame.TOOLKIT_SELECTION_MENU_NAME);
		  ObjectEditor.setMethodAttribute(AToolkitSelectionModel.class, "swing", AttributeNames.LABEL, uiFrame.SWING);
		//ObjectEditor.setVisible(ForwardBackwardListener.class,  false);
		  return null;
	}

}
