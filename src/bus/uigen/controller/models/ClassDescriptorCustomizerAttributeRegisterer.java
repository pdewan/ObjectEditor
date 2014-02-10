package bus.uigen.controller.models;

import java.beans.PropertyDescriptor;


import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AnAttributeName;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class ClassDescriptorCustomizerAttributeRegisterer implements ExecutableCommand {
	public Object execute(Object theFrame) {
		ObjectEditor.setVisibleAllProperties(PropertyDescriptor.class, false);
		//ObjectEditor.setPropertyAttribute(ClassDescriptorCustomizer.class, "apd", AttributeNames.VISIBLE, true);
		ObjectEditor.setPropertyAttribute(PropertyDescriptor.class, "name", AttributeNames.VISIBLE, true);
		ObjectEditor.setAttribute(AnAttributeName.class, AttributeNames.COMPONENT_WIDTH, 150);
		ObjectEditor.setAttribute(PropertyDescriptor.class,  AttributeNames.UN_NEST_HT_RECORD, true);
		
		
		return null;
	}

}
