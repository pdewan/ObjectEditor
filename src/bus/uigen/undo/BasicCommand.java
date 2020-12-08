package bus.uigen.undo;
import bus.uigen.*;

import java.lang.reflect.*;
import java.util.Vector;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
public class BasicCommand extends AbstractCommand implements Command {	
    uiFrame frame;
	MethodProxy method;
	Object parentObject;
	Object[] params;
	CommandListener listener;
	boolean isVoid;
	
	public BasicCommand (CommandListener theListener,
						  MethodProxy theMethod,
						  Object theParentObject,
						  Object[] theParams
						  ) {
		init(theListener, theMethod, theParentObject, theParams);
//        //frame = theFrame;
//        method = theMethod;
//		parentObject = theParentObject;
//		params = theParams;	
//		listener = theListener;
//		isVoid = method.getReturnType() == method.getDeclaringClass().voidType();
    }
	public void init(CommandListener theListener, MethodProxy theMethod,
			Object theParentObject, Object[] theParams) {
		// frame = theFrame;
		method = theMethod;
		parentObject = theParentObject;
		params = theParams;
		listener = theListener;
		isVoid = method.getReturnType() == method.getDeclaringClass()
				.voidType();
	}
	public Command clone(Object theParentObject, Object[] theParams, uiFrame theFrame, CommandListener theListener) {
		try {
		BasicCommand retVal = (BasicCommand) super.clone();
		retVal.init(theListener, method, theParentObject, theParams);
		return retVal;
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	public Object getObject() {
		return parentObject;
	}
	public MethodProxy getMethod() {
		return method;
	}
    public boolean isUndoable()
    {
        return false;
    }
    public boolean isVoid()
    {
        return isVoid;
    }
    public Object execute()   {
		try {
			
			Object retVal = MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   method,
												   params);
			//listener.commandActionPerformed();
			return retVal;
		} catch (Exception e) {
			System.err.println ("Could not execute: " + method + " " + e);
			return null;
		}
    }
    public void undo()    {
		
			System.err.println ("Cannot  undo: " + method);
		
    }
    public void redo()    {
        execute();
		listener.commandActionPerformed();
    }  
	public boolean isNoOp () { return false;}
	/*
	boolean notUndablePurgesHistory = false;
	@Override
	public boolean getNotUndoablePurgesUndoHistory() {
		// TODO Auto-generated method stub
		return notUndablePurgesHistory;
	}

	@Override
	public void setNotUndoablePurgesUndoHistory(boolean newVal) {
		// TODO Auto-generated method stub
		notUndablePurgesHistory = newVal;
	}
	*/
}

