package bus.uigen.pipe;

public interface ConsoleModelBasedLauncher {
	Class[] mainClasses();
	 void launchWithConsoles();
	 String[] processNames();
	void launchWithoutConsoles();
	MainClassListLauncher getLauncher();

}
