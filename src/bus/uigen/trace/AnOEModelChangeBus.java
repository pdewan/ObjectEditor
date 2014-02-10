package bus.uigen.trace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.trace.Traceable;
import util.trace.TraceableBus;
import bus.uigen.OEFrame;
import bus.uigen.uiFrameList;

public class AnOEModelChangeBus implements OEModelChangeBus {
	Set<ModelChangeListener> modelChangeListeners = new HashSet();
	List<ObjectAdapterInfo> executedEvents = new ArrayList();
	
	public AnOEModelChangeBus() {
		TraceableBus.addTraceableListener(this);
	}

	@Override
	public void newEvent(Exception aTraceable) {
		if (aTraceable instanceof ExecutedClassAdapterPropertyChangeEvent) {
			executedEvents.add((ExecutedClassAdapterPropertyChangeEvent)aTraceable);
		} else if (aTraceable instanceof ExecutedVectorAdapterVectorChangeEvent) {
			executedEvents.add((ExecutedVectorAdapterVectorChangeEvent)aTraceable);
		} else if (aTraceable instanceof ClassAdapterReceivedPropertyChangeEvent) {
			if (executedEvents.contains(aTraceable)) {
				return; // bounce back, will not work if multiple editable events fired by one change
			}			
			ClassAdapterReceivedPropertyChangeEvent eventInfo = (ClassAdapterReceivedPropertyChangeEvent) aTraceable;
			OEFrame frame = eventInfo.getClassAdapter().getUIFrame();
			int frameNo = uiFrameList.indexOfFrame(frame);
			String path = eventInfo.getClassAdapter().getCompletePathOnly();
			notifySerializablePropertyChangeEvent(
					new ASerializablePropertyChangeEvent(frameNo, path, eventInfo.getPropertyChangeEvent()));
		
		} else if (aTraceable instanceof VectorAdapterReceivedVectorChangeEvent) {
			if (executedEvents.contains(aTraceable)) {
				return; // bounce back, will not work if multiple editable events fired by one change
			}	
			VectorAdapterReceivedVectorChangeEvent eventInfo = (VectorAdapterReceivedVectorChangeEvent) aTraceable;
			OEFrame frame = eventInfo.getVectorAdapter().getUIFrame();
			int frameNo = uiFrameList.indexOfFrame(frame);
			String path = eventInfo.getVectorAdapter().getCompletePathOnly();
			notifySerializableVectorChangeEvent(
					new ASerializableVectorChangeEvent(frameNo, path, eventInfo.getVectorChangeEvent()));
		}
	}

	@Override
	public void addModelChangeListener(ModelChangeListener aModelChangeListener) {
		modelChangeListeners.add(aModelChangeListener);
	}
	
	void notifySerializablePropertyChangeEvent(OESerializablePropertyChangeEvent anEvent) {
		for (OESerializablePropertyChangeListener listener:modelChangeListeners) {
			listener.propertyChanged(anEvent);
		}
		
	}
	void notifySerializableVectorChangeEvent(OESerializableVectorChangeEvent anEvent) {
		for (OESerializableVectorChangeListener listener:modelChangeListeners) {
			listener.updateVector(anEvent);
		}
		
	}
}
