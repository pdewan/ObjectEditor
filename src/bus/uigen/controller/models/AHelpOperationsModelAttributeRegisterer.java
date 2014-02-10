package bus.uigen.controller.models;

import java.net.URL;

import javax.swing.JTable;

import util.misc.Common;
import util.models.AListenableHashtable;
import util.models.AListenableVector;
import bus.uigen.ObjectEditor;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.KeyShortCuts;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;

public class AHelpOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setLabel(AnUndoRedoModel.class, uiFrame.EDIT_MENU_NAME + "'");
		//if (theFrame != null)
			//theFrame.setDefaultAttribute(AttributeNames.TOOLBAR_METHOD, false);
		//ObjectEditor.setAttribute(URL.class, AttributeNames.TOOLBAR_METHOD, false);
		ObjectEditor.setLabel(AHelpOperationsModel.class, uiFrame.HELP_MENU_NAME + uiFrame.MENU_NESTING_DELIMITER + "Documents");
		ObjectEditor.setMethodAttribute(AHelpOperationsModel.class, "summary", AttributeNames.METHOD_MENU_NAME, uiFrame.HELP_MENU_NAME);
		ObjectEditor.setMethodAttribute(AHelpOperationsModel.class, "indexedClasses", AttributeNames.METHOD_MENU_NAME, uiFrame.HELP_MENU_NAME);
		ObjectEditor.setMethodAttribute(AHelpOperationsModel.class, "customizatonAttributes", AttributeNames.METHOD_MENU_NAME, uiFrame.HELP_MENU_NAME);
		ObjectEditor.setMethodAttribute(AHelpOperationsModel.class, "aboutObjectEditor", AttributeNames.METHOD_MENU_NAME, uiFrame.HELP_MENU_NAME);

		ObjectEditor.setAttribute(URL.class, AttributeNames.METHODS_VISIBLE, false);
		ObjectEditor.setAttribute(String.class, AttributeNames.TOOLBAR_METHOD, false);
		ObjectEditor.setAttribute(AHelpOperationsModel.class, AttributeNames.TOOLBAR_METHOD, false);
		//ObjectEditor.setAttribute(AListenableHashtable.class, AttributeNames.HASHTABLE_CHILDREN, AttributeNames.KEYS_ONLY);
		ObjectEditor.setAttribute(ClassNameTable.class, AttributeNames.HASHTABLE_CHILDREN, AttributeNames.KEYS_ONLY);
		//ObjectEditor.setAttribute(AListenableHashtable.class, AttributeNames.LABELLED, false);	
		ObjectEditor.setPreferredWidget(ClassNameList.class, JTable.class);
		ObjectEditor.setPropertyLabelled(ClassNameList.class, AttributeNames.ANY_ELEMENT, false);
		//ObjectEditor.setLabelled(ClassNameTable.class, "key", false);
		//ObjectEditor.setLabelled(ClassNameTable.class, "value", false);
		
		  //ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "open", "Open..");
		  //ObjectEditor.setMethodDisplayName(AFileOperationsModel.class, "load", "Load..");
		  return null;
	}

}
