package bus.uigen.undo;

public interface ListeningUndoer extends Undoer, ExecutedCommandListener  {
	public void addListener (HistoryUndoerListener theListener) ;
}
