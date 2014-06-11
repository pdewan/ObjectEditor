package bus.uigen.models;

import java.util.List;

import util.models.ConsoleModel;

public abstract class ADemoer implements Demoer {
//	protected boolean aliceJoined, bobJoined, cathyJoined;
//	protected boolean aliceCorrect, bobCorrect, cathyCorrect;
//	protected ConsoleModel aliceConsole, bobConsole, cathyConsole, sessionManagerConsole;
////	protected List<ProcessExecer> processExecers;
	protected List<ConsoleModel> consoleModels;
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
	
	
	
//	public static boolean clientJoined(PropertyChangeEvent aConsoleModelEvent, String aUserName) {
//		String aText = aConsoleModelEvent.getNewValue().toString();
////		if (!Tracer.isInfo(aText))
////			return false;
//		ClientJoined aClientJoined = null;
//		try {
//		 aClientJoined = ClientJoined.toTraceable(aText);
//		} catch (Exception e) {
//			return false;
//		}
//		if (aClientJoined != null) {
//			return aClientJoined.getUserName().equals(aUserName);
//		}
//		return false;
//	}
	
	
//	public static boolean isOutputLine(PropertyChangeEvent aConsoleModelEvent) {
//		return aConsoleModelEvent.getPropertyName().equals(ConsoleModel.OUTPUT_LINE);
//	}
//	public static boolean isOutput(PropertyChangeEvent aConsoleModelEvent) {
//		return aConsoleModelEvent.getPropertyName().equals("output");
//	}
//	public static boolean remoteEchoOf(PropertyChangeEvent aConsoleModelEvent, String anInput, String aUserName ) {
//		if (!isOutputLine(aConsoleModelEvent)) return false;
//		String aText = aConsoleModelEvent.getNewValue().toString();
//		return aText.contains(AHistoryInCoupler.remoteEcho(anInput, aUserName));
//		
//	}
//	public static String getText(PropertyChangeEvent aConsoleModelEvent) {
//		return aConsoleModelEvent.getNewValue().toString();
//	}
//	public static boolean isInfo(PropertyChangeEvent aConsoleModelEvent) {
//		return Tracer.isInfo(getText(aConsoleModelEvent));
//	}
//	public static boolean isInput(PropertyChangeEvent aConsoleModelEvent) {
//		return aConsoleModelEvent.getPropertyName().equalsIgnoreCase("input");
//	}
//	public static boolean textEquals(PropertyChangeEvent aConsoleModelEvent, String aText) {
//		return aConsoleModelEvent.getNewValue().equals(aText);
//	}
//	
//	public static boolean textContains(PropertyChangeEvent aConsoleModelEvent, String aText) {
//		return aConsoleModelEvent.getNewValue().toString().contains(aText);
//	}
	
	
	
//	void maybeFirstAliceInput() {
//		aliceConsole.setInput(poem[0]);	
//	}
	
	
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
	
	
	
	


}
