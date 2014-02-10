package bus.uigen;

import java.util.AbstractList;
import java.util.Vector;

import javax.swing.JTree;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.AFontSizeModel;
import bus.uigen.undo.ExecutableCommand;
import javax.swing.JTextArea;

import util.models.AListenableVector;


public class ClassNameListAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		  ObjectEditor.setAttribute(AbstractList.class, AttributeNames.METHODS_VISIBLE, false);
		  ObjectEditor.setAttribute(Vector.class, AttributeNames.METHODS_VISIBLE, false);
		  ObjectEditor.setAttribute(AListenableVector.class, AttributeNames.METHODS_VISIBLE, false);
		  ObjectEditor.setPreferredWidget(ClassNameTree.class, "value", JTextArea.class);
		  //ObjectEditor.setMethodAttribute(ClassNameList.class,AttributeNames.VISIBLE, false);
		  ObjectEditor.setPreferredWidget(ObjectEditor.class, "classNameList", JTree.class);
		  ObjectEditor.setLabelled(ClassNameTree.class, false);		  
		  return null;
	}
}
