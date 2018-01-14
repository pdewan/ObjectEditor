package util.trace.uigen;

import util.trace.ObjectInfo;

public class EditorGenerationEnded extends ObjectInfo {	
	
	public EditorGenerationEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	
	public static EditorGenerationEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Finished generating editor for " + anObject;
		EditorGenerationEnded retVal = new EditorGenerationEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}

}
