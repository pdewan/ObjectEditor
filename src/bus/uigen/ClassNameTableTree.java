package bus.uigen;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import util.models.AListenableHashtable;


import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;

public class ClassNameTableTree extends AListenableHashtable {
	ObjectEditor objectEditor;
	String name;
	int childStart;
	Hashtable<String, ClassNameTableTree> nameToClassNameList = new Hashtable();
	Vector<ClassNameTableTree> childrenPackages = new Vector();
	
	Vector<String> childrenClasses = new Vector();
	//Vector<String> descendents;
	public ClassNameTableTree(Vector<String> initialList, ObjectEditor theObjectEditor, String theName) {
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
			/*
			System.out.println ("CNTT " + i);
			
			if (i == 319 || i == 387) {
				int j = i;
			}
			*/
			put (initialList.get(i));
		}
		putChildren();
	}
	void putChildren() {
		for (int i = 0; i < childrenPackages.size(); i++) {
			childrenPackages.elementAt(i).putChildren();
			super.put(childrenPackages.elementAt(i), "");
		}
		/*
		Enumeration elements = nameToClassNameList.elements();
		while (elements.hasMoreElements()) {
			Object nextElement = elements.nextElement();
			super.put (nextElement, "");
		}
		*/
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
	public ClassNameTableTree(ObjectEditor theObjectEditor, String theName) {
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
	
	public Object put (String childName) {
		String childPackage =  getChildPackage((String) childName);
		if (childPackage == null) {
			String description = AClassDescriptor.getAnnotationString(childName);
			return super.put(childName, description);
			
		}
		ClassNameTableTree childClassNameList = nameToClassNameList.get(childPackage);
		if (childClassNameList == null) {
			
			childClassNameList = new ClassNameTableTree(objectEditor, name +'.' +childPackage );
			childrenPackages.add(childClassNameList);
			//super.put(childClassNameList, "");
			nameToClassNameList.put(childPackage, childClassNameList);
		}
		return childClassNameList.put(childName);
		 
	}
	
	String getChildPackage(String childName) {
		int childEnd = childName.indexOf('.', childStart);
		
		if (childEnd != -1) {
			return childName.substring(childStart, childEnd);
		}
		return null;
	}
	

}
