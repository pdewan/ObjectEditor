package bus.uigen.trace;

import java.beans.PropertyChangeEvent;

import util.models.VectorChangeEvent;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.VectorAdapter;

public class ExecutedVectorAdapterVectorChangeEvent 
	 		extends VectorAdapterReceivedVectorChangeEvent{
	public ExecutedVectorAdapterVectorChangeEvent(String aMessage, VectorAdapter aFinder,
			VectorChangeEvent anEvent) {
		super(aMessage, aFinder, anEvent);
	}
	public ExecutedVectorAdapterVectorChangeEvent(String aMessage) {
		super(aMessage);		
	}
	
//	public boolean equals (Object anObject) {
//		if (!(anObject instanceof VectorAdapterReceivedVectorChangeEvent) ) {
//			return super.equals(anObject);
//		}
//		VectorAdapterReceivedVectorChangeEvent other =  (VectorAdapterReceivedVectorChangeEvent) anObject;
//		return 
//				getVectorAdapter() == other.getVectorAdapter() &&
//				event.getEventType() == other.getVectorChangeEvent().getEventType() &&
//				event.getPosition() == other.getVectorChangeEvent().getPosition() &&
//				event.getNewValue() == other.getVectorChangeEvent().getNewValue();
//	}
	public static ExecutedVectorAdapterVectorChangeEvent newCase(VectorAdapter aFinder,
			VectorChangeEvent anEvent) {
    	String aMessage = "Executed vector adapter:" + aFinder + "  property change event:" + anEvent;
    	ExecutedVectorAdapterVectorChangeEvent traceable = new ExecutedVectorAdapterVectorChangeEvent(aMessage);
    	traceable.init(aFinder, anEvent);
    	traceable.announce();
    	return traceable;

	}
	

}
