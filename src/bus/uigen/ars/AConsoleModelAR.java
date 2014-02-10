package bus.uigen.ars;

import javax.swing.JTextArea;

import util.models.AConsoleModel;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class AConsoleModelAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		ObjectEditor.setPropertyAttribute(AConsoleModel.class, "input", AttributeNames.LABELLED, false);
		ObjectEditor.setPropertyAttribute(AConsoleModel.class, "output", AttributeNames.LABELLED, false);
		ObjectEditor.setPropertyAttribute(AConsoleModel.class, "output", AttributeNames.SCROLLED, true);


//		ObjectEditor.setPreferredWidget(StringBuilder.class, JTextArea.class);
		ObjectEditor.setAttribute(StringBuilder.class, AttributeNames.COMPONENT_HEIGHT, 250);


		  return null;
	}

}
