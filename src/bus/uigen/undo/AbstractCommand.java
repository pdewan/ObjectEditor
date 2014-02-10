package bus.uigen.undo;
import bus.uigen.*;
import java.lang.reflect.*;
import java.util.Vector;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.StandardProxyTypes;
public abstract class AbstractCommand implements Command {	
	boolean notUndablePurgesHistory = false;
	@Override
	public boolean getNotUndoablePurgesUndoHistory() {
		// TODO Auto-generated method stub
		return notUndablePurgesHistory;
	}

	@Override
	public void setNotUndoablePurgesUndoHistory(boolean newVal) {
		// TODO Auto-generated method stub
		notUndablePurgesHistory = newVal;
	}
	public Object clone() {
		try {
		return  super.clone();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

}

