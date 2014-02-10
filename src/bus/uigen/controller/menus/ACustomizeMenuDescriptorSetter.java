package bus.uigen.controller.menus;

import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.ACustomizeOperationsModel;
import bus.uigen.controller.models.ANewEditorOperationsModel;
import bus.uigen.controller.models.ARefreshOperationsModel;
import bus.uigen.controller.models.AToolkitSelectionModel;
import bus.uigen.controller.models.AWindowOperationsModel;
import bus.uigen.controller.models.AnElideOperationsModel;
import bus.uigen.controller.models.FrameModel;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;

public class ACustomizeMenuDescriptorSetter extends AnAbstractMenuDescriptorSetter implements MenuDescriptorSetter{
	String[] menuOrder = {
						  uiFrame.SELECTED_COMMAND,
						  uiFrame.BROADCAST,
						  uiFrame.TOOLKIT_SELECTION_MENU_NAME,
						  uiFrame.NEW_FRAME_COMMAND,
							uiFrame.NEW_TABLE_FRAME_COMMAND,
							uiFrame.NEW_TAB_FRAME_COMMAND,
							uiFrame.NEW_DESKTOP_FRAME_COMMAND,
							uiFrame.NEW_TEXT_FRAME_COMMAND,
							uiFrame.REPLACE_FRAME_COMMAND,
							uiFrame.NEW_FRAME_COMMAND_WITHOUT_ATTRIBUTES
							
							
								};
	String[] lineBelow = {
			uiFrame.SELECTED_COMMAND,
			uiFrame.TOOLKIT_SELECTION_MENU_NAME,
			uiFrame.BROADCAST, 
			uiFrame.NEW_FRAME_COMMAND_WITHOUT_ATTRIBUTES
			};
	/*
	FrameModel[] menuModels = {
			new ACustomizeOperationsModel()
			};
			*/
	Class[] menuModelClasses = {
			ACustomizeOperationsModel.class,
			ANewEditorOperationsModel.class,
			AToolkitSelectionModel.class
			};
	/*
	public void init (AMenuDescriptor menuDescriptor) {
		menuDescriptor.setAttribute(uiFrame.CUSTOMIZE_MENU_NAME + "'", AttributeNames.POSITION, 3);
		 menuDescriptor.setMenuModels(uiFrame.CUSTOMIZE_MENU_NAME + "'", menuModels());
		 	
	}
	*/
	int menuPosition() {
		return 3;
	}
	String menuName() {
		return uiFrame.CUSTOMIZE_MENU /*+ "'"*/;
	}
	String[] menuOrder() {
		return menuOrder;
	}
	String[] lineBelow() {
		return lineBelow;
	}
	/*
	FrameModel[] menuModels() {
		return menuModels;
	}
	*/
	ClassProxy[] menuModelClasses() {
		return AClassProxy.classProxy(menuModelClasses);
	}

}
