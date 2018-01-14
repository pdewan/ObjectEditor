package util.trace.uigen;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;

public interface OESerializableChangeEvent extends Serializable {

	public String getAdapterDescription();
	public void setAdapterDescription(String anAdapter);
	int getFrameDescription();
	void setFrameDescription(int aFrameDescripton);

}
