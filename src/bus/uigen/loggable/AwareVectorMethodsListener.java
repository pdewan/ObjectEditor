package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import util.models.VectorChangeEvent;
import util.models.VectorChangeSupport;
import util.models.VectorListener;
import util.models.VectorMethodsListener;


public interface AwareVectorMethodsListener  {
	

	
	
	public void elementAdded(String hostId, Object source, Object element, int newSize) ;


	public void elementChanged(String hostId, Object source, Object element, int pos) ;

	
	public void elementInserted(String hostId, Object source, Object element, int pos,
			int newSize) ;


	public void elementRemoved(String hostId, Object source, int pos, int newSize) ;

	
	public void elementRemoved(String hostId, Object source, Object element, int newSize);

	
	public void elementsCleared(String hostId, Object source) ;

	
	
	

}
