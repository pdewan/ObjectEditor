package bus.uigen.trace;

import util.trace.ObjectInfo;

public class DrawingEditorGenerationStarted extends EditorGenerationStarted {	
	
	public DrawingEditorGenerationStarted(String aMessage, Object anObject, Object aFinder) {
		super(aMessage, anObject, aFinder);
	}		
	public static DrawingEditorGenerationStarted newCase(Object anObject, Object aFinder) {
		String aMessage = "Generating graphics editor for:" + anObject;
		DrawingEditorGenerationStarted retVal = new DrawingEditorGenerationStarted(aMessage, anObject, aFinder);
		retVal.announce();
		return retVal;
	}
}
