package bus.uigen.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.misc.Common;

public class ComputerDefinitionsManager {
//	public static final String FILE_NAME = "data/TimeBuilt.txt";
//	public static final String RESOURCE_FILE_NAME = "/" + FILE_NAME;
	
	static List<ComputerDefinition> computerDefinitions = new ArrayList();
	static boolean computerDefinitionsRead = false;
	
	public static List<ComputerDefinition> getComputerDefinitions() {
		return computerDefinitions;
	}
	
	public static void  setComputerDefinitions(List<ComputerDefinition> newVal) {
		 computerDefinitions = newVal;
	}
	
	static int randomIndex() {
		return Common.random(0, computerDefinitions.size());		
	}
	public static ComputerDefinition getRandomDefinition() {
		return computerDefinitions.get(randomIndex());
	}
	
	public static void readComputerDefinitions() throws IOException  {
		if (computerDefinitionsRead) {
			return;
		}
		BufferedReader dicitionaryFileStream = ComputerDefinitionsGenerator.openDefinitionsTextFile();
		while (true) {
			try {
				String nextLine = dicitionaryFileStream.readLine();
				if (nextLine == null)
					break;
				int tabIndex = nextLine.indexOf('\t');
				String word = nextLine.substring(0, tabIndex);
				String meaning = nextLine.substring(tabIndex+1);
				computerDefinitions.add(new AComputerDefinition(word, meaning));
			} catch (Exception e) {
				break;
			}				
		}
		computerDefinitionsRead = true;
		
	}
//	
//	static {
//		
//		readComputerDefinitions();
//
//	}
	
	public static void main (String[] args) throws IOException {
		readComputerDefinitions();
		
	}
}
