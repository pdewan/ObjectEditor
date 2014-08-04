package bus.uigen.pipe;


public  class AConsoleModelBasedLauncher implements ConsoleModelBasedLauncher {
	
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
	
	public  void launch() {	
//		Class[] classes = {
//				SessionManagerServerStarter.class,
//				AliceIM.class,
//				BobIM.class,
//				CathyIM.class				
//		};
		MainClassLaunchingUtility.createInteractiveLauncher(mainClasses());
	}
	
	

}
