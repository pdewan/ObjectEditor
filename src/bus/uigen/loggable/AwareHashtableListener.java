package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import util.models.VectorChangeEvent;
import util.models.VectorChangeSupport;
import util.models.VectorListener;
import util.models.VectorMethodsListener;


public interface AwareHashtableListener  {
	

	
	
	public void keyPut (String hostId, Object source, Object key, Object value, int newSize) ;


	public void keyRemoved(String hostId, Object source, Object key, int newSize) ;

	
	
	

}
