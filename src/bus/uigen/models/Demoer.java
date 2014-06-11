package bus.uigen.models;

import java.beans.PropertyChangeListener;

import util.misc.ThreadSupport;
import util.models.ConsoleModel;
import util.remote.ProcessExecer;

public interface Demoer extends PropertyChangeListener{

	MainClassListLauncher createAndDisplayLauncher();

	void executeAll();

	void generateCorrectTranscripts();

	void generateTestTranscripts();
	

}
