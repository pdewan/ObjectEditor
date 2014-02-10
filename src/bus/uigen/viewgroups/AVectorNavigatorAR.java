package bus.uigen.viewgroups;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import util.models.ACheckedObject;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;



public class AVectorNavigatorAR implements bus.uigen.undo.ExecutableCommand {
	public Object execute(Object theFrame) {
		// TODO Auto-generated method stub
		
		//ObjectEditor.setMethodAttribute(AVectorNavigator.class, "next", AttributeNames.SHOW_BUTTON, new Boolean(true) );
		//ObjectEditor.setAttribute(AVectorNavigator.class, AttributeNames., new Boolean(true) );
//		ObjectEditor.setMethodAttribute(this, "Next", AttributeNames.COLUMN, 2 );
//		ObjectEditor.setMethodAttribute(this, "Previous", AttributeNames.COLUMN, 1 );
//		ObjectEditor.setMethodAttribute(this, "1", AttributeNames.COLUMN, 0 );
		ObjectEditor.setAttribute(AVectorNavigator.class, AttributeNames.STRETCHABLE_BY_PARENT, true);

		ObjectEditor.setAttribute(AVectorNavigator.class, AttributeNames.LAYOUT, AttributeNames.FLOW_LAYOUT);
		ObjectEditor.setAttribute(AVectorNavigator.class, AttributeNames.ALIGNMENT, FlowLayout.RIGHT);
		ObjectEditor.setMethodAttribute(AVectorNavigator.class, "retarget", AttributeNames.SHOW_BUTTON, false );

		ObjectEditor.setAttribute(AVectorNavigator.class, AttributeNames.SHOW_BUTTON, new Boolean(true) );
		//ObjectEditor.setAttribute(AVectorNavigator.class, AttributeNames.BOUND_PLACEMENT, BorderLayout.SOUTH );

		ObjectEditor.setPropertyAttribute(AVectorNavigator.class, "*", AttributeNames.LABELLED, false);
		ObjectEditor.setPropertyAttribute(ACheckedObject.class, "*" , AttributeNames.LABELLED, false);
		ObjectEditor.setAttribute(AVectorNavigator.class, AttributeNames.COMPONENT_FOREGROUND, Color.BLUE );

		ObjectEditor.setAttribute(AVectorNavigator.class, AttributeNames.SHOW_UNBOUND_BUTTONS, new Boolean(true) );
		return null;
	}	
}
