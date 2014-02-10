package bus.uigen.undo;
import java.util.Vector;
import java.lang.reflect.Method;

import util.trace.Tracer;

import bus.uigen.uiFrame;
import bus.uigen.reflect.MethodProxy;
public class CommandList implements Command{
	Vector commands = new Vector();
	public void addElement(Command c) {
		commands.addElement(c);
	}
	public int size() {
		return commands.size();
	}
	public Command elementAt(int i) {
		return (Command) commands.elementAt(i);
	}
	public Command clone(Object theParentObject, Object[] theParams, uiFrame theFrame, CommandListener theListener) {
		Tracer.error("CommandList nshould not be cloned");
		return null;
	}
	public  Object execute() {
		Vector results = new Vector();
		
		//System.out.println("next command index " + nextCommandIndex);
		for (int i = 0; i < commands.size(); i++) {
			Command c = (Command) commands.elementAt(i);
			Object retVal = c.execute();
			//results.addElement(c.execute());
			if (!c.isVoid())
			results.addElement(retVal);
		}
		return results;
	}
	public  void  undo() {
		for (int i = 0; i < commands.size(); i++) {
			Command c = (Command) commands.elementAt(i);
			c.undo();
		}
	}
	public  boolean isUndoable() {
		for (int i = 0; i < commands.size(); i++) {
			Command c = (Command) commands.elementAt(i);
			if (!c.isUndoable()) return false;
		}
		return true;
	}
	public  boolean isNoOp() {
		for (int i = 0; i < commands.size(); i++) {
			Command c = (Command) commands.elementAt(i);
			if (!c.isNoOp()) return false;
		}
		return true;
	}
	public  boolean isVoid() {
		for (int i = 0; i < commands.size(); i++) {
			Command c = (Command) commands.elementAt(i);
			if (!c.isVoid()) return false;
		}
		return true;
	}
	public void redo()    {
       // execute();		
		//System.out.println("next command index " + nextCommandIndex);
		for (int i = 0; i < commands.size(); i++) {
			Command c = (Command) commands.elementAt(i);
			c.redo();
		}
    }  
	public Object getObject() {
		return null;
	}
	public MethodProxy getMethod() {
		return null;
	}
	//boolean notUndablePurgesHistory = false;
	/*
	void initializeNotUndoable () {
		boolean retVal = true;
		for (int i = 0; i < commands.size(); i++) {
			Command c = (Command) commands.elementAt(i);
			retVal = retVal && c.getNotUndoablePurgesUndoHistory();
		}
	}
	*/
	@Override
	public boolean getNotUndoablePurgesUndoHistory() {
		boolean retVal = true;
		for (int i = 0; i < commands.size(); i++) {
			Command c = (Command) commands.elementAt(i);
			retVal = retVal && c.getNotUndoablePurgesUndoHistory();
		}
		return retVal;
	}

	@Override
	public void setNotUndoablePurgesUndoHistory(boolean newVal) {
		// TODO Auto-generated method stub
		//notUndablePurgesHistory = newVal;
	}
	
}
