package util.trace.uigen;

import util.trace.ObjectInfo;

public class DrawingEditorGenerationEnded extends EditorGenerationEnded {	
	
	public DrawingEditorGenerationEnded(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}	
	
	public static DrawingEditorGenerationEnded newCase(Object anObject, Object aFinder) {
		String aMessage = "Finished generating editor for " + anObject;
		DrawingEditorGenerationEnded retVal = new DrawingEditorGenerationEnded(aMessage, anObject, aFinder);
		retVal.announce();		
		return retVal;
	}

}
