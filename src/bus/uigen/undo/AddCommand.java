package bus.uigen.undo;
import bus.uigen.*;

import java.lang.reflect.*;
import java.util.Vector;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
public abstract class AddCommand extends AbstractCommand implements Command {	
    //uiFrame frame;
	MethodProxy subtractMethod, addMethod;
	Object parentObject;
	Object[] subtractParams;
	Object addParams[];
	CommandListener listener;
	
	public AddCommand (CommandListener theListener,
						  MethodProxy theAddMethod,
						  Object theParentObject,
						  Object[] theAddParams,
						  MethodProxy theSubtractMethod//,
						  //Object[] theReadMethodParams
						  ) {
		this.init(theListener, theAddMethod, theParentObject, theAddParams, theSubtractMethod);
        
//		//frame = theFrame;
//        addMethod = theAddMethod;
//		parentObject = theParentObject;
//		addParams = theAddParams;		
//		subtractMethod = theSubtractMethod;
//		//readMethodParams = theReadMethodParams;
//		listener = theListener;
//		subtractParams = createSubtractParams(theAddParams);
//		isVoid = addMethod.getReturnType() == addMethod.getDeclaringClass().voidType();

    }
	public void init (CommandListener theListener,
			MethodProxy theAddMethod,
			Object theParentObject,
			Object[] theAddParams,
			MethodProxy theSubtractMethod//,
			//Object[] theReadMethodParams
	) {
		//frame = theFrame;
		addMethod = theAddMethod;
		parentObject = theParentObject;
		addParams = theAddParams;		
		subtractMethod = theSubtractMethod;
		//readMethodParams = theReadMethodParams;
		listener = theListener;
		subtractParams = createSubtractParams(theAddParams);
		isVoid = addMethod.getReturnType() == addMethod.getDeclaringClass().voidType();

	}
	public Command clone(Object theParentObject, Object[] theParams, uiFrame theFrame, CommandListener theListener) {
		try {
		AddCommand retVal = (AddCommand) super.clone();
		retVal.init(theListener, addMethod, theParentObject, theParams, subtractMethod);
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
	 abstract Object[] createSubtractParams(Object[] addParams);
	public Object getObject() {
		return parentObject;
	}
	public MethodProxy getMethod() {
		return addMethod;
	}
	 /*
	 static Object[] removeLast(Object[] params) {
		Object[] readMethodParams = new Object[params.length - 1];
		for (int i = 0; i < params.length - 1; i ++) {
			readMethodParams[i] = params[i];
		}
		return readMethodParams;
	}
	*/
	public boolean isNoOp () { return false;}
    public boolean isUndoable()
    {
        return subtractMethod != null;
    }	
    public Object execute()   {
		try {
				Object retVal = MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   addMethod,
												   addParams);
			//listener.commandActionPerformed();
			return retVal;
			
		} catch (Exception e) {
			System.err.println ("Could not execute: " + subtractMethod + " " + e);
			return null;
		}
    }
    public void undo()    {
		try {			
			MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   subtractMethod,
												   subtractParams);
			listener.commandActionPerformed();
		} catch (Exception e) {
			System.err.println ("Could not undo: " + addMethod + " " + e);
		}
    }
    public void redo()    {
        execute();
		listener.commandActionPerformed();
    }  

}

