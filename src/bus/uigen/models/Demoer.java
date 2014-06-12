package bus.uigen.models;

import java.beans.PropertyChangeListener;
import java.util.List;

import util.misc.ThreadSupport;
import util.models.ConsoleModel;
import util.remote.ProcessExecer;
import util.trace.Traceable;

public interface Demoer extends PropertyChangeListener{

	MainClassListLauncher createAndDisplayLauncher();

	void executeAll();

	void generateCorrectTranscripts();

	void generateTestTranscripts();

	void loadTraceables();

	List<List<Traceable>> getLocalTraceableLists();

	List<Traceable> getGlobalTraceableList();

	void notifyInteractionTermination();

	void waitForInteractionTermination();
	

}
