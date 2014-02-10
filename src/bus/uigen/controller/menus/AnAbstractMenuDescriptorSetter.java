package bus.uigen.controller.menus;

import bus.uigen.uiFrame;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.ADoOperationsModel;
import bus.uigen.controller.models.AFileOperationsModel;
import bus.uigen.controller.models.AMiscEditOperationsModel;
import bus.uigen.controller.models.ANewEditorOperationsModel;
import bus.uigen.controller.models.ARefreshOperationsModel;
import bus.uigen.controller.models.ASelectionOperationsModel;
import bus.uigen.controller.models.AWindowOperationsModel;
import bus.uigen.controller.models.AnElideOperationsModel;
import bus.uigen.controller.models.AnUndoRedoModel;
import bus.uigen.controller.models.FrameModel;
import bus.uigen.reflect.ClassProxy;

public abstract class AnAbstractMenuDescriptorSetter implements MenuDescriptorSetter{
	String[] menuOrder ;
    String[] lineBelow;

	FrameModel[] menuModels;
	public void init (AMenuDescriptor menuDescriptor, uiFrame theFrame) {
		 menuDescriptor.setAttribute(menuName(), AttributeNames.POSITION, menuPosition());
		 //menuDescriptor.setAttribute(uiFrame.FILE_MENU_NAME + "'" + uiFrame.MENU_NESTING_DELIMITER + uiFrame.NEW_OBJECT_MENU_NAME, AttributeNames.POSITION, 0);
		 menuDescriptor.setSubMenuOrder(menuName() , menuOrder());
		 menuDescriptor.putLineBelow(menuName(), lineBelow());
		 //menuDescriptor.setMenuModels(menuName(), menuModels());
		 menuDescriptor.setMenuModels(menuName(), menuModelClasses(), theFrame);
	}
	abstract int  menuPosition() ;
	abstract String menuName() ;
	//abstract FrameModel[] menuModels();
	abstract ClassProxy[] menuModelClasses();
	
	abstract String[] menuOrder() ;
	abstract String[] lineBelow();

}
