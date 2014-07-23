package bus.uigen.models;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bus.uigen.trace.TraceUtility;
import util.misc.Common;
import util.models.AConsoleModel;
import util.models.ConsoleModel;
import util.trace.Traceable;

public abstract class ADemoerAndTester implements DemoerAndTester {

	protected List<ConsoleModel> consoleModels;
	protected List<List<Traceable>> localTraceableLists;
	protected Map <String, List<Traceable>> titleToLocalTraceableList = new HashMap();
	protected Map <String, List<Traceable>> titleToCorrectTraceableList = new HashMap();

	protected List<Traceable> globalTraceableList;
	
	protected List<List<Traceable>> correctLocalTraceableLists;
	protected List<Traceable> correctGlobalTraceableList;
	
	boolean terminated;
	boolean interactive;
	protected MainClassListLauncher launcher;
	String traceDirectory;

	


	public ADemoerAndTester() {
		this(true);
	}
	

	
	public ADemoerAndTester(boolean anInteractive) {
		interactive = anInteractive;
	}
	
	
	@Override
	public  void executeAll()  {
		launcher.executeAll();
	}
	
	protected abstract Class[] composeMainClasses() ;
	
	
	@Override
	public  MainClassListLauncher createAndDisplayLauncher() {	
		Class[] classes = composeMainClasses();

//		launcher = MainClassLaunchingUtility.interactiveLaunch(classes, "DemoerOfIM_Transcript.txt");
		if (interactive)
			launcher = MainClassLaunchingUtility.createInteractiveLauncher(classes);
		else
			launcher = MainClassLaunchingUtility.createLauncher(classes);
		
		consoleModels = launcher.getOrCreateConsoleModels();		


		return launcher;
		
	}
	
