package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import util.models.HashtableListener;
import util.models.VectorChangeEvent;
import util.models.VectorListener;


public class ANotificationBus implements PropertyChangeListener, VectorListener, HashtableListener {

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateVector(VectorChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPut(Object source, Object key, Object value, int newSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyRemoved(Object source, Object key, int newSize) {
		// TODO Auto-generated method stub
		
	}

}
