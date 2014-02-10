package bus.uigen.trace;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import util.trace.Tracer;

public class ABeanChanger implements PropertyChangeListener{

	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		if (!(anEvent.getSource() instanceof String)) {
				Tracer.error("Expecting an object adapter path as source");
				return;
		}
		
	}

	

}
