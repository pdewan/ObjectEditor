package bus.uigen;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import util.models.AListenableHashtable;


import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;

public abstract class HierarchicalNameTableTree extends AListenableHashtable {
	//ObjectEditor objectEditor;
	String name;
	int childStart;
	Hashtable<String, HierarchicalNameTableTree> nameToClassNameList = new Hashtable();
	Vector<HierarchicalNameTableTree> childrenPackages = new Vector();

	Vector<String> childrenClasses = new Vector();

	//Vector<String> descendents;
	public HierarchicalNameTableTree(Vector<String> initialList,
			//ObjectEditor theObjectEditor, String theName) {
			 String theName) {
		super();
		// descendents = initialList;
		/*
		 * objectEditor = theObjectEditor; name = theName; childStart =
		 * name.length(); setUserObject(name);
		 */
		init(theName);
		init(initialList);
		/*
		//init(theObjectEditor, theName);
		for (int i = 0; i < initialList.size(); i++) {
			
			put(initialList.get(i));
		}
		putChildren();
		*/
	}
	
	public void init(Vector<String> initialList) {
		//super();
		// descendents = initialList;
		/*
		 * objectEditor = theObjectEditor; name = theName; childStart =
		 * name.length(); setUserObject(name);
		 */
		//init(theName);
		//init(theObjectEditor, theName);
		for (int i = 0; i < initialList.size(); i++) {
			/*
			 * System.out.println ("CNTT " + i);
			 * 
			 * if (i == 319 || i == 387) { int j = i; }
			 */
			put(initialList.get(i));
		}
		putChildren();
	}

	void putChildren() {
		for (int i = 0; i < childrenPackages.size(); i++) {
			childrenPackages.elementAt(i).putChildren();
			super.put(childrenPackages.elementAt(i), "");
		}
		/*
		 * Enumeration elements = nameToClassNameList.elements(); while
		 * (elements.hasMoreElements()) { Object nextElement =
		 * elements.nextElement(); super.put (nextElement, ""); }
		 */
	}
	String longName = "";
	//void init(ObjectEditor theObjectEditor, String theName) {
	void init(String theName) {
		//objectEditor = theObjectEditor;
		name = theName;
		//String longName = "";
		if (!name.equals("")) {
			longName = name.substring(1); // get rid of the delimiter
		}
		if (showShortNameForInternalNodes())
			setUserObject(shortName(longName));
		else
			setUserObject(longName);
		childStart = name.length();
	}

	//public HierarchicalNameTableTree(ObjectEditor theObjectEditor,
	public HierarchicalNameTableTree(
			String theName) {
		super();
		init(theName);
		/*
		 * objectEditor = theObjectEditor; name = theName; setUserObject(name);
		 * childStart = name.length();
		 */
	}
	/*
	public void open(String newClassName) {
		objectEditor.newInstance(newClassName);
	}
	*/

	/*
	 * void createSubPackages() { for (int i = 0; i < descendents.size(); i++) {
	 * String childPackage = getChildPackage(descendents.elementAt(i));
	 * ClassNameList childClassNameList = nameToClassNameList.get(childPackage);
	 * if (childClassNameList == null) { childClassNameList } } }
	 */

	abstract Object getValue(String childName);
	abstract HierarchicalNameTableTree createChildNode(String name);

	public Object put(String childName) {
		String childPackage = getChildPackage((String) childName);
		if (childPackage == null) {

			// String description =
			// ClassDescriptor.getAnnotationString(childName);
			//String description = getValue(childName);
			Object description = getValue(childName);
			String keyForLeafNode = childName;
			if (showShortNameForLeafNodes())
				keyForLeafNode = shortName(childName);
			//return super.put(childName, description);
			return super.put(keyForLeafNode, description);

		}
		HierarchicalNameTableTree childClassNameList = nameToClassNameList
				.get(childPackage);
		if (childClassNameList == null) {
			/*
			childClassNameList = new HierarchicalNameTableTree(objectEditor,
					name + '.' + childPackage);
					*/
			childClassNameList = createChildNode (
					name +  getDelimiter()+ childPackage);
					//name + '.' + childPackage);
			childrenPackages.add(childClassNameList);
			// super.put(childClassNameList, "");
			nameToClassNameList.put(childPackage, childClassNameList);
			//nameToClassNameList.put(shortName(childPackage), childClassNameList);
		}
		return childClassNameList.put(childName);

	}

	/*
	 * char delimiter = '.'; void setDelimiter(char theDelimiter) { delimiter =
	 * theDelimiter; }
	 */
	String shortName (String longName) {
		int index = longName.lastIndexOf(getDelimiter());
		if (index > 0)
			return longName.substring(index+1);
		else
			return longName;
	}
	String longNameOfChild (String shortName) {
		int index = shortName.indexOf(getDelimiter());
		if (index > 0)
			return shortName;
		else
			return longName + getDelimiter() + shortName;
		
	}
	abstract char getDelimiter();
	
	boolean showShortNameForInternalNodes() {
		return true;
	}
	boolean showShortNameForLeafNodes() {
		return true;
	}
	

	String getChildPackage(String childName) {
		// int childEnd = childName.indexOf('.', childStart);
		int childEnd = childName.indexOf(getDelimiter(), childStart);

		if (childEnd != -1) {
			return childName.substring(childStart, childEnd);
		}
		return null;
	}

}
