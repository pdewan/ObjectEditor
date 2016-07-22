package bus.uigen.trace;

import util.trace.ObjectInfo;

public class TreefEditorGenerationEnded extends ObjectInfo {	
	
	public TreefEditorGenerationEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	
	public static TreefEditorGenerationEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Finished generating editor for " + anObject;
		TreefEditorGenerationEnded retVal = new TreefEditorGenerationEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}

}
