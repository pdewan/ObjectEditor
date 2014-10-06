package bus.uigen.models;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class AFileSetterModelAR implements ExecutableCommand{
	public Object execute(Object theFrame) {
		ObjectEditor.setAttribute(AFileSetterModel.class,  AttributeNames.LAYOUT, AttributeNames.BORDER_LAYOUT);
		ObjectEditor.setAttribute(AFileSetterModel.class,  AttributeNames.STRETCHABLE_BY_PARENT, true);


		return null;
	}

}
