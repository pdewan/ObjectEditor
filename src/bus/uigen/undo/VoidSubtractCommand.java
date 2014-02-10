package bus.uigen.undo;
import bus.uigen.*;
import java.lang.reflect.*;
import java.util.Vector;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
public abstract class VoidSubtractCommand extends SubtractCommand {	
    MethodProxy readMethod;
	public VoidSubtractCommand (CommandListener theListener,
						  MethodProxy theSubtractMethod,
						  Object theParentObject,
						  Object[] theSubtractParams,
						  MethodProxy theAddMethod,
						  //Object[] theReadMethodParams,
						  MethodProxy theReadMethod
						  ) {
        super(theListener, theSubtractMethod, theParentObject, theSubtractParams, 
			  theAddMethod);
		readMethod = theReadMethod;
		addParams = createAddParams(theSubtractParams);

    }
	public Object getObject() {
		return parentObject;
	}
	public MethodProxy getMethod() {
		return subtractMethod;
	}
	
    public boolean isUndoable()
    {
        return super.isUndoable() && readMethod != null;
    }
    Object subtractedValue;
    public Object execute()   {
		try {
			//System.out.println("Before invoke read method");
			/*
			Object subtractedValue =
			*/
			if (subtractedValue == null)
					subtractedValue =		MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   readMethod,
												   subtractParams);
												   
			//System.out.println("After invoke read method");
			Object retVal = MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   subtractMethod,
												   subtractParams);
			assignToAddParams(subtractedValue);
			//listener.commandActionPerformed();
			return retVal;
			
		} catch (Exception e) {
			System.out.println ("Could not execute: " + subtractMethod + " " + e);
			return null;
		}
    }   

}

