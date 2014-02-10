package bus.uigen.controller.models;

import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.reflect.MethodProxy;
import util.annotations.Column;
import util.annotations.Explanation;
import util.annotations.Row;
import util.annotations.Visible;
import util.models.DynamicCommands;

public interface InteractiveMethodInvoker extends DynamicCommands{
	public InteractiveActualParameter[] getParameters() ;
	public boolean preAutoClose();
	public boolean getAutoClose() ;
	public void setAutoClose(boolean newVal) ;
	public boolean getAutoReset() ;
	public void setAutoReset(boolean newVal) ;
	public String getMethodDisplayName() ;	
	public MethodProxy getMethod() ;
	public void doImplicitInvoke(boolean theNewValue);
	public void setInvocationManager (MethodInvocationManager theInvocationManager);
	void resetAll();


}
