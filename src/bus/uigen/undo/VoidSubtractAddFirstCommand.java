package bus.uigen.undo;
import bus.uigen.*;
import bus.uigen.reflect.MethodProxy;

import java.lang.reflect.*;
public class VoidSubtractAddFirstCommand extends VoidSubtractCommand {
	public VoidSubtractAddFirstCommand (CommandListener theListener,
						  MethodProxy theSubtractMethod,
						  Object theParentObject,
						  Object[] theSubtractParams,
						  MethodProxy theAddMethod,
						  //Object[] theReadMethodParams
						  MethodProxy theReadMethod
						  ) {
        super(theListener,
			  theSubtractMethod,
			  theParentObject,
			  theSubtractParams,
			  theAddMethod,
			  //theReadMethodParams
			  theReadMethod
			  );		
    }	
    void assignToAddParams(Object readValue) {
		Util.assignFirst (addParams, readValue);
	}
	
	Object[] createAddParams(Object[] params) {
		return Util.addFirst(params);
	}

}

