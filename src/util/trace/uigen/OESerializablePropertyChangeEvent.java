package util.trace.uigen;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;

public interface OESerializablePropertyChangeEvent extends OESerializableChangeEvent {
	public PropertyChangeEvent getPropertyChangeEvent();
	public void setPropertyChangeEvent(PropertyChangeEvent anEvent);
	public String getAdapterDescription();
	public void setAdapterDescription(String anAdapter);
	int getFrameDescription();
	void setFrameDescription(int aFrameDescripton);

}
