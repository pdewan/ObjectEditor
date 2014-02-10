package bus.uigen.trace;

import util.trace.ObjectInfo;

public class TreeEditorGenerationEnded extends EditorGenerationEnded {	
	
	public TreeEditorGenerationEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	
	public static TreeEditorGenerationEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Finished generating editor for " + anObject;
		TreeEditorGenerationEnded retVal = new TreeEditorGenerationEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}

}
