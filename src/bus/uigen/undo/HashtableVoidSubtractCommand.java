package bus.uigen.undo;
import bus.uigen.*;
import java.lang.reflect.*;
import java.util.Vector;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
public  class HashtableVoidSubtractCommand extends SubtractCommand { 
	// not using this method for now
    MethodProxy readMethod, indexMethod;
    
	public HashtableVoidSubtractCommand (CommandListener theListener,
						  MethodProxy theSubtractMethod,
						  Object theParentObject,
						  Object[] theSubtractParams,
						  MethodProxy theAddMethod,
						  //Object[] theReadMethodParams,
						  MethodProxy theReadMethod,
						  MethodProxy theIndexMethod
						  ) {
        super(theListener, theSubtractMethod, theParentObject, theSubtractParams, 
			  theAddMethod);
		readMethod = theReadMethod;
		indexMethod = theIndexMethod;
    }
	public Object getObject() {
		return parentObject;
	}
	public MethodProxy getMethod() {
		return subtractMethod;
	}
	
    public boolean isUndoable()
    {
        return super.isUndoable() && readMethod != null && indexMethod != null;
    }
    public Object execute()   {
		try {
			
			Object subtractedIndex = MethodInvocationManager.invokeMethod(//frame,
					   parentObject,
					   indexMethod,
					   subtractParams);
			/*
			Object[] indices = new Object[1];
			
			indices[0] = subtractedIndex;
			Object subtractedValue =
									uiMethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   readMethod,
												   indices);
			*/
			Object retVal = MethodInvocationManager.invokeMethod(//frame,
												   parentObject,
												   subtractMethod,
												   subtractParams);
			assignToAddParams(subtractedIndex);
			//listener.commandActionPerformed();
			return retVal;
			
		} catch (Exception e) {
			System.out.println ("Could not execute: " + subtractMethod + " " + e);
			return null;
		}
    }  
    void assignToAddParams(Object readValue) {
		Util.assignLast (addParams, readValue);
	}
	
	Object[] createAddParams(Object[] params) {
		return Util.addLast(params);
	}

}

