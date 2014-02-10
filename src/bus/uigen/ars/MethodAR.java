package bus.uigen.ars;

import java.lang.reflect.Method;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class MethodAR implements ExecutableCommand{
	public Object execute(Object theFrame) {
		ObjectEditor.setAttribute(Method.class, AttributeNames.DISPLAY_TO_STRING, true);
		return null;
	}


}
