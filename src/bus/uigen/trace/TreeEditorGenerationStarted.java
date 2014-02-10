package bus.uigen.trace;

import util.trace.ObjectInfo;

public class TreeEditorGenerationStarted extends EditorGenerationStarted {	
	
	public TreeEditorGenerationStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}		
	public static TreeEditorGenerationStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Generating tree editor for:" + anObject;
		TreeEditorGenerationStarted retVal = new TreeEditorGenerationStarted(aMessage, anObject, aFinder);
		retVal.announce();
		return retVal;
	}
}
