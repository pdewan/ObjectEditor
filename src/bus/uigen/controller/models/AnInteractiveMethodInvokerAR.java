package bus.uigen.controller.models;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class AnInteractiveMethodInvokerAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		  ObjectEditor.setAttribute(AnInteractiveMethodInvoker.class, AttributeNames.SHOW_UNBOUND_BUTTONS, new Boolean(true));
		  ObjectEditor.setAttribute(AnInteractiveMethodInvoker.class, AttributeNames.SHOW_BUTTON, new Boolean(true));

		  ObjectEditor.setPropertyAttribute(AnInteractiveMethodInvoker.class, "parameters", AttributeNames.LABELLED, false);
		  ObjectEditor.setPropertyAttribute(AnInteractiveMethodInvoker.class, "parameters", AttributeNames.SHOW_BORDER, false);
		  ObjectEditor.setPropertyAttribute(AnInteractiveMethodInvoker.class, "parameters", AttributeNames.STRETCH_ROWS, true);
//		  ObjectEditor.setPropertyAttribute(AnInteractiveActualParameter.class, "value", AttributeNames.COMPONENT_HEIGHT, 30);

		  /*
		  boolean showResult = (Boolean) AttributeNames.getDefaultOrSystemDefault(AttributeNames.INPLACE_METHOD_RESULT);
		  ObjectEditor.setPropertyAttribute(AnInteractiveMethodInvoker.class, "result", AttributeNames.VISIBLE, showResult);
		  */
		  ObjectEditor.setAttribute(AnInteractiveMethodInvoker.class, AttributeNames.STRETCH_ROWS, new Boolean(false));
		  ObjectEditor.setMethodAttribute(AnInteractiveMethodInvoker.class, "resetAll", AttributeNames.SHOW_BUTTON, false);
		  ObjectEditor.setMethodAttribute(AnInteractiveMethodInvoker.class, "doImplicitInvoke", AttributeNames.SHOW_BUTTON, false);
		  return null;
	}
}
