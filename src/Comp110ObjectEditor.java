

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
public class Comp110ObjectEditor {
	public static void main(String args[]) {
		ObjectEditor.setDefaultAttribute(AttributeNames.SEPARATE_THREAD, true);
		//ObjectEditor.setAttribute(AListenableVector.class, AttributeNames.VISIBLE, false);
		//ObjectEditor.setPropertyAttribute(ObjectEditor.class, "currentClassName", AttributeNames.COMPONENT_WIDTH, 250);
		ObjectEditor.edit(new ObjectEditor());
	}	
}
