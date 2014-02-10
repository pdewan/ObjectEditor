package bus.uigen.undo;
public interface Undoer
{

    public Object execute (Command c);
    public boolean undo();
    public boolean redo();
    public void beginTransaction();
    public void endTransaction();
}