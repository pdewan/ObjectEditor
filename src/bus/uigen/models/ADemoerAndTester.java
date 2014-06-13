package bus.uigen.models;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import bus.uigen.trace.TraceUtility;
import util.models.ConsoleModel;
import util.trace.Traceable;

public abstract class ADemoerAndTester implements DemoerAndTester {

	protected List<ConsoleModel> consoleModels;
	protected List<List<Traceable>> localTraceableLists;
	protected List<Traceable> globalTraceableList;
	boolean terminated;
	boolean interactive;
	protected MainClassListLauncher launcher;

	


	public ADemoerAndTester() {
		interactive = true;
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
		launcher = MainClassLaunchingUtility.interactiveLaunch(classes);
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
		return CORRECT_CONSOLE_TRANSCRIPTS + "/" + getClass().getSimpleName();
		
	}
	public  String generateTestDirectory() {
		return TEST_CONSOLE_TRANSCRIPTS + "/" + getClass().getSimpleName();		
	}
	@Override
	public void generateCorrectTranscripts() {
		launcher.logConsoles(generateCorrectDirectory());
	}
	@Override
	public void generateTestTranscripts() {
		launcher.logConsoles(generateTestDirectory());
	}
	@Override
	public void loadTraceables () {
		
		if (consoleModels == null || consoleModels.size() == 0)
			return;
		localTraceableLists = new ArrayList();

		for (int index = 0; index < consoleModels.size(); index++) {
			String aLocalTranscriptFile =  consoleModels.get(index).getLocalTranscriptFile();
			List<Traceable> traceableList = TraceUtility.toTraceableList(aLocalTranscriptFile);
			localTraceableLists.add(traceableList);			
		}
		String aGlobalTrancriptFile = consoleModels.get(0).getGlobalTranscriptFile();
		globalTraceableList = TraceUtility.toTraceableList(aGlobalTrancriptFile);
		
	}
	// null method for those who do not want to listen to console input
	public void propertyChange(PropertyChangeEvent aConsoleModelEvent) {	
		
	}

	@Override
	public List<List<Traceable>> getLocalTraceableLists() {
		return localTraceableLists;
	}

	@Override
	public List<Traceable> getGlobalTraceableList() {
		return globalTraceableList;
	}



	@Override
	public MainClassListLauncher demo() {
		createAndDisplayLauncher();
		executeAll();
		return launcher;
	}

	// override this method to work on the transcripts
	public Boolean test(Boolean aCorrectTranscripts) {
		createAndDisplayLauncher();
		if (aCorrectTranscripts)
			generateCorrectTranscripts();
		else
			generateTestTranscripts();
		executeAll();
		waitForInteractionTermination();
		loadTraceables();
		return true; // in general a test should be superclass tests added with subclsas ones
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



	public void setLocalTraceableLists(List<List<Traceable>> localTraceableLists) {
		this.localTraceableLists = localTraceableLists;
	}



	public void setGlobalTraceableList(List<Traceable> globalTraceableList) {
		this.globalTraceableList = globalTraceableList;
	}
	
	public void terminate() {
		launcher.terminateAll();
	}

	
	


}
