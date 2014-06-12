package bus.uigen.models;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import bus.uigen.trace.TraceUtility;
import util.models.ConsoleModel;
import util.trace.Traceable;

public abstract class ADemoer implements Demoer {
//	protected boolean aliceJoined, bobJoined, cathyJoined;
//	protected boolean aliceCorrect, bobCorrect, cathyCorrect;
//	protected ConsoleModel aliceConsole, bobConsole, cathyConsole, sessionManagerConsole;
////	protected List<ProcessExecer> processExecers;
	protected List<ConsoleModel> consoleModels;
	protected List<List<Traceable>> localTraceableLists;
	protected List<Traceable> globalTraceableList;
	boolean terminated;
//	protected boolean inputOver;
//	protected String finalOutput;

	protected MainClassListLauncher launcher;
//	protected String[] poem = {
//			"The woods are lovely dark and deep",			 
//			"But I have promises to keep",  			 
//			"And miles to go before I sleep"
//	};
	
//	public ADemoer() {
////		computeFinalHistory();
//	}
	
//	 void computeFinalHistory() {
////		History aHistory = new AHistory();
////		aHistory.add(0, poem[0]);
////		aHistory.add(1, poem[1]);
////		aHistory.add(2, poem[2]);
////		finalOutput = AnEchoerInteractor.toString(aHistory);
//
//	}
	
	public ADemoer() {
		
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


	
	
	


}
