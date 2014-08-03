package bus.uigen.pipe;


public abstract class AConsoleModelBasedLauncher implements ConsoleModelBasedLauncher {
	
	public abstract Class[] mainClasses() ;
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
