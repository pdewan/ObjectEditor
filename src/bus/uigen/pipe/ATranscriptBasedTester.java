package bus.uigen.pipe;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.misc.Common;
import util.models.ALocalGlobalTranscriptManager;
import util.models.LocalGlobalTranscriptManager;
import util.pipe.AConsoleModel;
import util.pipe.ConsoleModel;
import util.trace.Traceable;
import util.trace.uigen.TraceUtility;

public class ATranscriptBasedTester implements TranscriptBasedTester {

	// protected List<ConsoleModel> consoleModels;
	protected List<LocalGlobalTranscriptManager> transcriptManagers = new ArrayList();
	protected List<String> processNames = new ArrayList();

	protected List<List<Traceable>> localTraceableLists;
	protected Map<String, List<Traceable>> processToLocalTraceableList = new HashMap();
	protected Map<String, List<Traceable>> processToCorrectTraceableList = new HashMap();

	protected List<Traceable> globalTraceableList;

	protected List<List<Traceable>> correctLocalTraceableLists;
	protected List<Traceable> correctGlobalTraceableList;

	// boolean terminated;
	// boolean interactive;
	// protected MainClassListLauncher launcher;
	// protected String traceDirectory;

	// public ATranscriptBasedTester() {
	// this(true);
	// }

	// public ATranscriptBasedTester(boolean anInteractive) {
	// // interactive = anInteractive;
	// }

	// @Override
	// public void executeAll() {
	//
	// launcher.executeAll();
	// }

	// public void executeAll() {
	// consoleModels = launcher.getOrCreateConsoleModels();
	// // aliceConsole = consoleModels.get(1);
	// // bobConsole = consoleModels.get(2);
	// // cathyConsole = consoleModels.get(3);
	// launcher.addConsolesPropertyChangeListener(this); // input added in
	// // response to
	// // events
	// launcher.executeAll();
	// }

	// protected abstract Class[] composeMainClasses() ;
	// protected abstract String[] processNames();

	// @Override
	// public MainClassListLauncher createAndDisplayLauncher() {
	// Class[] classes = composeMainClasses();
	//
	// // launcher = MainClassLaunchingUtility.interactiveLaunch(classes,
	// "DemoerOfIM_Transcript.txt");
	// if (interactive)
	// launcher = MainClassLaunchingUtility.createInteractiveLauncher(classes);
	// else
	// launcher = MainClassLaunchingUtility.createLauncher(classes);
	//
	// consoleModels = launcher.getOrCreateConsoleModels();
	// for (ConsoleModel aConsoleModel: consoleModels) {
	//
	// }
	//
	//
	// return launcher;
	//
	// }

	// @Override
	// public synchronized void notifyInteractionTermination() {
	// terminated = true;
	// this.notify();
	//
	// }
	// @Override
	// public synchronized void waitForInteractionTermination() {
	// try {
	// while (!terminated)
	// this.wait();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static final String DEFAULT_CORRECT_CONSOLE_TRANSCRIPTS = "correctTranscripts";
	public static final String DEFAULT_TEST_CONSOLE_TRANSCRIPTS = "testTranscripts";

	protected String correctConsoleTranscriptsFolder = DEFAULT_CORRECT_CONSOLE_TRANSCRIPTS;
	protected String testConsoleTranscriptsFolder = DEFAULT_TEST_CONSOLE_TRANSCRIPTS;
	protected String correctSubFolder;
	protected String testSubFolder;

	public ATranscriptBasedTester() {
		setCorrectSubFolder(generateCorrectSubDirectory());
		setTestSubFolder(generateTestSubDirectory());
	}

	@Override
	public String getCorrectDirectory() {
		// return CORRECT_CONSOLE_TRANSCRIPTS + "/" +
		// getClass().getSimpleName();
		// return getCorrectConsoleTranscriptsFolder() + "/" +
		// generateCorrectSubDirectory();
		return getCorrectConsoleTranscriptsFolder() + "/"
				+ getCorrectSubFolder();

	}

	protected String generateCorrectSubDirectory() {
		return toDirectoryName(getClass());
	}

	protected String generateTestSubDirectory() {
		return toDirectoryName(getClass());
	}

	protected String toDirectoryName(Class aClass) {
		return getClass().getSimpleName();
	}

	@Override
	public String getTestDirectory() {
		// return TEST_CONSOLE_TRANSCRIPTS + "/" + getClass().getSimpleName();
		return getTestConsoleTranscriptsFolder() + "/" + getTestSubFolder();

	}

