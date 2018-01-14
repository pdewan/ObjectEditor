package util.trace.uigen;

import util.trace.ObjectInfo;

public class MainEditorGenerationEnded extends EditorGenerationEnded {	
	
	public MainEditorGenerationEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	
	public static MainEditorGenerationEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Finished generating editor for " + anObject;
		MainEditorGenerationEnded retVal = new MainEditorGenerationEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}

}
