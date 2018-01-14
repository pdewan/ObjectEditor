package util.trace.uigen;

import util.trace.TraceableError;


public class NoFramesForUndoBind extends TraceableError {
	public NoFramesForUndoBind(String aMessage , Object aFinder) {
		super(aMessage, aFinder);		
	}
	
	public static NoFramesForUndoBind newExample(Object aFinder) {
		String aMessage =	"Bind first the application objects to widgets before binding the undo";	
		NoFramesForUndoBind retVal = new NoFramesForUndoBind(aMessage, aFinder);
		retVal.announce();
		return retVal;
	}

}
