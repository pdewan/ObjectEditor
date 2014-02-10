package bus.uigen.ars;

import java.awt.TextField;

import bus.uigen.ObjectEditor;
import bus.uigen.attributes.AttributeNames;
import bus.uigen.undo.ExecutableCommand;

public class ExceptionAR implements ExecutableCommand{
	public Object execute(Object theFrame) {
		ObjectEditor.setAttribute(Exception.class, AttributeNames.DISPLAY_TO_STRING, true);
		ObjectEditor.setPropertyAttribute(Exception.class, "stackTrace", AttributeNames.VISIBLE, false);

//		ObjectEditor.setPreferredWidget(Thread.class,  TextField.class);
//
//		ObjectEditor.setPropertyVisible(Thread.class, "ThreadGroup", false);
//
//		ObjectEditor.setPropertyVisible(Thread.class, "DefaultUncaughtExceptionHandler", false);
//		ObjectEditor.setPropertyVisible(Thread.class, "ContextClassLoader", false);
//		ObjectEditor.setPropertyVisible(Thread.class, "UncaughtExceptionHandler", false);
//		ObjectEditor.setPropertyVisible(Thread.class, "StackTrace", false);
//		ObjectEditor.setPropertyVisible(Thread.class, "AllStackTraces", false);

		return null;
	}


}
