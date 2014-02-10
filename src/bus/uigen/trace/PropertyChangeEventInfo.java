package bus.uigen.trace;

import java.beans.PropertyChangeEvent;

import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.reflect.ClassProxy;

public class PropertyChangeEventInfo extends ObjectAdapterInfo{
	PropertyChangeEvent event;

	public PropertyChangeEventInfo(String aMessage, ClassAdapter aFinder,
			 PropertyChangeEvent anEvent) {
		super(aMessage, aFinder);
		event = anEvent;
	}
	
	public PropertyChangeEventInfo(String aMessage) {
		super(aMessage);		
	}
	public void init (ClassAdapter aFinder,
			 PropertyChangeEvent anEvent) {
		event = anEvent; // this must come before the firing init
		init(aFinder);
	}
	
	public PropertyChangeEvent getPropertyChangeEvent() {
		return event;
	}
	public ClassAdapter getClassAdapter() {
		return (ClassAdapter) getObjectAdapter();
	}
	public boolean equals (Object anObject) {
		if (!(anObject instanceof PropertyChangeEventInfo) ) {
			return super.equals(anObject);
		}
		PropertyChangeEventInfo other =  (PropertyChangeEventInfo) anObject;
		return 
				getClassAdapter() == other.getClassAdapter() &&
				event.getPropertyName().equalsIgnoreCase(other.getPropertyChangeEvent().getPropertyName()) &&
				event.getNewValue().equals(other.getPropertyChangeEvent().getNewValue());
			
	}
//	public static PropertyChangeEventInfo newCase(ClassAdapter aFinder,
//			PropertyChangeEvent anEvent) {
//    	String aMessage = "Class adapter:" + aFinder + " received property change event:" + anEvent;
//    	PropertyChangeEventInfo traceable =
//    			new PropertyChangeEventInfo(aFinder, anEvent);
////    	traceable.init(aFinder, anEvent);
//    	traceable.announce();
//    	return traceable;
//	}
	

}
