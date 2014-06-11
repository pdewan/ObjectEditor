package bus.uigen.models;

import java.beans.PropertyChangeListener;
import java.util.List;

import util.models.ConsoleModel;
import util.models.ListenableVector;
import util.remote.ProcessExecer;

public interface MainClassListLauncher extends /*ListenableVector<Class>,*/ Runnable {
	public void execute(Class element);
	public void terminateChildren() ;
	public void terminateAll() ;
	public ProcessExecer open(Class element);
	public boolean add(Class element);
	boolean add(Class element, String args);
	List<ProcessExecer> getProcessExecers();
	ProcessExecer nonInteractiveExecute(Class element);
	String getTranscriptFile();
	void setTranscriptFile(String logFileDirectory);
	void executeAll();
	void executeAll(long aWaitTime);
	List<ConsoleModel> createConsoleModels();
	List<ConsoleModel> getOrCreateConsoleModels();
	void addConsolesPropertyChangeListener(
			PropertyChangeListener aPropertyChangeListener);
	void logConsoles(String aLogDirectory);

}
