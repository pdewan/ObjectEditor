package bus.uigen.trace;

import java.beans.PropertyChangeEvent;

import util.models.VectorChangeEvent;
import bus.uigen.oadapters.ObjectAdapter;
// to be done later
public class ObjectAdapterReceivedTableChangeEvent extends ObjectAdapterInfo{
	VectorChangeEvent event;

	public ObjectAdapterReceivedTableChangeEvent(String aMessage, 
			ObjectAdapter anObjectAdapter, VectorChangeEvent anEvent) {
		super(aMessage, anObjectAdapter);
	}
	
	public VectorChangeEvent getPropertyChangeEvent() {
		return event;
	}
	

}
