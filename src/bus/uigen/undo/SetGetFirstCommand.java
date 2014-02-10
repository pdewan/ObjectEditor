package bus.uigen.undo;
import bus.uigen.*;
import bus.uigen.reflect.MethodProxy;

import java.lang.reflect.*;
public class SetGetFirstCommand extends bus.uigen.undo.SetCommand {
	public SetGetFirstCommand (CommandListener theListener,
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
    void assignToUndoWriteMethodParams(Object readValue) {
		Util.assignFirst (undoWriteMethodParams, readValue);
	}
	/*
	static void assignFirst(Object[] params, Object readValue) {
		params[0] = readValue;
	}
	*/
	Object[] createReadMethodParams(Object[] params) {
		return Util.removeFirst(params);
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

