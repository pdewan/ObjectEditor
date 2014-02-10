package bus.uigen.trace;

import java.beans.PropertyChangeEvent;

public class ASerializablePropertyChangeEvent implements OESerializablePropertyChangeEvent {
	PropertyChangeEvent event;
	String adapterDescription;
	int frameDescription;;
	public ASerializablePropertyChangeEvent(int aFrameDescription, String aAdapterDescription, PropertyChangeEvent anEvent) {
		adapterDescription = aAdapterDescription;
		event = anEvent;		
	}
	
	@Override
	public PropertyChangeEvent getPropertyChangeEvent() {
		return event;
	}

	@Override
	public void setPropertyChangeEvent(PropertyChangeEvent anEvent) {
		event = anEvent;
	}

	@Override
	public String getAdapterDescription() {
		return adapterDescription;
	}

	@Override
	public void setAdapterDescription(String anAdapter) {
		adapterDescription = anAdapter;
	}
	
	@Override
	public int getFrameDescription() {
		return frameDescription;
	}

	@Override
	public void setFrameDescription(int aFrameDescripton) {
		frameDescription = aFrameDescripton;
	}
	

}
