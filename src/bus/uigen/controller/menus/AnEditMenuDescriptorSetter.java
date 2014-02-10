package bus.uigen.controller.menus;

import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.ADoOperationsModel;
import bus.uigen.controller.models.AMiscEditOperationsModel;
import bus.uigen.controller.models.ARefreshOperationsModel;
import bus.uigen.controller.models.ASelectionOperationsModel;
import bus.uigen.controller.models.AnUndoRedoModel;
import bus.uigen.controller.models.FrameModel;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;

public class AnEditMenuDescriptorSetter extends AnAbstractMenuDescriptorSetter implements MenuDescriptorSetter{
	String[] menuOrder = {uiFrame.UNDO_COMMAND,
								uiFrame.REDO_COMMAND,
								uiFrame.CUT_COMMAND,
								uiFrame.COPY_COMMAND,
								uiFrame.LINK_COMMAND,
								uiFrame.PASTE_COMMAND,
								uiFrame.PASTE_AFTER,
								uiFrame.DELETE_COMMAND,
								uiFrame.CLEAR_COMMAND,
								uiFrame.SELECT_PEERS_COMMMAND,
								uiFrame.SELECT_UP_COMMAND,
								uiFrame.SELECT_DOWN_COMMAND,
								uiFrame.SELECT_ALL_COMMAND,
								uiFrame.UPDATE_COMMAND,
								uiFrame.SETTINGS_COMMAND,
								uiFrame.SELECTION_COMMAND
								};
	String[] lineBelow = {
			uiFrame.REDO_COMMAND,			
			uiFrame.PASTE_AFTER,
			uiFrame.CLEAR_COMMAND};
	/*
	FrameModel[] menuModels = {
			new AnUndoRedoModel(), 
			new ADoOperationsModel(),
			new ASelectionOperationsModel(),
			new AMiscEditOperationsModel()};
			*/
	Class[] menuModelClasses = {
			AnUndoRedoModel.class, 
			ADoOperationsModel.class,
			ASelectionOperationsModel.class,
			AMiscEditOperationsModel.class};
	/*
	public void init (AMenuDescriptor menuDescriptor) {
		 menuDescriptor.setAttribute(uiFrame.EDIT_MENU_NAME + "'", AttributeNames.POSITION, 1);
		 menuDescriptor.setSubMenuOrder(uiFrame.EDIT_MENU_NAME + "'", menuOrder());
		 menuDescriptor.putLineBelow(uiFrame.EDIT_MENU_NAME + "'", lineBelow());
		 
		 //menuDescriptor.setAttribute(uiFrame.EDIT_MENU_NAME + "'" + uiFrame.MENU_NESTING_DELIMITER + uiFrame.REDO_COMMAND, AttributeNames.LABEL_BELOW, "-");
		 menuDescriptor.setMenuModels(uiFrame.EDIT_MENU_NAME + "'", menuModels());
		 	
	}
	*/
	int menuPosition() {
		return 1;
	}
	String menuName() {
		return uiFrame.EDIT_MENU /*+ "'"*/;
	}
	String[] menuOrder() {
		return menuOrder;
	}
	String[] lineBelow() {
		return lineBelow;
	}
	FrameModel[] menuModels() {
		return menuModels;
	}
	ClassProxy[] menuModelClasses() {
		return AClassProxy.classProxy(menuModelClasses);
	}

}
