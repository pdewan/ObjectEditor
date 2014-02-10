package bus.uigen.folders;import java.io.File;
import java.util.Vector;
import java.util.Enumeration;
import bus.uigen.ObjectEditor;public class AClassFolder  {	Vector<String> classNames = new Vector();	Vector files = new Vector();
	AnObjectFolder folder;
	File f;	String name;	String packageName;
	public AClassFolder (AnObjectFolder theFolder, File theFile) {
		f = theFile;
		folder = theFolder;		packageName = util.misc.Common.fileNameToPackageName(f.getPath());
		readClasses(f);	}	public void readClasses(File directory) {		String[] fileNames = directory.list();		for (int i = 0; i < fileNames.length; i++) {
			processElement(fileNames[i]);		}
	}
	static final String CLASS_FILE_SUFFIX = ".class";
	void processElement(String fileName) {		
		File file = new File(fileName);		if (fileName.endsWith(CLASS_FILE_SUFFIX))
			processClassFile(file);
		
	}
	
	void processClassFile(File file) {
		addElement(file);		
	}	
	public Enumeration elements () {		return classNames.elements();
	}
	public Object newInstance (String className) {		return ObjectEditor.internalNewInstance(className);
	}	public void addElement(File element) {		String shortClassName = className(element.getName());		String fullClassName = shortClassName;		if (!packageName.equals(""))			fullClassName = packageName + "." + shortClassName;		//classNames.addElement(className(element.getName()));		classNames.addElement(fullClassName);		files.addElement(element);
	}	
	public static String className(String fileName) {
		String retVal = null;		if (fileName.endsWith(".class")) {			int i = fileName.lastIndexOf('.');
			retVal = fileName.substring(0, i);//			if (retVal.startsWith("bin.")) {//				retVal = retVal.substring(4, retVal.length());//			}
		}				return retVal;			
	}	/*	public String getName() {
		return name;	}
	*/	
	
		
}