	@Override
	public synchronized void notifyInteractionTermination() {
		terminated = true;
		this.notify();
		
	}
	@Override
	public synchronized void waitForInteractionTermination() {
		try {
			while (!terminated)
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static final String CORRECT_CONSOLE_TRANSCRIPTS = "correctTranscripts";
	public static final String TEST_CONSOLE_TRANSCRIPTS = "testTranscripts";
	public  String generateCorrectDirectory() {
//		return CORRECT_CONSOLE_TRANSCRIPTS + "/" + getClass().getSimpleName();
		return CORRECT_CONSOLE_TRANSCRIPTS + "/" + toDirectoryName(getClass());

		
	}
	protected String toDirectoryName(Class aClass) {
		return getClass().getSimpleName();
	}
	public  String generateTestDirectory() {
//		return TEST_CONSOLE_TRANSCRIPTS + "/" + getClass().getSimpleName();		
		return TEST_CONSOLE_TRANSCRIPTS + "/" + toDirectoryName(getClass());		

	}
	@Override
	public void generateCorrectTranscripts() {
		launcher.logConsoles(generateCorrectDirectory());
	}
	@Override
	public void generateTestTranscripts() {
		launcher.logConsoles(generateTestDirectory());
	}
	public void loadTestTraceables () {
		if (consoleModels == null || consoleModels.size() == 0)
			return;
		localTraceableLists = new ArrayList();

		for (int index = 0; index < consoleModels.size(); index++) {
			String aLocalTranscriptFile =  consoleModels.get(index).getLocalTranscriptFile();
			List<Traceable> traceableList = TraceUtility.toTraceableList(aLocalTranscriptFile);
			localTraceableLists.add(traceableList);	
			titleToLocalTraceableList.put(consoleModels.get(index).getTitle(), traceableList);
		}
		String aGlobalTrancriptFile = consoleModels.get(0).getGlobalTranscriptFile();
		globalTraceableList = TraceUtility.toTraceableList(aGlobalTrancriptFile);

	}
	public static List<String> getSortedFiles(String aDirectory) {
		File file = new File(aDirectory);
		String[] arrayChildren = file.list();
		List<String> listChildren = Common.arrayToArrayList(arrayChildren) ;
		Collections.sort(listChildren);
		return listChildren;
		
		
	}
	public void loadCorrectTraceables (String aCorrectDirectory) {
		
		correctLocalTraceableLists = new ArrayList();
		List<String> sortedFiles = getSortedFiles(aCorrectDirectory);
		
		String aGlobalTrancriptFile = AConsoleModel.getGlobalTranscriptFileName(aCorrectDirectory);

		for (int index = 0; index < sortedFiles.size(); index++) {
			String aTranscriptFile =  aCorrectDirectory + "/" + sortedFiles.get(index);
			if (aTranscriptFile.equals(aGlobalTrancriptFile)) continue;
			String aTitleName = AConsoleModel.getTitle(aTranscriptFile);
			List<Traceable> traceableList = TraceUtility.toTraceableList(aTranscriptFile);
			correctLocalTraceableLists.add(traceableList);	
			titleToCorrectTraceableList.put(aTitleName, traceableList);
		}
		correctGlobalTraceableList = TraceUtility.toTraceableList(aGlobalTrancriptFile);

	}

	@Override
	public void loadTraceables (Boolean aGenerateCorrectTranscripts, Boolean aTestAgainstCorrectTraceables) {
		loadTestTraceables();
		if (!aGenerateCorrectTranscripts && aTestAgainstCorrectTraceables) {
			loadCorrectTraceables(generateCorrectDirectory());
		}
	
		
//		if (consoleModels == null || consoleModels.size() == 0)
//			return;
//		localTraceableLists = new ArrayList();
//		correctLocalTraceableLists = new ArrayList();
//
//		for (int index = 0; index < consoleModels.size(); index++) {
//			String aLocalTranscriptFile =  consoleModels.get(index).getLocalTranscriptFile();
//			List<Traceable> traceableList = TraceUtility.toTraceableList(aLocalTranscriptFile);
//			localTraceableLists.add(traceableList);			
//		}
//		String aGlobalTrancriptFile = consoleModels.get(0).getGlobalTranscriptFile();
//		globalTraceableList = TraceUtility.toTraceableList(aGlobalTrancriptFile);
		
	}
	// null method for those who do not want to listen to console input
	public void propertyChange(PropertyChangeEvent aConsoleModelEvent) {	
		
	}

	@Override
	public List<List<Traceable>> getLocalTraceableLists() {
		return localTraceableLists;
	}
	
	@Override
	public List<List<Traceable>> getCorrectLocalTraceableLists() {
		return correctLocalTraceableLists;
	}

	@Override
	public List<Traceable> getCorrectGlobalTraceableList() {
		return correctGlobalTraceableList;
	}

	@Override
	public List<Traceable> getGlobalTraceableList() {
		return globalTraceableList;
	}

	@Override
	public MainClassListLauncher demo() {
		createAndDisplayLauncher();
		executeAll();
		waitForInteractionTermination();
		return launcher;
	}
	
	public Boolean test() {
		return false;
	}
	
	public Boolean testAgainstCorrectTranscripts() {
		return false;
	}
	
	public Boolean test (Boolean aTestAgainstCorrectTranscripts) {
		boolean retVal = test();
		return aTestAgainstCorrectTranscripts?				
				retVal & testAgainstCorrectTranscripts():
				retVal;
	}

	// override this method to work on the transcripts
	public Boolean executeLoadAndTest(Boolean aCorrectTranscripts, Boolean aTestAgainstCorrectTranscripts) {
		createAndDisplayLauncher();
		if (aCorrectTranscripts)
			generateCorrectTranscripts();
		else
			generateTestTranscripts();
		executeAll();
		waitForInteractionTermination();
		loadTraceables(aCorrectTranscripts, aTestAgainstCorrectTranscripts);
		return test(aTestAgainstCorrectTranscripts);
//		return true; // in general a test should be superclass tests added with subclsas ones
	}

	public List<ConsoleModel> getConsoleModels() {
		return consoleModels;
	}



	public void setConsoleModels(List<ConsoleModel> consoleModels) {
		this.consoleModels = consoleModels;
	}



	public boolean isTerminated() {
		return terminated;
	}



	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}



	public boolean isInteractive() {
		return interactive;
	}



	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}


	public MainClassListLauncher getLauncher() {
		return launcher;
	}



	public void setLauncher(MainClassListLauncher launcher) {
		this.launcher = launcher;
	}



	
	
	public void terminate() {
		launcher.terminateAll();
	}

	
	


}
