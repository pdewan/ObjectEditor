package bus.uigen.controller.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import util.trace.Tracer;

public class BuildTimeManager {
	
	public static final String TIME_TXT_FILE_NAME = "data/TimeBuilt.txt";
	public static final String RESOURCE_TIME_TXT_FILE_NAME = "/" + TIME_TXT_FILE_NAME;
	public static final String OUTPUT_TIME_TXT_FILE_NAME = "bin/" + TIME_TXT_FILE_NAME;
	public static final String BINLESS_OUTPUT_TIME_TXT_FILE_NAME =  TIME_TXT_FILE_NAME;



	public static void writeBuildTimeInTextFile() {
		try {
			
			String fileName = BINLESS_OUTPUT_TIME_TXT_FILE_NAME;
			File file = new File(fileName);
			if (!file.exists()) {
//				System.out.println("Could not find:" + fileName);
				fileName = OUTPUT_TIME_TXT_FILE_NAME;
				file = new File(fileName);
				if (!file.exists()) {
					Tracer.error("Could not find file:" + fileName);
				}
			}
			
		
//			ObjectOutputStream f = new ObjectOutputStream(new   FileOutputStream(OUTPUT_TIME_TXT_FILE_NAME));
			ObjectOutputStream f = new ObjectOutputStream(new   FileOutputStream(fileName));

			
//			ObjectOutputStream f = new ObjectOutputStream(BuildTimeManager.class.getResourceAsStream(TIME_TXT_FILE_NAME));

			Date date = new Date(System.currentTimeMillis());
//			f.writeObject(date.toString());
			f.writeUTF(date.toString());
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getBuildTimeInTextFile() {
		try {
//			ObjectInputStream   f = new ObjectInputStream(new FileInputStream(TIME_TXT_FILE_NAME));
			InputStream file = BuildTimeManager.class.getResourceAsStream(RESOURCE_TIME_TXT_FILE_NAME);
			if (file == null) { // source exists
//				File f = new File(TIME_TXT_FILE_NAME);
//				if (f.exists())
				file = new FileInputStream(TIME_TXT_FILE_NAME);
			}
//			ObjectInputStream   f = new ObjectInputStream(BuildTimeManager.class.getResourceAsStream(RESOURCE_TIME_TXT_FILE_NAME));
			ObjectInputStream   f = new ObjectInputStream(file);

			String date = f.readUTF();
			f.close();
			return date;
		} catch (Exception e) {
			return null;
		}
	}
	public static void writeBuildTimeInJavaFile() {
		
			String time = (new Date(System.currentTimeMillis())).toString();
			String className = GeneratedTime.class.getSimpleName();
			String packageName = GeneratedTime.class.getPackage().getName();
			String constantName = GeneratedTime.class.getFields()[0].getName();			
			String packageDeclaration = "package " + packageName + ";\n";
//			String classHeader = "public interface " + className + " {\n";
			String classHeader = "public class " + className + " {\n";
//			String constantDeclaration = "	public static final String " + constantName + " = \"" +  time + "\";\n";

			String constantDeclaration = "	public static  String " + constantName + " = \"" +  time + "\";\n";
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
	public static String getBuildTimeInJavaFile() {
		try {
//		return "The time is unknown because OE cannot assume everyone has Java 1.7";
		return GeneratedTime.BUILT_TIME;
		} catch (Exception e) {
			return "";

		}
	}
	public static void main(String args[]) {
		writeBuildTimeInTextFile();
		writeBuildTimeInJavaFile();
		
	}

}
