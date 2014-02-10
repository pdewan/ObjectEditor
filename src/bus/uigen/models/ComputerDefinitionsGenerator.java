package bus.uigen.models;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ComputerDefinitionsGenerator {
	public static final Class generatedClass = ComputerDefinitions.class;
	public static final String DICTIONARY_FILE = "/data/ComputerDictionary.txt";
	
	public static BufferedReader openDefinitionsTextFile() throws IOException {
		String retVal = "";
//		try {
//			return new BufferedReader( new FileReader(dictionaryFile));
			InputStream dictionaryStream = ComputerDefinitionsGenerator.class.getResourceAsStream(DICTIONARY_FILE);
			return new BufferedReader( new InputStreamReader(dictionaryStream));
//		} catch (Exception e) {
////			e.printStackTrace();
//			return null;
////			System.exit(-1);
////			return null;
//		}
	}

	// need to refactor this so it can share code with BuildTimeManager
	public static void writeComputerDefinitionsInJavaFile() throws IOException {
		BufferedReader   dicitionaryFileStream = openDefinitionsTextFile();
	if (dicitionaryFileStream == null)
		System.exit(-1);		
		String className = generatedClass.getSimpleName();
		String packageName = generatedClass.getPackage().getName();
		String constantName = generatedClass.getFields()[0].getName();			
		String packageDeclaration = "package " + packageName + ";\n";
//		String classHeader = "public interface " + className + " {\n";
		String classHeader = "public interface " + className + " {\n";
//		String constantDeclaration = "	public static final String " + constantName + " = \"" +  time + "\";\n";
//		String test = "\"" + "Testttttt" + "\"";
//
//		String constantDeclaration = "	public static  String[] " + constantName + " = {" + test +  "};\n";
		String constantDeclarationStart = "	public static  String[] " + constantName + " = {" ;
		StringBuilder constantDeclaration = new StringBuilder(constantDeclarationStart);		
		while (true) {
			try {
				String nextLine = dicitionaryFileStream.readLine();
				if (nextLine == null)
					break;
				constantDeclaration.append("\n\t\t\"" + nextLine + "\",");				
			} catch (Exception e) {
				break;
			}				
		}
		constantDeclaration.deleteCharAt(constantDeclaration.length()-1);
		String constantDeclarationEnd =  "\n\t};\n";		
		constantDeclaration.append(constantDeclarationEnd);

		String classEnd = "}";
		String classSource = packageDeclaration + classHeader + constantDeclaration + classEnd;
		String packageFolderName = packageName.replace('.', '/');
		String fileName = null;
		FileWriter writer = null;
		try {
		fileName = "src/" + packageFolderName + "/" + className + ".java";		
	    writer = new FileWriter(fileName);
		} catch (Exception e) {
			try {
				fileName = packageFolderName + "/" + className + ".java";
				writer = new FileWriter(fileName);
			} catch (Exception e2) {
				e.printStackTrace();
			}
		}
		try {
	      writer.write(classSource);
	       writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	
}
	public static void main (String[] args) throws IOException {
		
		writeComputerDefinitionsInJavaFile();
		
	}

}
