package bus.uigen.models;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.annotations.Visible;
import util.misc.Common;
import util.misc.ThreadSupport;
import util.models.AConsoleModel;
import util.models.AListenableVector;
import util.models.ConsoleModel;
import util.models.VectorChangeSupport;
import util.remote.ProcessExecer;
import bus.uigen.misc.OEMisc;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.LIST_PATTERN)
public class AMainClassListLauncher /*extends AListenableVector<Class>*/  implements MainClassListLauncher {
//	public static final String GLOBAL_FILE_NAME = "globalTranscript.txt";
	List<ProcessExecer> execers = new ArrayList();
	List<ConsoleModel> consoleModels; 
	List<Class> mainClasses  = new ArrayList();
	List<String> mainArgs = new ArrayList();
	
	public static final int DEFAULT_WAIT_TIME = 4000;
	transient protected VectorChangeSupport<Class> vectorChangeSupport = new VectorChangeSupport(
			this);
	boolean interactive;


	//	protected String transcriptFile;
	public AMainClassListLauncher(boolean anInteractive) {
		interactive = anInteractive;
		trackTermination();
//		Thread thread = new Thread(this);
//		Runtime.getRuntime().addShutdownHook(thread);
	}

	public AMainClassListLauncher() {
		this(true);
//		Thread thread = new Thread(this);
//		Runtime.getRuntime().addShutdownHook(thread);
	}
	
//	public AMainClassListLauncher(String aLogFile) {
//		setTranscriptFile(aLogFile);
//		trackTermination();
////		Thread thread = new Thread(this);
////		Runtime.getRuntime().addShutdownHook(thread);
//	}
	
	
	void trackTermination() {
		Thread thread = new Thread(this);
		thread.setName("Shut down hook");
		Runtime.getRuntime().addShutdownHook(thread);
	}
	
