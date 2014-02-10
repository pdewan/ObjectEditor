package bus.uigen.undo;
import bus.uigen.*;
import bus.uigen.reflect.MethodProxy;

import java.lang.reflect.*;
public class SetGetLastCommand extends SetCommand {
	public SetGetLastCommand (CommandListener theListener,
						  MethodProxy theWriteMethod,
						  Object theParentObject,
						  Object[] theParams,
						  MethodProxy theReadMethod//,
						  //Object[] theReadMethodParams
						  ) {
        super(theListener,
			  theWriteMethod,
			  theParentObject,
			  theParams,
			  theReadMethod//,
			  //theReadMethodParams
			  );
    }	
    Object[] createReadMethodParams(Object[] params) {
		return removeLast(params);
	}
	 
	 static Object[] removeLast(Object[] params) {
		Object[] readMethodParams = new Object[params.length - 1];
		for (int i = 0; i < params.length - 1; i ++) {
			readMethodParams[i] = params[i];
		}
		return readMethodParams;
	}
	void assignToUndoWriteMethodParams(Object readValue) {
		assignLast(undoWriteMethodParams, readValue);
	}
	static void assignLast(Object[] undoWriteMethodParams, Object readValue) {
		undoWriteMethodParams[undoWriteMethodParams.length - 1] = readValue;
	}
	

}

