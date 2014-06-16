package bus.uigen.models;

import java.beans.PropertyChangeListener;
import java.util.List;

import util.misc.ThreadSupport;
import util.models.ConsoleModel;
import util.remote.ProcessExecer;
import util.trace.Traceable;

public interface DemoerAndTester extends PropertyChangeListener{
	Boolean test(Boolean aCorrectTranscripts);
	MainClassListLauncher createAndDisplayLauncher();
	MainClassListLauncher demo();


	void executeAll();

	void generateCorrectTranscripts();

	void generateTestTranscripts();

	void loadTraceables();

	List<List<Traceable>> getLocalTraceableLists();

	List<Traceable> getGlobalTraceableList();

	void notifyInteractionTermination();

	void waitForInteractionTermination();
	public List<ConsoleModel> getConsoleModels() ;



	public void setConsoleModels(List<ConsoleModel> consoleModels) ;



	public boolean isTerminated() ;



	public void setTerminated(boolean terminated) ;



	public boolean isInteractive() ;



	public void setInteractive(boolean interactive) ;


	public MainClassListLauncher getLauncher() ;



	public void setLauncher(MainClassListLauncher launcher) ;


	
	
	void terminate();
	

}