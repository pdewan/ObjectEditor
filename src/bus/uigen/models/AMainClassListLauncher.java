package bus.uigen.models;
import java.util.ArrayList;
import java.util.List;
import util.annotations.Visible;
import util.models.AListenableVector;
import util.models.VectorChangeSupport;
import util.remote.ProcessExecer;
import bus.uigen.misc.OEMisc;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.LIST_PATTERN)
public class AMainClassListLauncher /*extends AListenableVector<Class>*/  implements MainClassListLauncher {
	List<ProcessExecer> executed = new ArrayList();	
	List<Class> mainClasses  = new ArrayList();
	List<String> mainArgs = new ArrayList();
	transient protected VectorChangeSupport<Class> vectorChangeSupport = new VectorChangeSupport(
			this);
	protected String logFileDirectory;
	

	public AMainClassListLauncher() {
		trackTermination();
//		Thread thread = new Thread(this);
//		Runtime.getRuntime().addShutdownHook(thread);
	}
	
	public AMainClassListLauncher(String aLogFileDirectory) {
		logFileDirectory = aLogFileDirectory;
		trackTermination();
//		Thread thread = new Thread(this);
//		Runtime.getRuntime().addShutdownHook(thread);
	}
	
	
	void trackTermination() {
		Thread thread = new Thread(this);
		Runtime.getRuntime().addShutdownHook(thread);
	}
	
	public int size() {
		return mainClasses.size();
	}
	public Class get(int index) {
		return mainClasses.get(index);
	}
	public ProcessExecer open(Class element) {
		ProcessExecer anExecuted = OEMisc.runWithObjectEditorConsole(element, "");
//		executed.add(OEMisc.runWithObjectEditorConsole(element, ""));
		executed.add(anExecuted);
		return anExecuted;
	}	
	// if we want to for instance gater output of all processes
	@Override
	public ProcessExecer nonInteractiveExecute(Class element) {
		ProcessExecer anExecuted = OEMisc.runWithProcessExecer(element, "");
//		executed.add(OEMisc.runWithObjectEditorConsole(element, ""));
		executed.add(anExecuted);
		return anExecuted;
	}
	
	// why do we need this? better name than open?
	public void execute(Class element) {
		open(element);
	}	
	public void terminateChildren() {
		killAllChildren();
	}	
	public void terminateAll() {
		System.exit(0);
	}	
	void killAllChildren() {
		for (ProcessExecer processExecer: executed) {
			processExecer.getProcess().destroy();
		}
	}
	@Override
	public List<ProcessExecer> getProcessExecers() {
		return executed;
	}
	
	// this is a runnable run
	// what is this? not a runnable, strange thing to do, kill all children
	// in a method called run
	// ah, this is to deal with shutdown hook. The hook is a runnable
	// and it is supposed to kill all childre. works from console from
	// what I remmeber
	@Visible(false)
	public void run() {
		killAllChildren();
	}
	@Override
	public boolean add(Class element) {
		return add(element, "" );
	}
	@Override
	public boolean add(Class element, String args) {
		boolean retVal = mainClasses.add(element);
		retVal = retVal && mainArgs.add(args);
		vectorChangeSupport.elementAdded(element);
		return retVal;
	}
	@Override
	public String getLogFileDirectory() {
		return logFileDirectory;
	}
	@Override
	public void setLogFileDirectory(String logFileDirectory) {
		this.logFileDirectory = logFileDirectory;
	}
}
