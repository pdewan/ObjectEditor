package bus.uigen.controller.menus;

import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.ABasicFileOperationsModel;
import bus.uigen.controller.models.ABasicHelpOperationsModel;
import bus.uigen.controller.models.ABasicNewEditorOperationsModel;
import bus.uigen.controller.models.ABasicRefreshOperationsModel;
import bus.uigen.controller.models.ABasicSourceOperationsModel;
import bus.uigen.controller.models.ABasicProblemOperationsModel;
import bus.uigen.controller.models.ABasicWindowOperationsModel;
import bus.uigen.controller.models.ADemoFontOperationsModel;
import bus.uigen.controller.models.ADoOperationsModel;
import bus.uigen.controller.models.AFileOperationsModel;
import bus.uigen.controller.models.AGraphLogicalStructureModel;
import bus.uigen.controller.models.AGraphWindowLogicalStructureModel;
import bus.uigen.controller.models.AMiscEditOperationsModel;
import bus.uigen.controller.models.ANewEditorOperationsModel;
import bus.uigen.controller.models.ARefreshOperationsModel;
import bus.uigen.controller.models.ASelectionOperationsModel;
import bus.uigen.controller.models.ASourceOperationsModel;
import bus.uigen.controller.models.ATreeWindowOperationsModel;
import bus.uigen.controller.models.AWindowOperationsModel;
import bus.uigen.controller.models.AnElideOperationsModel;
import bus.uigen.controller.models.AnUndoRedoModel;
import bus.uigen.controller.models.FrameModel;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;

public class ACommonMenuDescriptorSetter extends AnAbstractMenuDescriptorSetter implements MenuDescriptorSetter{
	String[] menuOrder = {
			uiFrame.REFRESH_COMMAND,
			uiFrame.TREE_PANEL_COMMAND,
			uiFrame.GRAPH_LOGICAL_STRUCTURE_COMMAND,
			uiFrame.NEW_FRAME_COMMAND,
			uiFrame.DEMO_FONT_SIZE,
			uiFrame.DISPLAY_COMPLETE_WINDOW_TREE_COMMAND,
			uiFrame.GRAPH_WINDOW_LOGICAL_STRUCTURE_COMMAND,
			uiFrame.DONE_COMMAND,
			uiFrame.ABOUT_COMMAND,
			
			};
String[] lineBelow = {
		

};
	/*
	FrameModel[] menuModels = {
			new AFileOperationsModel(),
			new ASourceOperationsModel()};
			*/
	Class[] menuModelClasses = {
			ABasicRefreshOperationsModel.class,
			ATreeWindowOperationsModel.class,
			AGraphLogicalStructureModel.class,
			ABasicNewEditorOperationsModel.class,
			ADemoFontOperationsModel.class,
			ABasicProblemOperationsModel.class,
			ABasicHelpOperationsModel.class,
			ABasicWindowOperationsModel.class,
			AGraphWindowLogicalStructureModel.class,
			ABasicSourceOperationsModel.class,
			ABasicFileOperationsModel.class
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
		return AttributeNames.COMMON_MENU /*+ "'"*/;
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
