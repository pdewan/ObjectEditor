package bus.uigen.undo;
import bus.uigen.*;
import bus.uigen.reflect.MethodProxy;

import java.lang.reflect.*;
public class SubtractAddFirstCommand extends SubtractCommand {
	public SubtractAddFirstCommand (CommandListener theListener,
						 MethodProxy theSubtractMethod,
						  Object theParentObject,
						  Object[] theSubtractParams,
						 MethodProxy theAddMethod//,
						 //VirtualMethod theElementAtMethod//,
						  //Object[] theReadMethodParams
						  ) {
        super(theListener,
			  theSubtractMethod,
			  theParentObject,
			  theSubtractParams,
			  theAddMethod//,
			  //theElementAtMethod//,
			  //theReadMethodParams
			  );
    }	
    void assignToAddParams(Object readValue) {
		Util.assignFirst (addParams, readValue);
	}
	/*
	static void assignFirst(Object[] params, Object readValue) {
		params[0] = readValue;
	}
	*/
	Object[] createAddParams(Object[] params) {
		return Util.addFirst(params);
	}
	/*
	 static Object[] removeFirst(Object[] params) {
		Object[] readMethodParams = new Object[params.length - 1];
		for (int i = 1; i < params.length; i++) {
			readMethodParams[i-1] = params[i];
		}
		return readMethodParams;
	}
	*/

}

