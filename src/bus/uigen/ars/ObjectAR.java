package bus.uigen.ars;

import bus.uigen.ObjectEditor;
import bus.uigen.undo.ExecutableCommand;

public class ObjectAR implements ExecutableCommand{
	public Object execute(Object theFrame) {
		ObjectEditor.setPropertyVisible(Object.class, "Class", false);
		return null;
	}


}
