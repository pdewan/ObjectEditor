package bus.uigen.folders;import java.io.File;
import java.util.Vector;
import java.util.Enumeration;public class AnObjectFolder  {	AClassFolder classes;	AnObjectFileFolder objectFiles;	ATextFileFolder textFiles;	AJavaFileFolder javaFiles;	AnHTMLFileFolder htmlFiles;
	Vector<AnObjectFolder> subFolders = new Vector();	String name;	File f;	String parentName;	AnObjectFolder parentFolder;
	public AnObjectFolder (File theFile, String theParentName, AnObjectFolder theParentFolder) {
		f = theFile;
		parentName = theParentName;		parentFolder = theParentFolder;		setFile(f, parentName);			}		public Vector getClassNamesInFolderTree() {		Vector<String> classNames = new Vector();		fillClassNamesInFolderTree(classNames);		return classNames;	}		public void fillClassNamesInFolderTree(Vector<String> classNames) {		Enumeration<String> myClassNames = classes.elements();		while (myClassNames.hasMoreElements()) {						classNames.add(myClassNames.nextElement());					}		for (int i = 0; i < subFolders.size(); i++ )			subFolders.elementAt(i).fillClassNamesInFolderTree(classNames);			}	
	void setFile(File directory, String parentName) {		setName(directory, parentName);		classes = new AClassFolder(this, directory);
		objectFiles = new AnObjectFileFolder(this, directory);		
				String[] fileNames = directory.list();		File[] files = directory.listFiles();
		classes = new AClassFolder(this, directory);
		textFiles = new ATextFileFolder(this, directory);
		javaFiles = new AJavaFileFolder(this, directory);		htmlFiles = new AnHTMLFileFolder(this, directory);		for (int i = 0; i < fileNames.length; i++) {
			//File element = new File(fileNames[i]);			processFolder(fileNames[i], files[i]);
			//processElement(fileNames[i]);					} 		
		
	}
	/*	static final String SOURCE_FILE_SUFFIX = ".java";
	static final String CLASS_FILE_SUFFIX = ".class";	static final String OBJECT_FILE_SUFFIX = ".obj";	static final String TEXT_FILE_SUFFIX = ".txt";
	*/	/*
	void processElement(String fileName) {		File file = new File(fileName);
		if (file.isDirectory())
			processFolder(file);		else if (fileName.endsWith(CLASS_FILE_SUFFIX))
			processClassFile(file);
		else if (fileName.endsWith(OBJECT_FILE_SUFFIX))
			processObjectFile(file);
		else if (fileName.endsWith(TEXT_FILE_SUFFIX))
			processTextFile(file);		else if (fileName.endsWith(SOURCE_FILE_SUFFIX))
			processSourceFile(file);
	}	*/
	void processFolder(String fileName, File file) {		//File file = new File(fileName);		if (file.isDirectory())
		   subFolders.addElement(new AnObjectFolder(file, name, this));		
	}
	void processClassFile(File file) {
		classes.addElement(file);		
	}
	void processObjectFile(File file) {		
	}
	void processSourceFile(File file) {		
	}
	void processTextFile(File file) {		
	}		void processHTMLFile(File file) {			}
	
	public AClassFolder getClasses() {		return classes;
	}
	public AnObjectFileFolder getObjectFiles() {		return objectFiles;
	}
	public ATextFileFolder getTextFiles() {		return textFiles;
	}
	public AJavaFileFolder getSourceFiles() {		return javaFiles;
	}	public AnHTMLFileFolder getHTMLFiles() {		return htmlFiles;	}	/*
	public Vector getSubFolders() {		return subFolders;
	}
	*/
	public Enumeration elements() {		return subFolders.elements();
	}		void setName (File f, String parentName) {
						String directoryName = f.getName();			if (directoryName.equals(".")) 				//name = "";
				name = f.getAbsolutePath();			else
			   //name = toFullName(parentName, directoryName);
				name = directoryName;	}
	
	public void openSource(String name) {		javaFiles.open(name);
	}		public String toFullName (String className) {
		return toFullName(name, className);	}
	
	public static String toFullName(String parentName, String shortName) {		if (parentName == null || parentName.equals("") || parentName.equals("."))
			return shortName;
		else return parentName + "." + shortName;							   
	}	
	public static String className(String fileName) {
		String retVal = null;		if (fileName.endsWith(".class")) {			int i = fileName.lastIndexOf('.');
			retVal = fileName.substring(0, i);
		}		return retVal;			
	}		public String getName() {
		return name;	}
		
	/*	public Vector getClassNames() {		return classNames;			}	*/
		
}

