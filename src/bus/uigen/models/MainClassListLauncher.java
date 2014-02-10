package bus.uigen.models;

import util.models.ListenableVector;
import util.remote.ProcessExecer;

public interface MainClassListLauncher extends /*ListenableVector<Class>,*/ Runnable {
	public void execute(Class element);
	public void terminateChildren() ;
	public void terminateAll() ;
	public void open(Class element);
	public boolean add(Class element);
	boolean add(Class element, String args);

}
