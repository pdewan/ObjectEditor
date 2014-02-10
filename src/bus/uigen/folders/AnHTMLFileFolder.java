package bus.uigen.folders;import java.io.File;
import java.util.Vector;import java.util.Hashtable;
import java.util.Enumeration;import java.io.IOException;
import java.io.DataInputStream;
import java.io.FileInputStream;public class AnHTMLFileFolder extends ATextFileFolder  {	
	public AnHTMLFileFolder (AnObjectFolder theFolder, File theFile) {
		super(theFolder, theFile);	}	
	static final String JAVA_FILE_SUFFIX = ".java";
	void processElement(String fileName) {		
		File file = new File(fileName);		if (fileName.endsWith(JAVA_FILE_SUFFIX))
			processTextFile(file);		
	}
	
		
}

