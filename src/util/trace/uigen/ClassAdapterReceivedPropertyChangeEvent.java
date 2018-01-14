package util.trace.uigen;

import java.beans.PropertyChangeEvent;

import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.reflect.ClassProxy;

public class ClassAdapterReceivedPropertyChangeEvent extends ObjectAdapterPropertyChangeEventInfo{
	PropertyChangeEvent event;

	public ClassAdapterReceivedPropertyChangeEvent(ClassAdapter aFinder,
			 PropertyChangeEvent anEvent) {
		super("Class adapter:" + aFinder + " received property change event:" + anEvent, aFinder, anEvent);
		event = anEvent;
	}
	
	public ClassAdapterReceivedPropertyChangeEvent(String aMessage) {
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
	public static ClassAdapterReceivedPropertyChangeEvent newCase(ClassAdapter aFinder,
			PropertyChangeEvent anEvent) {
//    	String aMessage = "Class adapter:" + aFinder + " received property change event:" + anEvent;
    	ClassAdapterReceivedPropertyChangeEvent traceable =
    			new ClassAdapterReceivedPropertyChangeEvent(aFinder, anEvent);
//    	traceable.init(aFinder, anEvent);
    	traceable.announce();
    	return traceable;
	}
	

}
