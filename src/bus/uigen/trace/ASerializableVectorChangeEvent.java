package bus.uigen.trace;

import util.models.VectorChangeEvent;


public class ASerializableVectorChangeEvent implements
		OESerializableVectorChangeEvent {
	VectorChangeEvent event;
	String adapterDescription;
	int frameDescription;;

	public ASerializableVectorChangeEvent(int aFrameDescription,
			String aAdapterDescription, VectorChangeEvent anEvent) {
		adapterDescription = aAdapterDescription;
		event = anEvent;
	}

	@Override
	public VectorChangeEvent getVectorChangeEvent() {
		return event;
	}

	@Override
	public void setVectorChangeEvent(VectorChangeEvent anEvent) {
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