	public int size() {
		return mainClasses.size();
	}
	public Class get(int index) {
		return mainClasses.get(index);
	}
	public ProcessExecer execute(Class element, ConsoleModel aConsoleModel) {
		ProcessExecer anExecuted;
		if (interactive)
		 anExecuted = OEMisc.runWithObjectEditorConsole(element, "", aConsoleModel);
		else
			anExecuted = OEMisc.runWithProcessExecer(element, "", aConsoleModel);
//		executed.add(OEMisc.runWithObjectEditorConsole(element, ""));
//		executed.add(anExecuted);
		add(anExecuted);
		
		return anExecuted;
	}
	public ProcessExecer open(Class element) {
		return execute(element, new AConsoleModel());
//		ProcessExecer anExecuted = OEMisc.runWithObjectEditorConsole(element, "");
////		executed.add(OEMisc.runWithObjectEditorConsole(element, ""));
////		executed.add(anExecuted);
//		add(anExecuted);
//		
//		return anExecuted;
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
		execers.add(aProcessExecer);
//		aProcessExecer.consoleModel().setGlobalTranscriptFile(transcriptFile);
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
		for (ProcessExecer processExecer: execers) {
			processExecer.getProcess().destroy();
		}
	}
	@Override
	@Visible(false)
	public List<ProcessExecer> getProcessExecers() {
		return execers;
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
//	@Visible(false)
//	@Override
//	public String getTranscriptFile() {
//		return transcriptFile;
//	}
//	@Visible(false)
//	@Override
//	public void setTranscriptFile(String aTranscriptFile) {
//		this.transcriptFile = aTranscriptFile;
//		if (aTranscriptFile == null) return;
//		try {
//			Common.writeText(transcriptFile, "");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	@Override
	public void executeAll() {
		executeAll(DEFAULT_WAIT_TIME);
	}
	@Override
	public void waitForAll() throws InterruptedException {
		for (ProcessExecer aProcessExecer:execers) {
			aProcessExecer.getProcess().waitFor();
		}
	}
	@Override
	public void executeAll(long aWaitTime) {
		getOrCreateConsoleModels();
		int i = 0;
		for (Class aMainClass:mainClasses) {
			execute(aMainClass, consoleModels.get(i));
			i++;
			ThreadSupport.sleep (aWaitTime);
		}
	}
	@Visible(false)
	public List<ConsoleModel> createConsoleModels() {
		consoleModels = new ArrayList();
		for (Class aMainClass:mainClasses) {
			ConsoleModel consoleModel = new AConsoleModel();
			consoleModels.add(consoleModel);
		}
		return consoleModels;
	}
	@Override
	@Visible(false)
	public List<ConsoleModel> getOrCreateConsoleModels() {
		if (consoleModels == null) {
			createConsoleModels();
		}
		return consoleModels;
	}
	
	@Override
	@Visible(false)
	public void addConsolesPropertyChangeListener(PropertyChangeListener aPropertyChangeListener) {
		getOrCreateConsoleModels();
		for (ConsoleModel aConsoleModel:consoleModels) {
			aConsoleModel.addPropertyChangeListener(aPropertyChangeListener);
		}
	}
//	public static final String INDEX_SUFFIX = "_";
//	public static final String TRANSCRIPT_FILE_SUFFIX = ".txt";
//	public static String getLocalTranscriptFileName(String aDirectory, Integer anIndex, Class aClass) {
//		return aDirectory + "/" + anIndex + "_" + aClass.getSimpleName() + TRANSCRIPT_FILE_SUFFIX ;
//	}
//	
//	public static String getLocalTranscriptFileName(String aDirectory, Integer anIndex, String aTitle) {
//		return aDirectory + "/" + anIndex + "_" + aTitle + TRANSCRIPT_FILE_SUFFIX ;
//	}
//	
//	public static String getClassName(String aLocalTranscriptFileName) {
//		int aStart = aLocalTranscriptFileName.indexOf(INDEX_SUFFIX) + 1;
//		int anEnd = aLocalTranscriptFileName.indexOf(TRANSCRIPT_FILE_SUFFIX);
//		return aLocalTranscriptFileName.substring(aStart, anEnd);
//	}
//	
//	public static String getTitle(String aLocalTranscriptFileName) {
//		int aStart = aLocalTranscriptFileName.indexOf(INDEX_SUFFIX) + 1;
//		int anEnd = aLocalTranscriptFileName.indexOf(TRANSCRIPT_FILE_SUFFIX);
//		return aLocalTranscriptFileName.substring(aStart, anEnd);
//	}
//	
//	
//	public static String getGlobalTranscriptFileName (String aDirectory) {
//		return aDirectory + "/" + GLOBAL_FILE_NAME;
//	}
	
	@Override
	public void logConsoles(String aLogDirectory) {
//		String aGlobalTranscriptFile = aLogDirectory + "/" + GLOBAL_FILE_NAME;
		String aGlobalTranscriptFile =  AConsoleModel.getGlobalTranscriptFileName(aLogDirectory);

		try {
			Common.clearOrCreateFile(aGlobalTranscriptFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getOrCreateConsoleModels();
		for (int i = 0; i < consoleModels.size(); i++) {
			ConsoleModel aConsoleModel = consoleModels.get(i);
			aConsoleModel.setGlobalTranscriptFile(aGlobalTranscriptFile);
//			String aLocalTranscriptFile= aLogDirectory + "/" + mainClasses.get(i).getSimpleName() + ".txt";
//			String aLocalTranscriptFile= getLocalTranscriptFileName(aLogDirectory,i, mainClasses.get(i));
//			String aLocalTranscriptFile= getLocalTranscriptFileName(aLogDirectory,i, consoleModels.get(i).getTitle());

//			try {
//				Common.clearOrCreateFile(aLocalTranscriptFile);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			aConsoleModel.setLocalTranscriptFile(aLocalTranscriptFile);
			aConsoleModel.setIndexAndLogDirectory(i, aLogDirectory);
		}
	}
	@Visible(false)
	public boolean isInteractive() {
		return interactive;
	}
	@Visible(false)
	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}
	
}
