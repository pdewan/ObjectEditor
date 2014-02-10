package bus.uigen;

import javax.swing.JSplitPane;
import javax.swing.JTable;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;



public class ObjectEditorAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		//ObjectEditor.setPreferredWidget(AListenableHashtable.class, JTable.class);
		//ObjectEditor.setVisible(ObjectEditor.class, "folder", false);
		//ObjectEditor.setVisible(ObjectEditor.class, "currentClassName", false);
		//ObjectEditor.setPreferredWidget(ObjectEditor.class, JSplitPane.class);
		ObjectEditor.setPropertyAttribute(ObjectEditor.class, "currentClassName", AttributeNames.COMPONENT_WIDTH, 250);

		ObjectEditor.setAttribute(ObjectEditor.class, AttributeNames.AUTO_SAVE, true);
		//ObjectEditor.setPreferredWidget(ObjectEditor.class, "classDescription", javax.swing.JTextArea.class);
		//ObjectEditor.setPropertyAttribute(ObjectEditor.class, "classDescription", AttributeNames.COMPONENT_WIDTH,  250);
		//ObjectEditor.setPropertyAttribute(ObjectEditor.class, "classDescription", AttributeNames.COMPONENT_HEIGHT,  150);
		return null;
	}

}
