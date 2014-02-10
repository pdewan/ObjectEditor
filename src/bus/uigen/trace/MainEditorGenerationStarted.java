package bus.uigen.trace;

import util.trace.ObjectInfo;

public class MainEditorGenerationStarted extends EditorGenerationStarted {	
	
	public MainEditorGenerationStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	
	public static MainEditorGenerationStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Generating main editor for:" + anObject;
		MainEditorGenerationStarted retVal = new MainEditorGenerationStarted(aMessage, anObject, aFinder);
		retVal.announce();
		return retVal;
	}

}
