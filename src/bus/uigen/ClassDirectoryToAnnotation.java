package bus.uigen;

import java.util.Vector;

import bus.uigen.HierarchicalNameTableTree;
import bus.uigen.introspect.AClassDescriptor;

public class ClassDirectoryToAnnotation extends HierarchicalNameTableTree {
	//transient ObjectEditor objectEditor;
	public ClassDirectoryToAnnotation(Vector<String> initialList,
			//ObjectEditor theObjectEditor, 
			String theName) {
			 super (initialList, theName);
			 //objectEditor = theObjectEditor;
			 
				 
			 }
	
	public ClassDirectoryToAnnotation(
			//ObjectEditor theObjectEditor,
	
			String theName) {
		super (theName);
		//objectEditor = theObjectEditor;
	}
	@Override
	HierarchicalNameTableTree createChildNode(String name) {
		// TODO Auto-generated method stub
		//return new ClassDirectoryToAnnotation(objectEditor, name);
		return new ClassDirectoryToAnnotation(name);
	}

	@Override
	char getDelimiter() {
		// TODO Auto-generated method stub
		return '.';
	}

	@Override
	Object getValue(String childName) {
		// TODO Auto-generated method stub
		return AClassDescriptor.getAnnotationString(childName);
	}
	public void open(String newClassName) {
		ObjectEditor.defaultObjectEditor().newInstance(longNameOfChild(newClassName));
	}
	

}
