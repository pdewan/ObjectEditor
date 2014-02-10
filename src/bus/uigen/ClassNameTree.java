package bus.uigen;

import java.util.Hashtable;
import java.util.Vector;

import util.models.AListenableVector;



public class ClassNameTree extends AListenableVector {
	ObjectEditor objectEditor;
	String name;
	int childStart;
	Hashtable<String, ClassNameTree> nameToClassNameList = new Hashtable();
	Vector<ClassNameTree> childrenPackages = new Vector();
	Vector<String> childrenClasses = new Vector();
	//Vector<String> descendents;
	public ClassNameTree(Vector initialList, ObjectEditor theObjectEditor, String theName) {
		super();
		//descendents = initialList;
		/*
		objectEditor = theObjectEditor;
		name = theName;
		childStart = name.length();
		setUserObject(name);
		*/
		init (theObjectEditor, theName);
		for (int i = 0; i < initialList.size(); i++) {
			addElement(initialList.get(i));
		}
	}
	void init (ObjectEditor theObjectEditor, String theName) {
		objectEditor = theObjectEditor;
		name = theName;
		String myName = "";
		if (!name.equals("")) {
			myName = name.substring(1); // get rid of the '.'
		}
		
		setUserObject(myName);
		childStart = name.length();
	}
	public ClassNameTree(ObjectEditor theObjectEditor, String theName) {
		super();
		init (theObjectEditor, theName);
		/*
		objectEditor = theObjectEditor;
		name = theName;
		setUserObject(name);
		childStart = name.length();
		*/
	}
	public void open (String newClassName) {
		objectEditor.newInstance(newClassName);
	}
	/*
	void createSubPackages() {
		for (int i = 0; i < descendents.size(); i++) {
			String childPackage = getChildPackage(descendents.elementAt(i));
			ClassNameList childClassNameList = nameToClassNameList.get(childPackage);
			if (childClassNameList == null) {
				childClassNameList
			}
		}
	}
	*/
	
	public void addElement(Object childName) {
		String childPackage =  getChildPackage((String) childName);
		if (childPackage == null) {
			super.addElement(childName);
			return;
		}
		ClassNameTree childClassNameList = nameToClassNameList.get(childPackage);
		if (childClassNameList == null) {
			
			childClassNameList = new ClassNameTree(objectEditor, name +'.' +childPackage );
			super.addElement(childClassNameList);
			nameToClassNameList.put(childPackage, childClassNameList);
		}
		childClassNameList.addElement(childName);
	}
	
	String getChildPackage(String childName) {
		int childEnd = childName.indexOf('.', childStart);
		
		if (childEnd != -1) {
			return childName.substring(childStart, childEnd);
		}
		return null;
	}
	

}
