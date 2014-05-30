package bus.uigen.ars;

import java.awt.GridBagConstraints;


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
		
		ObjectEditor.setAttribute(AConsoleModel.class, AttributeNames.LAYOUT, AttributeNames.GRID_BAG_LAYOUT);
		ObjectEditor.setPropertyAttribute(AConsoleModel.class, "*", AttributeNames.STRETCHABLE_BY_PARENT, true);
		ObjectEditor.setPropertyAttribute(AConsoleModel.class, "output", AttributeNames.ADD_ANCHOR_CONSTRAINT, GridBagConstraints.PAGE_END);
//		ObjectEditor.setPropertyAttribute(AMainProjectStepper.class, "Transcript", AttributeNames.ADD_WEIGHT_Y_CONSTRAINT, 1.0);
		ObjectEditor.setPropertyAttribute(AConsoleModel.class, "output",  AttributeNames.ADD_FILL_CONSTRAINT, GridBagConstraints.BOTH);
		ObjectEditor.setPropertyAttribute(AConsoleModel.class, "output",  AttributeNames.STRETCHABLE_BY_PARENT, true);
		ObjectEditor.setPropertyAttribute(AConsoleModel.class, "output",  AttributeNames.ADD_WEIGHT_Y_CONSTRAINT, 1.0);




		  return null;
	}

}
