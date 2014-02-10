package bus.uigen.controller.models;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.controller.ObjectParameterListener;


public interface InteractiveActualParameter extends ObjectParameterListener, PropertyChangeListener, Serializable {
	
	public String getName() ;
	public boolean preGetType() ;
	public Object getType() ;
	public boolean preSetType() ;
	public void setType(Object newVal); 
	
	public Object getValue() ;
	
	public boolean preSetValueObject() ;
	
	public void setValue(Object newVal) ;
	public void setInvocationManager (MethodInvocationManager theInvocationManager) ;
	public void newUserValue(int theParameterNumber, Object theValue) ;
	public void addPropertyChangeListener(PropertyChangeListener l) ;
	public void reset() ;
			
	

}
