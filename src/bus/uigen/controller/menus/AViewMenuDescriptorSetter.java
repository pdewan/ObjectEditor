package bus.uigen.controller.menus;

import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.AFontSizeModel;
import bus.uigen.controller.models.ANewEditorOperationsModel;
import bus.uigen.controller.models.ARefreshOperationsModel;
import bus.uigen.controller.models.AToolkitSelectionModel;
import bus.uigen.controller.models.AWindowOperationsModel;
import bus.uigen.controller.models.AnElideOperationsModel;
import bus.uigen.controller.models.FrameModel;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.local.AClassProxy;

public class AViewMenuDescriptorSetter extends AnAbstractMenuDescriptorSetter implements MenuDescriptorSetter{
	String[] menuOrder = {uiFrame.REFRESH_COMMAND,
								uiFrame.AUTO_REFRESH_COMMAND,
								uiFrame.AUTO_REFRESH_ALL_COMMAND,
								uiFrame.INCREMENTAL_REFRESH_COMMAND,
								uiFrame.DEMO_FONT_SIZE,
								uiFrame.FONT_SIZE_MENU_NAME,
								//uiFrame.TOOLKIT_SELECTION_MENU_NAME,
								uiFrame.TREE_PANEL_COMMAND,
								uiFrame.DRAW_PANEL_COMMAND,
								uiFrame.MAIN_PANEL_COMMAND,
								uiFrame.TOOLBAR_COMMAND,
								uiFrame.SECONDARY_PANEL_COMMAND,
								//uiFrame.WINDOW_HISTORY_PANEL_COMMAND,
								
								/*
								uiFrame.NEW_FRAME_COMMAND,
								uiFrame.NEW_TABLE_FRAME_COMMAND,
								uiFrame.NEW_TAB_FRAME_COMMAND,
								uiFrame.NEW_DESKTOP_FRAME_COMMAND,
								uiFrame.NEW_TEXT_FRAME_COMMAND,
								uiFrame.REPLACE_FRAME_COMMAND,
								*/
								uiFrame.NEW_SCROLL_PANE_COMMAND,
								uiFrame.NEW_SCROLL_PANE_BOTTOM_COMMAND,	
								uiFrame.DISPLAY_TOPADAPTER_STRUCTURE_COMMAND,
								uiFrame.DISPLAY_COMPLETE_WINDOW_TREE_COMMAND,
								uiFrame.ELIDE_COMMAND,
								uiFrame.ELIDE_CHILDREN_COMMAND,
								uiFrame.ELIDE_HANDLE,
								uiFrame.DEEP_ELIDE_4,
								uiFrame.FORWARD_ADAPTER_NAME,
								uiFrame.BACK_ADAPTER_NAME
								};
	String[] lineBelow = {
			uiFrame.INCREMENTAL_REFRESH_COMMAND,
			uiFrame.FONT_SIZE_MENU_NAME,
			//uiFrame.TOOLKIT_SELECTION_MENU_NAME,
			uiFrame.WINDOW_HISTORY_PANEL_COMMAND,
			uiFrame.NEW_TEXT_FRAME_COMMAND,
			uiFrame.NEW_SCROLL_PANE_BOTTOM_COMMAND,
			uiFrame.DISPLAY_COMPLETE_WINDOW_TREE_COMMAND,
			uiFrame.DEEP_ELIDE_4,
			//uiFrame.ELIDE_HANDLE
			};
	/*
	FrameModel[] menuModels = {
			new ARefreshOperationsModel(),
			new AWindowOperationsModel(),
			new AnElideOperationsModel(),
			new ANewEditorOperationsModel()
			};
			*/
	Class[] menuModelClasses = {
			ARefreshOperationsModel.class,
			AFontSizeModel.class,
			AWindowOperationsModel.class,
			AnElideOperationsModel.class,
			//ANewEditorOperationsModel.class,
			//AToolkitSelectionModel.class
			};

	int menuPosition() {
		return 2;
	}
	String menuName() {
		return AttributeNames.VIEW_MENU /* "'"*/;
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
