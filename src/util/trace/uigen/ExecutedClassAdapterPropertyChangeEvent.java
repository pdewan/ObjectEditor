package util.trace.uigen;

import java.beans.PropertyChangeEvent;

import bus.uigen.oadapters.ClassAdapter;

public class ExecutedClassAdapterPropertyChangeEvent 
              extends ObjectAdapterPropertyChangeEventInfo{

	public ExecutedClassAdapterPropertyChangeEvent(
			ClassAdapter aFinder, PropertyChangeEvent anEvent) {
		super("Class adapter:" + aFinder + " processed property change event:" + anEvent, aFinder, anEvent);
	}
	public ExecutedClassAdapterPropertyChangeEvent(String aMessage) {
		super(aMessage);		
	}
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
	
	public static ExecutedClassAdapterPropertyChangeEvent newCase(ClassAdapter aFinder,
			PropertyChangeEvent anEvent) {
//    	String aMessage = "Class adapter:" + aFinder + " processed property change event:" + anEvent;
    	ExecutedClassAdapterPropertyChangeEvent traceable = 
    			new ExecutedClassAdapterPropertyChangeEvent(aFinder, anEvent);
    	traceable.announce();
    	return traceable;
	}

}
