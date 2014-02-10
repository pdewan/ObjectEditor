package bus.uigen.controller.menus;

import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.ADoOperationsModel;
import bus.uigen.controller.models.AFileOperationsModel;
import bus.uigen.controller.models.AMiscEditOperationsModel;
import bus.uigen.controller.models.ANewEditorOperationsModel;
import bus.uigen.controller.models.ARefreshOperationsModel;
import bus.uigen.controller.models.ASelectionOperationsModel;
import bus.uigen.controller.models.ASourceOperationsModel;
import bus.uigen.controller.models.AWindowOperationsModel;
import bus.uigen.controller.models.AnElideOperationsModel;
import bus.uigen.controller.models.AnUndoRedoModel;
import bus.uigen.controller.models.FrameModel;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;

public class AFileMenuDescriptorSetter extends AnAbstractMenuDescriptorSetter implements MenuDescriptorSetter{
	String[] menuOrder = {
			uiFrame.ALL_SOURCE_NAME,
			uiFrame.SOURCE_MENU,
			AttributeNames.NEW_OBJECT_MENU_NAME,			
			uiFrame.OPEN_FILE_COMMAND,
			uiFrame.SAVE_COMMAND,
			uiFrame.SAVE_AS_FILE_COMMAND,
			//uiFrame.SAVE_TEXT_FILE_COMMAND,
			uiFrame.SAVE_TEXT_AS_FILE_COMMAND,
			uiFrame.LOAD_FILE_COMMAND,
			uiFrame.UPDATE_ALL_COMMAND,
			uiFrame.EXIT_COMMAND
			};
String[] lineBelow = {
		

};
	/*
	FrameModel[] menuModels = {
			new AFileOperationsModel(),
			new ASourceOperationsModel()};
			*/
	Class[] menuModelClasses = {
			AFileOperationsModel.class,
			ASourceOperationsModel.class
			};
	/*
	public void init (AMenuDescriptor menuDescriptor) {
		
		
		 menuDescriptor.setAttribute(menuName(), AttributeNames.POSITION, menuPosition());
		 //menuDescriptor.setAttribute(uiFrame.FILE_MENU_NAME + "'" + uiFrame.MENU_NESTING_DELIMITER + uiFrame.NEW_OBJECT_MENU_NAME, AttributeNames.POSITION, 0);
		 menuDescriptor.setSubMenuOrder(menuName() , menuOrder());
		 menuDescriptor.putLineBelow(menuName(), lineBelow());
		 menuDescriptor.setMenuModels(menuName(), menuModels());
		 
		
	}
	*/
	int menuPosition() {
		return 0;
	}
	String menuName() {
		return AttributeNames.FILE_MENU /*+ "'"*/;
	}
	FrameModel[] menuModels() {
		return menuModels;
	}
	ClassProxy[] menuModelClasses() {
		return AClassProxy.classProxy(menuModelClasses);
	}
	String[] menuOrder() {
		return menuOrder;
	}
	String[] lineBelow() {
		return lineBelow;
	}

}
