package bus.uigen.trace;

import java.beans.PropertyChangeEvent;

import util.models.VectorChangeEvent;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.VectorAdapter;

public class VectorAdapterReceivedVectorChangeEvent extends ObjectAdapterInfo{
	VectorChangeEvent event;
	public VectorAdapterReceivedVectorChangeEvent(String aMessage, VectorAdapter aFinder,
			VectorChangeEvent anEvent) {
		super(aMessage, aFinder);
		event = anEvent;
	}
	public VectorAdapterReceivedVectorChangeEvent(String aMessage) {
		super(aMessage);		
	}
	
	public void init (ClassAdapter aFinder,
			 VectorChangeEvent anEvent) {
		event = anEvent; // this must come before the firing init
		init(aFinder);
	}
	public boolean equals (Object anObject) {
		if (!(anObject instanceof VectorAdapterReceivedVectorChangeEvent) ) {
			return super.equals(anObject);
		}
		VectorAdapterReceivedVectorChangeEvent other =  (VectorAdapterReceivedVectorChangeEvent) anObject;
		return 
				getVectorAdapter() == other.getVectorAdapter() &&
				event.getEventType() == other.getVectorChangeEvent().getEventType() &&
				event.getPosition() == other.getVectorChangeEvent().getPosition() &&
				event.getNewValue().equals(other.getVectorChangeEvent().getNewValue());
	}
	
	public VectorChangeEvent getVectorChangeEvent() {
		return event;
	}
	public VectorAdapter getVectorAdapter() {
		return (VectorAdapter) getObjectAdapter();
	}
	public static VectorAdapterReceivedVectorChangeEvent newCase(VectorAdapter aFinder,
			VectorChangeEvent anEvent) {
   	String aMessage = "Vector adapter:" + aFinder + " received vector change event:" + anEvent;
   	VectorAdapterReceivedVectorChangeEvent traceable = new VectorAdapterReceivedVectorChangeEvent(aMessage);
   	traceable.init (aFinder, anEvent);
   	traceable.announce();
   	return traceable;

	}
	

}
