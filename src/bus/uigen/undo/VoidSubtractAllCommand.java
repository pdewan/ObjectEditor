package bus.uigen.undo;
import bus.uigen.*;

import java.lang.reflect.*;
import java.util.Vector;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
public class VoidSubtractAllCommand extends AbstractCommand implements Command {	
    uiFrame frame;
	MethodProxy doMethod, inverseMethod;
	Object parentObject;
	Object[] doParams = {};
	Object inverseParams[] = {null};
	CommandListener listener;
	public VoidSubtractAllCommand (CommandListener theListener,
			  MethodProxy theDoMethod,
			  Object theParentObject,
			  Object[] theInverseParams,
			  MethodProxy theInverseMethod//,
			  //Object[] theReadMethodParams
			  ) {
		init(theListener, theDoMethod, theParentObject, theInverseParams, theInverseMethod);
	}
	
	public void init (CommandListener theListener,
						  MethodProxy theDoMethod,
						  Object theParentObject,
						  Object[] theInverseParams,
						  MethodProxy theInverseMethod//,
						  //Object[] theReadMethodParams
						  ) {
        //frame = theFrame;
        doMethod = theDoMethod;
		parentObject = theParentObject;
		//doParams = theDoParams;		
		inverseMethod = theInverseMethod;
		//readMethodParams = theReadMethodParams;
		listener = theListener;
		inverseParams = theInverseParams;
		//inverseParams = createInverseParams(theDoParams);
		//inverseParams = theDoParams;
		//Message.debug("Not copying parameters, will this cause a problem?");
		//doParams = inverseParams;		

    }
	public Command clone(Object theParentObject, Object[] theParams, uiFrame theFrame, CommandListener theListener) {
		try {
		VoidSubtractAllCommand retVal = (VoidSubtractAllCommand) super.clone();
		retVal.init(theListener, doMethod, theParentObject, theParams, inverseMethod);
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
	public boolean isNoOp () { return false;}
	public Object getObject() {
		return parentObject;
	}
	public MethodProxy getMethod() {
		return doMethod;
	}
	/*
	Object[] createInverseParams(Object[] doParams) {
		
		return Util.copy(doParams);
	}
	*/
	
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
        return inverseMethod != null;
    }
    public Object execute()   {
		try {
			Object retVal = MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   doMethod,
												   doParams
												   );	
			
			//listener.commandActionPerformed();			
			return retVal;
			
		} catch (Exception e) {
			System.out.println ("Could not execute: " + doMethod + " " + e);
			return null;
		}
    }
    public void undo()    {
		try {			
			MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   inverseMethod,
												   inverseParams);
			if (listener != null)
				listener.commandActionPerformed();
		} catch (Exception e) {
			System.out.println ("Could not undo: " + inverseMethod + " " + e);
		}
    }
    public void redo()    {
        execute();
		
		if (listener != null)
			listener.commandActionPerformed();
		
    }  

}

