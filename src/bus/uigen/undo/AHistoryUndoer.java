package bus.uigen.undo;
import java.util.Vector;
import java.util.Hashtable;
import bus.uigen.*;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.reflect.MethodProxy;
//import bus.uigen.introspect.uiBean;
import java.lang.reflect.Method;

import util.undo.ExecutedCommand;
public class AHistoryUndoer implements /*ListenableUndoer, */ ListeningUndoer /*Undoer, ExecutedCommandListener*/ 
{
    Vector historyList = new Vector();
    int nextCommandIndex = 0;
	//HistoryUndoerListener listener;
	Hashtable listenables = new Hashtable();
	boolean middleOfTransaction = false;
	ExecutedCommandList commandList = new ExecutedCommandList();
	
	boolean addListenable(Object o, MethodProxy method) {
		//Method adder = uiBean.getAddExecutedCommandListener(o, method);
		MethodProxy adder = IntrospectUtility.getAddExecutedCommandListener(o);
		if (adder == null) return false;
		Vector v =  (Vector) listenables.get(o);
		if (v == null) {
			v = new Vector();
			listenables.put(o, v);
		}
		//if (!v.contains(method.getName())) {
		if (!v.contains("all")) {
			try {
				Object[] params = {this};
				adder.invoke(o, params);				
				//v.addElement(method.getName());
				v.addElement("all");
				return true;
			} catch (Exception e) {
				return false;
			}
		} else
			return true;
	}
	// this is sent by a model  after it has executed a command. Supports manual UI systems.
	public void commandExecuted(ExecutedCommand c) {
		//add(c);
		commandList.addElement(c);
		//addExecutedCommand(c);
	}
	
	public AHistoryUndoer(HistoryUndoerListener theListener) {
		addListener(theListener);
		//listener = theListener;
		
	}
	
	void notifyUndoHistoryEmpty (boolean isEmpty) {
		for (int index = 0; index < listeners.size(); index++) {
			listeners.elementAt(index).undoHistoryEmpty(isEmpty);
		}
	}
	
	void notifyRedoHistoryEmpty (boolean isEmpty) {
		for (int index = 0; index < listeners.size(); index++) {
			listeners.elementAt(index).redoHistoryEmpty(isEmpty);
		}
	}
	
	public AHistoryUndoer() {
		
		
	}
	Vector<HistoryUndoerListener> listeners = new Vector();
	public void addListener (HistoryUndoerListener theListener) {
		if (listeners.contains(theListener)) return;
		listeners.addElement(theListener);
		
	} 
	void notifyExecuted (ExecutedCommand c, int pos) {
		for (int index = 0; index < listeners.size(); index++) {
			listeners.elementAt(0).commandExecuted(c, pos);
		}
	}
	void notifyUndone (ExecutedCommand c, int pos) {
		for (int index = 0; index < listeners.size(); index++) {
			listeners.elementAt(0).commandUndone(c, pos);
		}
	}
	void notifyRedone (ExecutedCommand c, int pos) {
		for (int index = 0; index < listeners.size(); index++) {
			listeners.elementAt(0).commandRedone(c, pos);
		}
	}
	//boolean commandReceived = false;
    // thisis sent by a controller before the command is executed. The under must execute the method.
	public Object execute (Command c)
    {
		
		addListenable(c.getObject(), c.getMethod());
		Object	retVal = c.execute();
		if (commandList.size() > 0) {
			add(commandList);
			commandList = new ExecutedCommandList();			
		} else
		if (c.isUndoable())
			add(c);
		else if (c.getNotUndoablePurgesUndoHistory())
			emptyHistory();
			
		/*
		
		commandList = new ExecutedCommandList();
		if (middleOfTransaction()) {
			transactionCommands.addElement(c);
			return null;
		}
		//Object	retVal = c.execute();
		// if it is  a new list, its size = 0
		if (commandList.size() == 1)
			addExecutedCommand(commandList.elementAt(0));
		else if (commandList.size() >  1)
			addExecutedCommand(commandList);
		else if (c.isUndoable())
			addExecutedCommand(c);
			*/
				 
		
		return retVal;
    }
	public Object add (ExecutedCommand c)
    {
		
		
		
		//commandList = new ExecutedCommandList();
		if (middleOfTransaction()) {
			transactionCommands.addElement(c);
			return null;
		}
		
		addExecutedCommand(c);
		return c;
		/*
		//Object	retVal = c.execute();
		// if it is  a new list, its size = 0
		if (commandList.size() == 1)
			addExecutedCommand(commandList.elementAt(0));
		else if (commandList.size() >  1)
			addExecutedCommand(commandList);
		else if (c.isUndoable())
			addExecutedCommand(c);
			*/
				 
		/*
		int oldSize = historyList.size();
		Object	retVal = c.execute();
		if (oldSize == historyList.size() && c.isUndoable())
				addExecutedCommand(c);
		*/
		/*
		Object retVal = null;
		if (addListenable(c.getObject(), c.getMethod()))
			retVal = c.execute();
		else  {
			retVal = c.execute();
			if (c.isUndoable())			
				addExecutedCommand(c);
		}
		*/
        //Object retVal = c.execute();
		/*
		if (nextCommandIndex != historyList.size()) {
			historyList.removeAllElements();
			nextCommandIndex = 0;
			listener.redoHistoryEmpty(true);
		}
		//System.err.println("next command index " + nextCommandIndex);
        historyList.insertElementAt(c, nextCommandIndex);		
		if (nextCommandIndex == 0) {
			listener.undoHistoryEmpty(false);
		}
        nextCommandIndex++;
		*/
		
    }
	
