package bus.uigen.folders;import java.io.File;
import java.util.Vector;import java.util.Hashtable;
import java.util.Enumeration;import util.misc.Common;import bus.uigen.ObjectEditor;public class ATextFileFolder  {	Vector textFileNames = new Vector();	Hashtable files = new Hashtable();
	AnObjectFolder folder;
	File f;	String name;
	public ATextFileFolder (AnObjectFolder theFolder, File theFile) {
		f = theFile;
		folder = theFolder;
		readTextFiles(f);	}	public void readTextFiles(File directory) {		String[] fileNames = directory.list();		for (int i = 0; i < fileNames.length; i++) {
			processElement(fileNames[i]);		}
	}
	static final String TEXT_FILE_SUFFIX = ".txt";
	void processElement(String fileName) {		
		File file = new File(fileName);		if (fileName.endsWith(TEXT_FILE_SUFFIX))
			processTextFile(file);		
	}
	
	void processTextFile(File file) {
		addElement(file);		
	}
	public Enumeration elements () {		return textFileNames.elements();
	}
	public void open (String textFileName) {
		File file = (File) files.get(textFileName);
	    open(file);		//return ObjectEditor.edit(toText(file));;
	}	public static void open (File file) {		ObjectEditor.edit(Common.toText(file));;
	}
	public void addElement(File element) {
		String name = withoutExtension(element.getName());		textFileNames.addElement(name);		files.put(name, element);
	}	
	public static String withoutExtension(String fileName) {
		String retVal = null;		int i = fileName.lastIndexOf('.');
		retVal = fileName.substring(0, i);		return retVal;			
	}	/*	public String getName() {
		return name;	}
	*/	
	
		
}

