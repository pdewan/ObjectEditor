package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;

public class AFrameModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {		
		ObjectEditor.setMethodsVisible(FrameModel.class,  false);
		//ObjectEditor.setAttribute(FrameModel.class, AttributeNames.IS_UNDOABLE, false);		
		return null;
	}

}
