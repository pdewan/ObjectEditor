package util.trace.uigen;

import java.io.Serializable;

import util.models.VectorChangeEvent;

public interface OESerializableVectorChangeEvent extends OESerializableChangeEvent {
	public VectorChangeEvent getVectorChangeEvent();
	public void setVectorChangeEvent(VectorChangeEvent anEvent);
	

}
