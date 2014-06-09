package bus.uigen.models;

import java.util.List;

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

}
