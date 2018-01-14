package util.trace.uigen;

import util.trace.ObjectInfo;

public class WidgetShellCreationStarted extends MajorStepInfo {	
	
	public WidgetShellCreationStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	public static WidgetShellCreationStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Creating widget shells of tree rooted by: " + anObject;
		WidgetShellCreationStarted retVal = new WidgetShellCreationStarted(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}
	
	

}
