package bus.uigen.undo;
import java.lang.reflect.Method;

import util.undo.ExecutedCommand;

import bus.uigen.uiFrame;
import bus.uigen.reflect.MethodProxy;
public interface  Command extends ExecutedCommand, Cloneable/*, ExecutableCommand*/
{
    public  Object execute();
    //public abstract void  undo();
    public  boolean isUndoable();	
    //public abstract void redo();
    public boolean getNotUndoablePurgesUndoHistory();
    public void setNotUndoablePurgesUndoHistory(boolean newVal);
	public MethodProxy getMethod();
	public Object getObject();
	public boolean isNoOp();
	public boolean isVoid();
	public Command clone(Object theParentObject, Object[] theParams, uiFrame theFrame, CommandListener theListener);
}