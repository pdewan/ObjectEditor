package bus.uigen.folders;import java.io.File;
import java.util.Vector;
import java.util.Enumeration;public class PackageObject implements java.io.Serializable {	Vector classNames = new Vector();
	Vector packages = new Vector();	String name;	File f;	String parentName;
	public PackageObject (File theFile, String theParentName) {
		f = theFile;
		parentName = theParentName;		setFile(f, parentName);			}	
	void setFile(File directory, String parentName) {
		setName(directory, parentName);		String[] fileNames = directory.list();		for (int i = 0; i < fileNames.length; i++) {
			File element = new File(fileNames[i]);
			if (element.isDirectory()) {
				//classNames.addElement(new PackageObject(element, name));
				packages.addElement(new PackageObject(element, name));				
			} else {
								String className = className(fileNames[i]);
								if (className != null) {					/*
				String fullClassName = toFullName(className);
				try {				Class c = Class.forName(fullClassName);				if (c.isInterface() || ObjectEditor.isAbstract(c))				continue;				classNames.addElement(fullClassName);
				} catch (Exception e) {
				}				*/				classNames.addElement(toFullName(className));
				}				
			}				} 		for (Enumeration elements = packages.elements(); elements.hasMoreElements();) {
			/*			
			try {
				String className = (String) elements.nextElement();				Class c = Class.forName(className);				if (c.isInterface() || ObjectEditor.isAbstract(c))					continue;				classNames.addElement(className);
			} catch (Exception e) {
			}			*/						classNames.addElement(elements.nextElement());		}
	}		void setName (File f, String parentName) {		String directoryName = f.getName();		if (directoryName.equals(".")) 			name = "";		else
		   name = toFullName(parentName, directoryName);	}		public String toFullName (String className) {
		return toFullName(name, className);	}
	
	public static String toFullName(String parentName, String shortName) {		if (parentName == null || parentName.equals("") || parentName.equals("."))
			return shortName;
		else return parentName + "." + shortName;							   
	}	
	public static String className(String fileName) {
		String retVal = null;		if (fileName.endsWith(".class")) {			int i = fileName.lastIndexOf('.');
			retVal = fileName.substring(0, i);
		}		return retVal;			
	}	/*	public String getName() {
		return name;	}
	*/	
		public Vector getClassNames() {		return classNames;			}
		
}

