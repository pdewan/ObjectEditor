package bus.uigen.folders;import java.io.File;
import java.util.Vector;import java.util.Hashtable;
import java.util.Enumeration;
import bus.uigen.uiGenerator;public class AnObjectFileFolder  {	Vector objectFileNames = new Vector();	Hashtable files = new Hashtable();
	AnObjectFolder folder;
	File f;	String name;
	public AnObjectFileFolder (AnObjectFolder theFolder, File theFile) {
		f = theFile;
		folder = theFolder;
		readObjectFiles(f);	}	public void readObjectFiles(File directory) {		String[] fileNames = directory.list();		for (int i = 0; i < fileNames.length; i++) {
			processElement(fileNames[i]);		}
	}
	static final String OBJECT_FILE_SUFFIX = ".obj";
	void processElement(String fileName) {		
		File file = new File(fileName);		if (fileName.endsWith(OBJECT_FILE_SUFFIX))
			processObjectFile(file);		
	}
	
	void processObjectFile(File file) {
		addElement(file);		
	}
	public Enumeration elements () {		return objectFileNames.elements();
	}
	public Object open (String objectFileName) {
		File file = (File) files.get(objectFileName);		return uiGenerator.generateUIFrameFromFile(file.getAbsolutePath());
	}	public void addElement(File element) {
		String name = withoutExtension(element.getName());		objectFileNames.addElement(name);		files.put(name, element);
	}	
	public static String withoutExtension(String fileName) {
		String retVal = null;		int i = fileName.lastIndexOf('.');
		retVal = fileName.substring(0, i);		return retVal;			
	}	/*	public String getName() {
		return name;	}
	*/	
	
		
}

