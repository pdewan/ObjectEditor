package bus.uigen.controller;import bus.uigen.uiFrame;
import java.lang.reflect.*;
import bus.uigen.reflect.MethodProxy;import bus.uigen.undo.CommandCreator;
import bus.uigen.undo.Undoer;
import bus.uigen.undo.Command;
public class MethodInvocation {
	Object parentObject;
	MethodProxy method;
	Object[] parameterValues;
	uiFrame frame;
	public MethodInvocation (uiFrame theFrame, Object theParentObject, MethodProxy theMethod, Object[] theParameterValues) {
		parentObject = theParentObject;
		method = theMethod;
		parameterValues = theParameterValues;
		frame = theFrame;
		
	}
	public Object execute() {
		try {
			Command cmd = CommandCreator.createCommand(null, method, parentObject, parameterValues);
			if (cmd != null)				
				return frame.getUndoer().execute(cmd);
			else			
				//return method.invoke(parentObject, parameterValues);
				return method.invoke(parentObject, parameterValues);
		} catch (Exception e) {
			return null;
		}
	}
	
}