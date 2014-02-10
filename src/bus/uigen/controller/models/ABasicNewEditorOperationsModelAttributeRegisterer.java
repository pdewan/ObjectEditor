package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.menus.AMenuDescriptor;
import bus.uigen.undo.ExecutableCommand;
import bus.uigen.undo.HistoryUndoerListener;
import bus.uigen.view.ForwardBackwardListener;
import bus.uigen.view.SaveAsListener;

public class ABasicNewEditorOperationsModelAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
//		ObjectEditor.setAttribute(ANewEditorOperationsModel.class, AttributeNames.TOOLBAR_METHOD, new Boolean(false));
//		ObjectEditor.setAttribute(ANewEditorOperationsModel.class, AttributeNames.IS_UNDOABLE, false);	
//		ObjectEditor.setMethodsVisible(ForwardBackwardListener.class,  false);
		/*
		if (theFrame != null && theFrame.getFrameKind() == uiFrame.HELP_FRAME) {
			//theFrame.setDefaultAttribute(attributeName, value)
			ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "forward", AttributeNames.TOOLBAR_METHOD, true);
			ObjectEditor.setMethodAttribute(ANewEditorOperationsModel.class, "back", AttributeNames.TOOLBAR_METHOD, true);
		}
		*/
		  return null;
	}

}
