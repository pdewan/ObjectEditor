package bus.uigen.trace;

import util.models.VectorChangeEvent;
import util.trace.Tracer;
import bus.uigen.OEFrame;
import bus.uigen.uiFrameList;
import bus.uigen.oadapters.ClassAdapter;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.oadapters.VectorAdapter;
import bus.uigen.sadapters.RecordStructure;
import bus.uigen.sadapters.VectorStructure;

public class AnOEModelChanger implements OEModelChanger{
	
	ObjectAdapter getAdapter(OESerializableChangeEvent anEvent) {
		int frameIndex = anEvent.getFrameDescription();
		if (frameIndex < 0) {
			Tracer.error("Illegal frame description in " + anEvent);
			return null;
		}
		OEFrame frame = uiFrameList.getFrame(frameIndex);
		ObjectAdapter retVal =  frame.getObjectAdapterFromPath(anEvent.getAdapterDescription());	
		if (retVal == null) {
			Tracer.error("No class adapter for: " + anEvent.getAdapterDescription());
		}
		return retVal;
		
		
	}
	@Override
	public void propertyChanged(OESerializablePropertyChangeEvent anEvent) {
		ClassAdapter aClassAdapter = (ClassAdapter) getAdapter(anEvent);		
		RecordStructure aRecordStructure = aClassAdapter.getRecordStructure();
		String aPropertyName  = anEvent.getPropertyChangeEvent().getPropertyName();
		if (aRecordStructure.isReadOnly(aPropertyName))
			return;
		Object currentPropertyValue =  aRecordStructure.get(aPropertyName);
		Object newPropertyValue = anEvent.getPropertyChangeEvent().getNewValue();
		
		if (   
				(currentPropertyValue != null &&  currentPropertyValue.equals(newPropertyValue)) || // == will never work for composite structures
				(currentPropertyValue == null && newPropertyValue == null)) // prevents bounce back
				return;
		ExecutedClassAdapterPropertyChangeEvent.newCase(aClassAdapter, anEvent.getPropertyChangeEvent());
		aRecordStructure.set(aPropertyName, newPropertyValue);
		
		
	}

	@Override
	public void updateVector(OESerializableVectorChangeEvent anEvent) {
		VectorAdapter aVectorAdapter = (VectorAdapter) getAdapter(anEvent);		
		VectorStructure aVectorStructure = aVectorAdapter.getVectorStructure();
		int currentSize = aVectorStructure.size();
		VectorChangeEvent vectorChangeEvent = anEvent.getVectorChangeEvent();
		int newSize = vectorChangeEvent.getNewSize(); 
		// works as long as one insertion at a time and no concurrent
//		if (currentSize == newSize) return; // bounce back
		ExecutedVectorAdapterVectorChangeEvent.newCase(aVectorAdapter, anEvent.getVectorChangeEvent());

		switch (vectorChangeEvent.getEventType()) {
		case VectorChangeEvent.AddComponentEvent: {
			aVectorStructure.addElement(vectorChangeEvent.getNewValue(), null);
			break;
		}
		case VectorChangeEvent.InsertComponentEvent: {
			aVectorStructure.insertElementAt(vectorChangeEvent.getNewValue(), vectorChangeEvent.getPosition(), null);
			break;
		}
		case VectorChangeEvent.DeleteComponentEvent: {
			aVectorStructure.removeElementAt(vectorChangeEvent.getPosition(), null);
			break;
		}
		case VectorChangeEvent.ClearEvent: {
			aVectorStructure.clear();
			break;
		}
		default: {
			Tracer.error("Cannot do update of vector event type:" + vectorChangeEvent.getEventType());
		}	
		
		
	}

}
}
