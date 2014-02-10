package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import util.models.VectorChangeEvent;
import util.models.VectorChangeSupport;
import util.models.VectorListener;
import util.models.VectorMethodsListener;


public interface AwarePropertyChangeListener  {
	
		public void propertyChange(String hostId, PropertyChangeEvent evt) ;
		
	

}
