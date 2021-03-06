package bus.uigen.pipe;


public  class AConsoleModelBasedLauncher implements ConsoleModelBasedLauncher {
	MainClassListLauncher launcher;
	public  Class[] mainClasses() {
		return new Class[0];
	}
	public String[] processNames() {
		Class[] aMainClasses = mainClasses();
		String[] retVal = new String[aMainClasses.length];
		for (int i = 0; i < retVal.length; i++) {
			retVal[i] = aMainClasses[i].getSimpleName();
		}
		return retVal;
	}
	@Override
	public  MainClassListLauncher launchWithConsoles() {	
//		Class[] classes = {
//				SessionManagerServerStarter.class,
//				AliceIM.class,
//				BobIM.class,
//				CathyIM.class				
//		};
		launcher = MainClassLaunchingUtility.createInteractiveLauncher(mainClasses(), this.getClass().getSimpleName());
		return launcher;
	}
	@Override
	public  MainClassListLauncher launchWithoutConsoles() {	
//		Class[] classes = {
//				SessionManagerServerStarter.class,
//				AliceIM.class,
//				BobIM.class,
//				CathyIM.class				
//		};
		launcher = MainClassLaunchingUtility.createConsoleLessLauncher(mainClasses(), this.getClass().getSimpleName());
		return launcher;
	}
	@Override
	public MainClassListLauncher getLauncher() {
		return launcher;
	}
	
	

}
