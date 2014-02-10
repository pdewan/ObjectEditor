package bus.uigen.undo;
import bus.uigen.*;

import java.lang.reflect.*;
import java.util.Vector;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
public abstract class SubtractCommand extends AbstractCommand implements Command {	
    //uiFrame frame;
	MethodProxy subtractMethod, addMethod, elementAtMethod;
	Object parentObject;
	Object[] subtractParams;
	Object addParams[];
	CommandListener listener;
	public SubtractCommand (CommandListener theListener,
						  MethodProxy theSubtractMethod,
						  Object theParentObject,
						  Object[] theSubtractParams,
						  MethodProxy theAddMethod//,
						  //VirtualMethod theElementAtMethod//,
						  //Object[] theReadMethodParams
						  ) {
		init(theListener, theSubtractMethod, theParentObject, theSubtractParams, theAddMethod);
//        //frame = theFrame;
//        subtractMethod = theSubtractMethod;
//		parentObject = theParentObject;
//		subtractParams = theSubtractParams;		
//		addMethod = theAddMethod;
//		//elementAtMethod = theElementAtMethod;
//		//readMethodParams = theReadMethodParams;
//		listener = theListener;
//		addParams = createAddParams(theSubtractParams);
//
//		isVoid = subtractMethod != null && subtractMethod.getReturnType() == StandardProxyTypes.voidType();

    }
	public void init (CommandListener theListener,
			MethodProxy theSubtractMethod,
			Object theParentObject,
			Object[] theSubtractParams,
			MethodProxy theAddMethod//,
			//VirtualMethod theElementAtMethod//,
			//Object[] theReadMethodParams
	) {
		//frame = theFrame;
		subtractMethod = theSubtractMethod;
		parentObject = theParentObject;
		subtractParams = theSubtractParams;		
		addMethod = theAddMethod;
		//elementAtMethod = theElementAtMethod;
		//readMethodParams = theReadMethodParams;
		listener = theListener;
		addParams = createAddParams(theSubtractParams);

		isVoid = subtractMethod != null && subtractMethod.getReturnType() == StandardProxyTypes.voidType();

	}
	public Command clone(Object theParentObject, Object[] theParams, uiFrame theFrame, CommandListener theListener) {
		try {
			SubtractCommand retVal = (SubtractCommand) super.clone();
			retVal.init(theListener, subtractMethod, theParentObject, theParams, addMethod);
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
		return subtractMethod;
	}
	 abstract Object[] createAddParams(Object[] subtractParams);
	
	 /*
	 static Object[] removeLast(Object[] params) {
		Object[] readMethodParams = new Object[params.length - 1];
		for (int i = 0; i < params.length - 1; i ++) {
			readMethodParams[i] = params[i];
		}
		return readMethodParams;
	}
	*/
	
    public boolean isUndoable()
    {
        return addMethod != null;
    }
	abstract void assignToAddParams(Object subtractedValue);	
    public Object execute()   {
		try {
			Object subtractedValue =
									MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   subtractMethod,
												   subtractParams);
			assignToAddParams(subtractedValue);
			//listener.commandActionPerformed();
			return subtractedValue;
			
		} catch (Exception e) {
			System.out.println ("Could not execute: " + subtractMethod + " " + e);
			return null;
		}
    }
    public void undo()    {
		try {			
			MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   addMethod,
												   addParams);
			listener.commandActionPerformed();
		} catch (Exception e) {
			System.out.println ("Could not undo: " + subtractMethod + " " + e);
		}
    }
    public void redo()    {
        execute();
		listener.commandActionPerformed();
    } 
	public boolean isNoOp () { return false;}

}

