package bus.uigen;

import java.util.AbstractList;
import java.util.Vector;

import javax.swing.JTree;

import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.models.AFontSizeModel;
import bus.uigen.undo.ExecutableCommand;
import javax.swing.JTextArea;

import util.models.AListenableVector;


public class ClassNameTreeAR implements ExecutableCommand {
	public Object execute(Object theFrame) {
		ObjectEditor.setPropertyAttribute(ClassNameTree.class, AttributeNames.ANY_ELEMENT, AttributeNames.LABELLED, false);	
  
		  return null;
	}
}
