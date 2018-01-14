package util.trace.uigen;

import util.singlestep.AThreadWithSingleStepperAndListBrowser;
import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class ATraceableDisplayAndWaitManagerAR implements ExecutableCommand {

	@Override
	public Object execute(Object theFrame) {
		ObjectEditor.setAttribute(AThreadWithSingleStepperAndListBrowser.class, AttributeNames.DIRECTION, AttributeNames.VERTICAL);
		ObjectEditor.setPropertyAttribute(AThreadWithSingleStepperAndListBrowser.class, "*", AttributeNames.LABELLED, false);
		

		return null;
	}

}
