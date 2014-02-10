package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.undo.ExecutableCommand;

public class AnInteractiveActualParameterAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		

		ObjectEditor.setMethodAttribute(AnInteractiveActualParameter.class, "newUserValue", AttributeNames.VISIBLE, false);
		//if ((Boolean)AttributeNames.getDefaultOrSystemDefault(AttributeNames.SHOW_PARAMETER_ELIDE_HANDLE))
		ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.SHOW_ELIDE_HANDLES,
				(Boolean)AttributeNames.getDefaultOrSystemDefault(AttributeNames.SHOW_PARAMETER_ELIDE_HANDLE));
		ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.GRAPHICS_VIEW,
				(Boolean)AttributeNames.getDefaultOrSystemDefault(AttributeNames.SHOW_PARAMETER_GRAPHICS_VIEW));
		
		ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.SHOW_BORDER, false);
		ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.ELIDE_STRING_IS_TOSTRING, true);
		//if ((Boolean)AttributeNames.getDefaultOrSystemDefault(AttributeNames.DEFAULT_EXPAND_PARAMETER_VALUE))
		ObjectEditor.setAttribute(AnInteractiveActualParameter.class, AttributeNames.EXPAND_PRIMITIVE_CHILDREN, 
				true);
		ObjectEditor.setAttribute(AnInteractiveActualParameter.class, AttributeNames.ALLOW_MULTIPLE_EQUAL_REFERENCES, true);

		ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.DEFAULT_EXPANDED, 
			(Boolean)AttributeNames.getDefaultOrSystemDefault(AttributeNames.DEFAULT_EXPAND_PARAMETER_VALUE));
		//else
		/*
		ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "type", AttributeNames.VISIBLE, 
				(Boolean)AttributeNames.getDefaultOrSystemDefault(AttributeNames.SHOW_PARAMETER_TYPE));	
		*/
		ObjectEditor.setAttribute(AnInteractiveActualParameter.class, AttributeNames.SHOW_COLUMN_TITLES, false);
		//ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.LABELLED, false);
		//ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "type", AttributeNames.LABELLED, false);
		//ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.COMPONENT_WIDTH, 50);
		//ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "type", AttributeNames.COMPONENT_WIDTH, 50);
		//ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.LABEL_POSITION, AttributeNames.LABEL_IS_LEFT);
		//ObjectEditor.setAttribute(AnInteractiveMethodInvoker.class, AttributeNames.SHOW_UNBOUND_BUTTONS, new Boolean(true));
		  ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.COMPONENT_HEIGHT, 25);
		  ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "type", AttributeNames.COMPONENT_HEIGHT, 25);
		  ObjectEditor.setAttribute(AnInteractiveActualParameter.class, AttributeNames.USE_NAME_AS_LABEL, true);


		
		return null;
	}

}
