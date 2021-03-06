package bus.uigen.pipe;

import java.beans.PropertyChangeListener;
import java.util.List;

import util.misc.ThreadSupport;
import util.models.LocalGlobalTranscriptManager;
import util.pipe.ConsoleModel;
import util.remote.ProcessExecer;
import util.trace.Traceable;

public interface TranscriptBasedTester {
//	Boolean executeLoadAndTest(Boolean aGenerateCorrectTranscripts, Boolean aTestAgainstCorrectTranscripts);
//	MainClassListLauncher createAndDisplayLauncher();
//	MainClassListLauncher demo();
//
//
//	void executeAll();

//	void generateCorrectTranscripts();
//
//	void generateTestTranscripts();

	void loadTraceables(Boolean aGenerateCorrectTranscripts, Boolean aTestAgainstCorrectTraceables);

	List<List<Traceable>> getLocalTraceableLists();

	List<Traceable> getGlobalTraceableList();
//
//	void notifyInteractionTermination();
//
//	void waitForInteractionTermination();
//	public List<ConsoleModel> getConsoleModels() ;
//
//
//
//	public void setConsoleModels(List<ConsoleModel> consoleModels) ;



//	public boolean isTerminated() ;
//
//
//
//	public void setTerminated(boolean terminated) ;
//
//
//
//	public boolean isInteractive() ;
//
//
//
//	public void setInteractive(boolean interactive) ;
//
//
//	public MainClassListLauncher getLauncher() ;
//
//
//
//	public void setLauncher(MainClassListLauncher launcher) ;
	
	
//	void terminate();
	List<Traceable> getCorrectGlobalTraceableList();
	List<List<Traceable>> getCorrectLocalTraceableLists();
	String getCorrectConsoleTranscriptsFolder();
	void setCorrectConsoleTranscriptsFolder(
			String correctConsoleTranscriptsFolder);
	String getTestConsoleTranscriptsFolder();
	void setTestConsoleTranscriptsFolder(String testConsoleTranscriptsFolder);
	public List<String> getProcessNames();
	public void setProcessNames(List<String> processNames);
	void addProcessName(String aName);

	void addTranscriptManager(LocalGlobalTranscriptManager aTranscriptManager);

	String getTestDirectory();


	String getCorrectDirectory();

	Boolean test();

	Boolean testAgainstCorrectTranscripts();

	Boolean test(Boolean aTestAgainstCorrectTranscripts);
	public String getCorrectSubFolder() ;
	public void setCorrectSubFolder(String correctSubFolder) ;
	public String getTestSubFolder();
	public void setTestSubFolder(String testSubFolder) ;
	

}
