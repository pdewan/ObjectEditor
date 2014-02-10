package bus.uigen.controller.models;

import util.annotations.Visible;
import util.undo.ExecutedCommand;
import bus.uigen.uiFrame;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.undo.HistoryUndoerListener;
import bus.uigen.undo.ListeningUndoer;
@util.annotations.StructurePattern(util.annotations.StructurePatternNames.NO_PATTERN)
public class AnUndoRedoModel extends AnAbstractOperationsModel implements FrameModel, HistoryUndoerListener {
//	uiFrame frame;
	ListeningUndoer undoer;
	@Visible(false)
	public void init (uiFrame theFrame, Object theObject, ObjectAdapter theObjectAdapter) {
//		frame = theFrame;
		super.init(theFrame, theObject, theObjectAdapter);
		undoer = frame.getUndoer();
		if (undoer != null)
		undoer.addListener(this);
		frame.getModelRegistry().registerUndoRedoModel(this);		
		//frame.registerModel(uiFrame.UNDO_REDO_MODEL, this);
	}
	public boolean preUndo() {
		return !undoHistoryEmpty;
	}
	public void undo() {
		if (undoer != frame.getUndoer()) {
			undoer = frame.getUndoer();
			undoer.addListener(this);
		}
		undoer.undo();
	}
	public boolean preRedo() {
		return !redoHistoryEmpty;
	}
	public void redo() {
		if (undoer != frame.getUndoer()) {
			undoer = frame.getUndoer();
			undoer.addListener(this);
		}
		undoer.redo();
		
	}
	boolean undoHistoryEmpty = true;
	@Visible(false)
	public void undoHistoryEmpty(boolean isEmpty) {
		undoHistoryEmpty = isEmpty;
	}
	boolean redoHistoryEmpty = true;
	@Visible(false)
	public void redoHistoryEmpty(boolean isEmpty) {
		redoHistoryEmpty = isEmpty;	
	}
	@Visible(false)
	public void commandUndone (ExecutedCommand c, int index) {
		
	}
	@Visible(false)
	public void commandRedone (ExecutedCommand c, int index) {
		
	}
	@Visible(false)
	public void commandExecuted (ExecutedCommand c, int index) {
		
	}
	

}
