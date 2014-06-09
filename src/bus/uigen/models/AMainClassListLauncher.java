package bus.uigen.models;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.annotations.Visible;
import util.misc.Common;
import util.misc.ThreadSupport;
import util.models.AListenableVector;
import util.models.VectorChangeSupport;
import util.remote.ProcessExecer;
import bus.uigen.misc.OEMisc;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.LIST_PATTERN)
public class AMainClassListLauncher /*extends AListenableVector<Class>*/  implements MainClassListLauncher {
	List<ProcessExecer> executed = new ArrayList();	
	List<Class> mainClasses  = new ArrayList();
	List<String> mainArgs = new ArrayList();
	public static final int DEFAULT_WAIT_TIME = 3000;
	transient protected VectorChangeSupport<Class> vectorChangeSupport = new VectorChangeSupport(
			this);
	protected String transcriptFile;
	

	public AMainClassListLauncher() {
		trackTermination();
//		Thread thread = new Thread(this);
//		Runtime.getRuntime().addShutdownHook(thread);
	}
	
	public AMainClassListLauncher(String aLogFile) {
		setTranscriptFile(aLogFile);
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
//		executed.add(anExecuted);
		add(anExecuted);
		
		return anExecuted;
	}	
	// if we want to for instance gater output of all processes
	@Override
	public ProcessExecer nonInteractiveExecute(Class element) {
		ProcessExecer anExecuted = OEMisc.runWithProcessExecer(element, "");
//		executed.add(OEMisc.runWithObjectEditorConsole(element, ""));
//		executed.add(anExecuted);
		add(anExecuted);
		return anExecuted;
	}
	
	protected void add (ProcessExecer aProcessExecer) {		
		executed.add(aProcessExecer);
		aProcessExecer.consoleModel().setTranscriptFile(transcriptFile);
	}
	
	
	// why do we need this? better name than open?
	// yes, in the menu it sounds better, open is for double click
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
	@Visible(false)
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
	@Visible(false)
	@Override
	public String getTranscriptFile() {
		return transcriptFile;
	}
	@Visible(false)
	@Override
	public void setTranscriptFile(String aTranscriptFile) {
		this.transcriptFile = aTranscriptFile;
		try {
			Common.writeText(transcriptFile, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void executeAll() {
		executeAll(DEFAULT_WAIT_TIME);
	}
	@Override
	public void executeAll(long aWaitTime) {
		for (Class aMainClass:mainClasses) {
			execute(aMainClass);
			ThreadSupport.sleep (aWaitTime);
		}
	}
}
