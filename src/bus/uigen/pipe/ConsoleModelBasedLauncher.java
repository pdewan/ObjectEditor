package bus.uigen.pipe;

public interface ConsoleModelBasedLauncher {
	Class[] mainClasses();
	 MainClassListLauncher launchWithConsoles();
	 String[] processNames();
	MainClassListLauncher launchWithoutConsoles();
	MainClassListLauncher getLauncher();

}
