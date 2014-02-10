package bus.uigen.trace;

import util.trace.TraceableError;


public class NoFramesForUndoBind extends TraceableError {
	public NoFramesForUndoBind(String aMessage , Object aFinder) {
		super(aMessage, aFinder);		
	}
	
	public static void newExample(Object aFinder) {
		String aMessage =	"Bind first the application objects to widgets before binding the undo";	
		new NoFramesForUndoBind(aMessage, aFinder);
	}

}
