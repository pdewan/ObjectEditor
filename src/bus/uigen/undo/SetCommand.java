package bus.uigen.undo;
import bus.uigen.*;

import java.lang.reflect.*;
import java.util.Vector;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
public abstract class SetCommand extends AbstractCommand implements Command {	
    //uiFrame frame;
	MethodProxy writeMethod, readMethod, undoWriteMethod;
	Object parentObject;
	Object[] params;
	Object undoWriteMethodParams[]; 
	Object[] readMethodParams;
	CommandListener listener;
	public SetCommand (CommandListener theListener,
						  MethodProxy theWriteMethod,
						  Object theParentObject,
						  Object[] theParams,
						  MethodProxy theReadMethod//,
						  //Object[] theReadMethodParams
						  ) {
		init(theListener, theWriteMethod, theParentObject, theParams, theReadMethod);
//        //frame = theFrame;
//        writeMethod = theWriteMethod;
//        //undoWriteMethod = uiBean.getUndo(theParentObject, writeMethod);
//        //if (undoWriteMethod == null)
//        	undoWriteMethod = writeMethod;
//		parentObject = theParentObject;
//		params = theParams;		
//		readMethod = theReadMethod;
//		//readMethodParams = theReadMethodParams;
//		listener = theListener;
//		undoWriteMethodParams = Util.copy(theParams);
//		readMethodParams = createReadMethodParams(theParams);
//
//    
//	    isVoid = theWriteMethod.getReturnType() == theWriteMethod.getDeclaringClass().voidType();
	

}
	public void init(CommandListener theListener, MethodProxy theWriteMethod,
			Object theParentObject, Object[] theParams,
			MethodProxy theReadMethod// ,
	// Object[] theReadMethodParams
	) {
		// frame = theFrame;
		writeMethod = theWriteMethod;
		// undoWriteMethod = uiBean.getUndo(theParentObject, writeMethod);
		// if (undoWriteMethod == null)
		undoWriteMethod = writeMethod;
		parentObject = theParentObject;
		params = theParams;
		readMethod = theReadMethod;
		// readMethodParams = theReadMethodParams;
		listener = theListener;
		undoWriteMethodParams = Util.copy(theParams);
		readMethodParams = createReadMethodParams(theParams);

		isVoid = theWriteMethod.getReturnType() == theWriteMethod
				.getDeclaringClass().voidType();

	}

public Command clone(Object theParentObject, Object[] theParams, uiFrame theFrame, CommandListener theListener) {
	try {
	SetCommand retVal = (SetCommand) clone();
	//SetCommand retVal = (SetCommand) clone();
	retVal.init(theListener, writeMethod, theParentObject, theParams, readMethod);
	return retVal;
	} catch (Exception e) {			
		e.printStackTrace();
		return null;
	}
}
boolean isVoid;
public boolean isVoid () {
	return isVoid;
}
	public Object getObject() {
		return parentObject;
	}
	public MethodProxy getMethod() {
		return writeMethod;
	}
	 abstract Object[] createReadMethodParams(Object[] params);
	
	 /*
	 static Object[] removeLast(Object[] params) {
		Object[] readMethodParams = new Object[params.length - 1];
		for (int i = 0; i < params.length - 1; i ++) {
			readMethodParams[i] = params[i];
		}
		return readMethodParams;
	}
	*/
	 /*
	static Object[] createUndoWriteMethodParams(Object[] params) {
		Object[] undoWriteMethodParams = new Object[params.length];
		for (int i = 0; i < params.length; i ++) {
			undoWriteMethodParams[i] = params[i];
		}
		return undoWriteMethodParams;
	}
	 */
	 /*
	static Object[] copyParams(Object[] params) {
		Object[] copyParams = new Object[params.length];
		for (int i = 0; i < params.length; i ++) {
			copyParams[i] = params[i];
		}
		return copyParams;
	}
	 */
    public boolean isUndoable()
    {
        return readMethod != null;
    }
	abstract void assignToUndoWriteMethodParams(Object readValue);
	public Object computeReadValue() {
		Object readValue;
		try {				
				 readValue = MethodInvocationManager.invokeMethod(readMethod, 
															   parentObject,
															   readMethodParams);
				return Util.deepCopy(readValue);
		} catch (Exception e) { return null;}
		
	}
	public boolean isNoOp() {
		try {
			return computeReadValue().equals(params[0]);
		} catch (Exception e) { return false;}
	}
    public Object execute()   {
		try {
			if (readMethod != null) {
				/*
				
				Object readValue = uiMethodInvocationManager.invokeMethod(readMethod, 
															   parentObject,
															   readMethodParams);
				*/
				assignToUndoWriteMethodParams(computeReadValue());
			}
			Object retVal = MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   writeMethod,
												   params);
			//listener.commandActionPerformed();
			return retVal;
		} catch (Exception e) {
			System.err.println ("Could not execute: " + writeMethod + " " + e);
			return null;
		}
    }
    public void undo()    {
    	
		try {			
			MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   undoWriteMethod,
												   undoWriteMethodParams);
			listener.commandActionPerformed();
		} catch (Exception e) {
			System.err.println ("Could not undo: " + writeMethod + " " + e);
		}
    }
    public void redo()    {
        execute();
		listener.commandActionPerformed();
    } 
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

