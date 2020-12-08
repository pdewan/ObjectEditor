package bus.uigen.ars;

import java.awt.GridBagConstraints;



import javax.swing.JTextArea;

import util.models.ATerminalModel;
import util.pipe.AConsoleModel;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class ATerminalModelAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		ObjectEditor.setPropertyAttribute(ATerminalModel.class, "input", AttributeNames.LABELLED, false);
		ObjectEditor.setPropertyAttribute(ATerminalModel.class, "output", AttributeNames.LABELLED, false);
		ObjectEditor.setPropertyAttribute(ATerminalModel.class, "output", AttributeNames.SCROLLED, true);
		ObjectEditor.setPropertyAttribute(ATerminalModel.class, "output", AttributeNames.AUTO_SCROLLED_DOWN, true);


//		ObjectEditor.setPreferredWidget(StringBuilder.class, JTextArea.class);
		ObjectEditor.setAttribute(StringBuilder.class, AttributeNames.COMPONENT_HEIGHT, 250);
		
		ObjectEditor.setAttribute(ATerminalModel.class, AttributeNames.LAYOUT, AttributeNames.GRID_BAG_LAYOUT);
		ObjectEditor.setPropertyAttribute(ATerminalModel.class, "*", AttributeNames.STRETCHABLE_BY_PARENT, true);
		ObjectEditor.setPropertyAttribute(ATerminalModel.class, "output", AttributeNames.ADD_ANCHOR_CONSTRAINT, GridBagConstraints.PAGE_END);
//		ObjectEditor.setPropertyAttribute(AMainProjectStepper.class, "Transcript", AttributeNames.ADD_WEIGHT_Y_CONSTRAINT, 1.0);
		ObjectEditor.setPropertyAttribute(ATerminalModel.class, "output",  AttributeNames.ADD_FILL_CONSTRAINT, GridBagConstraints.BOTH);
		ObjectEditor.setPropertyAttribute(ATerminalModel.class, "output",  AttributeNames.STRETCHABLE_BY_PARENT, true);
		ObjectEditor.setPropertyAttribute(ATerminalModel.class, "output",  AttributeNames.ADD_WEIGHT_Y_CONSTRAINT, 1.0);




		  return null;
	}

}
