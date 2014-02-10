package bus.uigen.undo;
import bus.uigen.*;
import bus.uigen.reflect.MethodProxy;

import java.lang.reflect.*;
import java.util.Vector;
public  class AddSubtractLastCommand extends AddCommand {
	public AddSubtractLastCommand (CommandListener theListener,
						  MethodProxy theAddMethod,
						  Object theParentObject,
						  Object[] theAddParams,
						  MethodProxy theSubtractMethod//,
						  //Object[] theReadMethodParams
						  ) {
        //frame = theFrame;
        super(theListener,
			  theAddMethod,
			  theParentObject,
			  theAddParams,
			  theSubtractMethod);

    }
	Object[] createSubtractParams(Object[] addParams) {
		return Util.removeLast(addParams);
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
    
}

