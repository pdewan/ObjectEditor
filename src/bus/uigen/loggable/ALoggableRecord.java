package bus.uigen.loggable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

//import bus.uigen.oadapters.uiObjectAdapter;

public class ALoggableRecord extends ACompositeLoggable implements PropertyChangeListener {
	transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this) ;
	public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
        //uiObjectAdapter.maybeAddPropertyChangeListener(getRealObject(), this);
    }
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		propertyChangeSupport.firePropertyChange(evt);
		
	}
	

}
