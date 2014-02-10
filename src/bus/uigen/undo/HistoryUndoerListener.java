package bus.uigen.undo;

import util.undo.ExecutedCommand;

public interface HistoryUndoerListener {
    public void undoHistoryEmpty(boolean isEmpty);
	public void redoHistoryEmpty(boolean isEmpty);
	public void commandUndone (ExecutedCommand c, int index);
	public void commandRedone (ExecutedCommand c, int index);
	public void commandExecuted (ExecutedCommand c, int index);
	
}