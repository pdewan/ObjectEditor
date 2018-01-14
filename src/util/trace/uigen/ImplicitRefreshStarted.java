package util.trace.uigen;



public class ImplicitRefreshStarted extends MajorStepInfo {
	public ImplicitRefreshStarted(String aMessage, Object aTarget, Object aFinder) {
		super(aMessage, aTarget, aFinder);	
	}	

	

	public static ImplicitRefreshStarted newCase(Object aTarget, Object aFinder) {
		String aMessage = "Implicit refresh started for frame: " + aTarget;
		ImplicitRefreshStarted retVal = new ImplicitRefreshStarted(aMessage, aTarget, aFinder);
		retVal.announce();
		return retVal;
	}

}
