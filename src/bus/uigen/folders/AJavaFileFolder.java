package bus.uigen.folders;import java.io.File;
import java.util.Vector;import java.util.Hashtable;
import java.util.Enumeration;import java.io.IOException;
import java.io.DataInputStream;
import java.io.FileInputStream;public class AJavaFileFolder extends ATextFileFolder  {	
	public AJavaFileFolder (AnObjectFolder theFolder, File theFile) {
		super(theFolder, theFile);	}	
	static final String HTML_FILE_SUFFIX = ".html";	void processElement(String fileName) {		
		File file = new File(fileName);		if (fileName.endsWith(HTML_FILE_SUFFIX))
			processTextFile(file);		
	}
	
		
}

