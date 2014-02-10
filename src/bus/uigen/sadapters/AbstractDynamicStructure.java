package bus.uigen.sadapters;

import util.trace.Tracer;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.CommandListener;

public class AbstractDynamicStructure extends BeanToRecord  {

	transient MethodProxy clearMethod = null;
	
	public void clear() {
		clear(null);
	}
	
	public boolean hasClearMethod() {
		return clearMethod != null ;
	}
	public void clear(CommandListener commandListener) {
		if (clearMethod == null) {
			Tracer.error("Cannot clear.");
			return;
		}
		new MethodInvocationManager(frame, targetObject,
			     clearMethod, commandListener);
	}

}
