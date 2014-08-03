package bus.uigen.ars;

import util.models.AListenableVector;
import util.pipe.AConsoleModel;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class AListenableVectorAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		ObjectEditor.setPropertyAttribute(AListenableVector.class, "*",  AttributeNames.INVISIBLE_IF_NULL, true);

		ObjectEditor.setAttribute(AListenableVector.class, AttributeNames.SHOW_DEBUG_INFO_WITH_TOOL_TIP, false);
		ObjectEditor.setMethodAttribute(AListenableVector.class, "*", AttributeNames.VISIBLE, false);
		ObjectEditor.setMethodAttribute(AListenableVector.class, "add", AttributeNames.VISIBLE, false);
	    return null;
	}

}
