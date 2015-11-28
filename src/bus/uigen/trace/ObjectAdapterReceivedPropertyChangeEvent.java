package bus.uigen.trace;

import java.beans.PropertyChangeEvent;

import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;

public class ObjectAdapterReceivedPropertyChangeEvent extends PropertyChangeEventInfo{
	PropertyChangeEvent event;

	public ObjectAdapterReceivedPropertyChangeEvent(ObjectAdapter aFinder,
			 PropertyChangeEvent anEvent) {
		super("Object adapter:" + aFinder + " received property change event:" + anEvent, aFinder, anEvent);
		event = anEvent;
	}
	
	public ObjectAdapterReceivedPropertyChangeEvent(String aMessage) {
		super(aMessage);		
	}
//	public void init (ClassAdapter aFinder,
//			 PropertyChangeEvent anEvent) {
//		event = anEvent; // this must come before the firing init
//		init(aFinder);
//	}
//	
//	public PropertyChangeEvent getPropertyChangeEvent() {
//		return event;
//	}
//	public ClassAdapter getClassAdapter() {
//		return (ClassAdapter) getObjectAdapter();
//	}
//	public boolean equals (Object anObject) {
//		if (!(anObject instanceof ClassAdapterReceivedPropertyChangeEvent) ) {
//			return super.equals(anObject);
//		}
//		ClassAdapterReceivedPropertyChangeEvent other =  (ClassAdapterReceivedPropertyChangeEvent) anObject;
//		return 
//				getClassAdapter() == other.getClassAdapter() &&
//				event.getPropertyName().equalsIgnoreCase(other.getPropertyChangeEvent().getPropertyName()) &&
//				event.getNewValue().equals(other.getPropertyChangeEvent().getNewValue());
//			
//	}
	public static ObjectAdapterReceivedPropertyChangeEvent newCase(ObjectAdapter aFinder,
			PropertyChangeEvent anEvent) {
//    	String aMessage = "Class adapter:" + aFinder + " received property change event:" + anEvent;
    	ObjectAdapterReceivedPropertyChangeEvent traceable =
    			new ObjectAdapterReceivedPropertyChangeEvent(aFinder, anEvent);
//    	traceable.init(aFinder, anEvent);
    	traceable.announce();
    	return traceable;
	}
	

}
