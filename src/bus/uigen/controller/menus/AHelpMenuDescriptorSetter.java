package bus.uigen.controller.menus;

import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.ADoOperationsModel;
import bus.uigen.controller.models.AHelpOperationsModel;
import bus.uigen.controller.models.AMiscEditOperationsModel;
import bus.uigen.controller.models.ARefreshOperationsModel;
import bus.uigen.controller.models.ASelectionOperationsModel;
import bus.uigen.controller.models.AnUndoRedoModel;
import bus.uigen.controller.models.FrameModel;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;

public class AHelpMenuDescriptorSetter extends AnAbstractMenuDescriptorSetter implements MenuDescriptorSetter{
	String[] menuOrder = {uiFrame.ABOUT_COMMAND
								};
	String[] lineBelow = {
			//uiFrame.REDO_COMMAND,			
			//uiFrame.PASTE_AFTER,
			//uiFrame.CLEAR_COMMAND
			};
	/*
	FrameModel[] menuModels = {
			new AnUndoRedoModel(), 
			new ADoOperationsModel(),
			new ASelectionOperationsModel(),
			new AMiscEditOperationsModel()};
			*/
	Class[] menuModelClasses = {
			AHelpOperationsModel.class
			};
	
	int menuPosition() {
		return -1;
	}
	String menuName() {
		return uiFrame.HELP_MENU_NAME;
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