	void addExecutedCommand(ExecutedCommand c) {
		if (nextCommandIndex != historyList.size()) {
			historyList.removeAllElements();
			nextCommandIndex = 0;
			notifyRedoHistoryEmpty(true);
		}
		//System.err.println("next command index " + nextCommandIndex);
        historyList.insertElementAt(c, nextCommandIndex);
        notifyExecuted(c, nextCommandIndex);
		if (nextCommandIndex == 0) {
			notifyUndoHistoryEmpty(false);
		}
        nextCommandIndex++;
	}
	
	void emptyHistory() {
		historyList.removeAllElements();
		nextCommandIndex = 0;
		notifyUndoHistoryEmpty(true);
		notifyRedoHistoryEmpty(true);
	}

    public boolean undo()
    {
        if (nextCommandIndex == 0)
            return (false);
        else        {
			if (nextCommandIndex == historyList.size()) {
				notifyRedoHistoryEmpty(false);
			}
            nextCommandIndex--;			
			if (nextCommandIndex == 0)
					notifyUndoHistoryEmpty(true);
            ExecutedCommand c = (ExecutedCommand) historyList.elementAt(nextCommandIndex);
			try {
				c.undo();
				notifyUndone(c, nextCommandIndex);
				return true;
			} catch (Exception e) {
				System.err.println("Could not undo command: " + c);
				return false;
			}
			/*
            if (!c.isUndoable())
                return (false);
            else {
                c.undo();
                return (true);
            }
			*/
        }
    }

    public boolean redo()
    {
        if (nextCommandIndex == historyList.size())
            return (false);
        else
        {
					
		if (nextCommandIndex == 0) {
			notifyUndoHistoryEmpty(false);
		}
            ExecutedCommand c = (ExecutedCommand) historyList.elementAt(nextCommandIndex);
            nextCommandIndex++;
			if (nextCommandIndex == historyList.size()) {
				notifyRedoHistoryEmpty(true);
			}
			try {
				c.redo();
				notifyRedone(c, nextCommandIndex);
				return true;
			} catch (Exception e) {
				System.err.println("Could not undo command: " + c);
				return false;
			}
			/*
            if (!c.isUndoable())
                return (false);
            else {
                c.redo();
                return (true);
            }
			*/
        }
    }
    ExecutedCommandList transactionCommands;
    public void beginTransaction() {
    	middleOfTransaction = true;
    	transactionCommands = new ExecutedCommandList();
    }
    public void endTransaction() {    	
    	middleOfTransaction = false;
    	//transactionCommands.execute();
    	if (transactionCommands.size() > 0) {    		
    		addExecutedCommand(transactionCommands);
    	}
    }
    public boolean middleOfTransaction() {
    	return middleOfTransaction;
    }


}