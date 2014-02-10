package bus.uigen.viewgroups;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class APropertyAndCommandFilterAttributeRegistrer implements ExecutableCommand  {
	
	public Object execute(Object theFrame) {
		ObjectEditor.setAttribute(APropertyAndCommandFilter.class, AttributeNames.ONLY_DYNAMIC_METHODS, new Boolean (true));
		ObjectEditor.setAttribute(APropertyAndCommandFilter.class, AttributeNames.ONLY_DYNAMIC_PROPERTIES, new Boolean (true));
		return true;
	}

}