	// @Override
	// public void generateCorrectTranscripts() {
	// launcher.logConsoles(generateCorrectDirectory());
	// }
	// @Override
	// public void generateTestTranscripts() {
	// launcher.logConsoles(generateTestDirectory());
	// }
	// public void loadTestTraceables () {
	// if (consoleModels == null || consoleModels.size() == 0)
	// return;
	// localTraceableLists = new ArrayList();
	//
	// for (int index = 0; index < consoleModels.size(); index++) {
	// String aLocalTranscriptFile =
	// consoleModels.get(index).getLocalGlobalTranscriptManager().getLocalTranscriptFile();
	// List<Traceable> traceableList =
	// TraceUtility.toTraceableList(aLocalTranscriptFile);
	// localTraceableLists.add(traceableList);
	// processToLocalTraceableList.put(consoleModels.get(index).getTitle(),
	// traceableList);
	// }
	// String aGlobalTrancriptFile =
	// consoleModels.get(0).getLocalGlobalTranscriptManager().getGlobalTranscriptFile();
	//
	//
	// globalTraceableList = TraceUtility.toTraceableList(aGlobalTrancriptFile);
	//
	// }
	public void loadTestTraceables() {
		if (transcriptManagers == null || transcriptManagers.size() == 0)
			return;
		localTraceableLists = new ArrayList();

		for (int index = 0; index < transcriptManagers.size(); index++) {
			String aLocalTranscriptFile = transcriptManagers.get(index)
					.getLocalTranscriptFile();
			List<Traceable> traceableList = TraceUtility
					.toTraceableList(aLocalTranscriptFile);
			localTraceableLists.add(traceableList);
			processToLocalTraceableList.put(processNames.get(index),
					traceableList);
		}
		String aGlobalTrancriptFile = transcriptManagers.get(0)
				.getGlobalTranscriptFile();

		globalTraceableList = TraceUtility
				.toTraceableList(aGlobalTrancriptFile);

	}

	public static List<String> getSortedFiles(String aDirectory) {
		File file = new File(aDirectory);
		if (!file.exists()) {
			System.err.println("Folder does not exist:" + aDirectory);
			return null;
		}
		String[] arrayChildren = file.list();
		List<String> listChildren = Common.arrayToArrayList(arrayChildren);
		Collections.sort(listChildren);
		return listChildren;
	}

	public void loadCorrectTraceables(String aCorrectDirectory) {
		correctLocalTraceableLists = new ArrayList();
		List<String> sortedFiles = getSortedFiles(aCorrectDirectory);
		if (sortedFiles == null) {
			System.err.println("Correct directory not found");
			return;
		}
		String aGlobalTrancriptFile = AConsoleModel
				.getGlobalTranscriptFileName(aCorrectDirectory);
		for (int index = 0; index < sortedFiles.size(); index++) {
			String aTranscriptFile = aCorrectDirectory + "/"
					+ sortedFiles.get(index);
			if (aTranscriptFile.equals(aGlobalTrancriptFile))
				continue;
			String aTitleName = ALocalGlobalTranscriptManager
					.getTitle(aTranscriptFile);
			List<Traceable> traceableList = TraceUtility
					.toTraceableList(aTranscriptFile);
			correctLocalTraceableLists.add(traceableList);
			processToCorrectTraceableList.put(aTitleName, traceableList);
		}
		correctGlobalTraceableList = TraceUtility
				.toTraceableList(aGlobalTrancriptFile);
	}

	@Override
	public void loadTraceables(Boolean aGenerateCorrectTranscripts,
			Boolean aTestAgainstCorrectTraceables) {
		loadTestTraceables();
		if (!aGenerateCorrectTranscripts && aTestAgainstCorrectTraceables) {
			loadCorrectTraceables(getCorrectDirectory());
		}

		// if (consoleModels == null || consoleModels.size() == 0)
		// return;
		// localTraceableLists = new ArrayList();
		// correctLocalTraceableLists = new ArrayList();
		//
		// for (int index = 0; index < consoleModels.size(); index++) {
		// String aLocalTranscriptFile =
		// consoleModels.get(index).getLocalTranscriptFile();
		// List<Traceable> traceableList =
		// TraceUtility.toTraceableList(aLocalTranscriptFile);
		// localTraceableLists.add(traceableList);
		// }
		// String aGlobalTrancriptFile =
		// consoleModels.get(0).getGlobalTranscriptFile();
		// globalTraceableList =
		// TraceUtility.toTraceableList(aGlobalTrancriptFile);

	}

	// // null method for those who do not want to listen to console input
	// public void propertyChange(PropertyChangeEvent aConsoleModelEvent) {
	//
	// }

	@Override
	public List<List<Traceable>> getLocalTraceableLists() {
		return localTraceableLists;
	}

	@Override
	public List<List<Traceable>> getCorrectLocalTraceableLists() {
		return correctLocalTraceableLists;
	}

	@Override
	public List<Traceable> getCorrectGlobalTraceableList() {
		return correctGlobalTraceableList;
	}

	@Override
	public List<Traceable> getGlobalTraceableList() {
		return globalTraceableList;
	}

	// @Override
	// public MainClassListLauncher demo() {
	// createAndDisplayLauncher();
	// executeAll();
	// waitForInteractionTermination();
	// return launcher;
	// }
	@Override
	public Boolean test() {
		return false;
	}

	@Override
	public Boolean testAgainstCorrectTranscripts() {
		return false;
	}

	@Override
	public Boolean test(Boolean aTestAgainstCorrectTranscripts) {
		boolean retVal = test();
		return aTestAgainstCorrectTranscripts ? retVal
				& testAgainstCorrectTranscripts() : retVal;
	}

