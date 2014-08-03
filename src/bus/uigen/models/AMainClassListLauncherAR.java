package bus.uigen.models;

import javax.swing.JTextArea;

import util.pipe.AConsoleModel;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class AMainClassListLauncherAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		ObjectEditor.setPropertyAttribute(AMainClassListLauncher.class, "element", AttributeNames.COMPONENT_WIDTH, 500);
		
		ObjectEditor.setMethodAttribute(AMainClassListLauncher.class, "*", AttributeNames.VISIBLE, false);
		ObjectEditor.setMethodAttribute(AMainClassListLauncher.class, "execute",  AttributeNames.VISIBLE, true);
		ObjectEditor.setMethodAttribute(AMainClassListLauncher.class, "terminateAll",  AttributeNames.VISIBLE, true);
		ObjectEditor.setMethodAttribute(AMainClassListLauncher.class, "terminateChildren",  AttributeNames.VISIBLE, true);


		  return null;
	}

}
