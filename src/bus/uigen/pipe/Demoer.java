package bus.uigen.pipe;

import java.beans.PropertyChangeListener;
import java.util.List;

import util.misc.ThreadSupport;
import util.pipe.ConsoleModel;
import util.pipe.InputGenerator;
import util.remote.ProcessExecer;
import util.trace.Traceable;

public interface Demoer extends InputGenerator {
//	Boolean executeLoadAndTest(Boolean aGenerateCorrectTranscripts, Boolean aTestAgainstCorrectTranscripts);
//	MainClassListLauncher createAndDisplayLauncher();
//	MainClassListLauncher demo();
//
//
//	void executeAll();
//
//	void generateCorrectTranscripts();
//
//	void generateTestTranscripts();
//
//	void loadTraceables(Boolean aGenerateCorrectTranscripts, Boolean aTestAgainstCorrectTraceables);
//
//	List<List<Traceable>> getLocalTraceableLists();
//
//	List<Traceable> getGlobalTraceableList();
	
	void executionStarted();

	void notifyInteractionTermination();

	void waitForInteractionTermination();
//	public List<ConsoleModel> getConsoleModels() ;



//	public void setConsoleModels(List<ConsoleModel> consoleModels) ;



	public boolean isTerminated() ;



	public void setTerminated(boolean terminated) ;

//	void addProcessName(String aProcessName);
//
//	void processNamesAdded();



//	public boolean isInteractive() ;



//	public void setInteractive(boolean interactive) ;


//	public MainClassListLauncher getLauncher() ;



//	public void setLauncher(MainClassListLauncher launcher) ;


	
	
//	void terminate();
//	List<Traceable> getCorrectGlobalTraceableList();
//	List<List<Traceable>> getCorrectLocalTraceableLists();
//	String getCorrectConsoleTranscriptsFolder();
//	void setCorrectConsoleTranscriptsFolder(
//			String correctConsoleTranscriptsFolder);
//	String getTestConsoleTranscriptsFolder();
//	void setTestConsoleTranscriptsFolder(String testConsoleTranscriptsFolder);
	

}