	// public void generateTranscripts(Boolean aCorrectTranscripts, Boolean
	// aTestAgainstCorrectTranscripts) {
	// if (aCorrectTranscripts)
	// generateCorrectTranscripts();
	// else
	// generateTestTranscripts();
	// }
	public void addProcessName(String aProcessName) {
		if (!processNames.contains(aProcessName)) {
			processNames.add(aProcessName);
		}

	}

	@Override
	public void addTranscriptManager(
			LocalGlobalTranscriptManager aTranscriptManager) {
		if (!transcriptManagers.contains(aTranscriptManager)) {
			transcriptManagers.add(aTranscriptManager);
		}

	}

	// // override this method to work on the transcripts
	// public Boolean executeLoadAndTest(Boolean aCorrectTranscripts, Boolean
	// aTestAgainstCorrectTranscripts) {
	// createAndDisplayLauncher();
	// generateTranscripts(aCorrectTranscripts, aTestAgainstCorrectTranscripts);
	// // if (aCorrectTranscripts)
	// // generateCorrectTranscripts();
	// // else
	// // generateTestTranscripts();
	// executeAll();
	// waitForInteractionTermination();
	// loadTraceables(aCorrectTranscripts, aTestAgainstCorrectTranscripts);
	// return test(aTestAgainstCorrectTranscripts);
	// // return true; // in general a test should be superclass tests added
	// with subclsas ones
	// }
	//
	// public List<ConsoleModel> getConsoleModels() {
	// return consoleModels;
	// }
	//
	//
	//
	// public void setConsoleModels(List<ConsoleModel> consoleModels) {
	// this.consoleModels = consoleModels;
	// }
	//
	//
	//
	// public boolean isTerminated() {
	// return terminated;
	// }
	//
	//
	//
	// public void setTerminated(boolean terminated) {
	// this.terminated = terminated;
	// }
	//
	//
	//
	// public boolean isInteractive() {
	// return interactive;
	// }
	//
	//
	//
	// public void setInteractive(boolean interactive) {
	// this.interactive = interactive;
	// }
	//
	//
	// public MainClassListLauncher getLauncher() {
	// return launcher;
	// }
	//
	//
	//
	// public void setLauncher(MainClassListLauncher launcher) {
	// this.launcher = launcher;
	// }
	//
	//
	//
	//
	//
	// public void terminate() {
	// launcher.terminateAll();
	// }

	@Override
	public String getCorrectConsoleTranscriptsFolder() {
		return correctConsoleTranscriptsFolder;
	}

	@Override
	public void setCorrectConsoleTranscriptsFolder(
			String correctConsoleTranscriptsFolder) {
		this.correctConsoleTranscriptsFolder = correctConsoleTranscriptsFolder;
	}

	@Override
	public String getTestConsoleTranscriptsFolder() {
		return testConsoleTranscriptsFolder;
	}

	@Override
	public void setTestConsoleTranscriptsFolder(
			String testConsoleTranscriptsFolder) {
		this.testConsoleTranscriptsFolder = testConsoleTranscriptsFolder;
	}

	protected void newInput(String aProcessName, String newValue) {

	}

	protected void newOutputLine(String aProcessName, String newValue) {

	}

	// public void propertyChange(PropertyChangeEvent aConsoleModelEvent) {
	// newIOFromProcess(((ConsoleModel)
	// aConsoleModelEvent.getSource()).getTitle(),
	// aConsoleModelEvent.getNewValue());
	// ConsoleModel aConsoleModel = (ConsoleModel)
	// aConsoleModelEvent.getSource();
	// if
	// (aConsoleModelEvent.getPropertyName().equals(ConsoleModel.OUTPUT_LINE))
	// newOutputLine(aConsoleModel.getTitle(), (String)
	// aConsoleModelEvent.getNewValue());
	// else if (aConsoleModelEvent.getPropertyName().equals(ConsoleModel.INPUT))
	// {
	// newInput(aConsoleModel.getTitle(), (String)
	// aConsoleModelEvent.getNewValue());
	// }
	//
	// }

	protected void newIOFromProcess(String aProcessName, Object newValue) {
		// inefficient as each output causes this codee to be executed
		// addProcessName(aProcessName);
	}

	public List<String> getProcessNames() {
		return processNames;
	}

	public void setProcessNames(List<String> processNames) {
		this.processNames = processNames;
	}

	public String getCorrectSubFolder() {
		return correctSubFolder;
	}

	public void setCorrectSubFolder(String correctSubFolder) {
		this.correctSubFolder = correctSubFolder;
	}

	public String getTestSubFolder() {
		return testSubFolder;
	}

	public void setTestSubFolder(String testSubFolder) {
		this.testSubFolder = testSubFolder;
	}

	// protected void consoleModelsInitialized() {
	// for (ConsoleModel aConsoleModel:consoleModels) {
	// addProcessName(aConsoleModel.getTitle());
	// addTranscriptManager(aConsoleModel.getLocalGlobalTranscriptManager());
	// }
	// }

}
