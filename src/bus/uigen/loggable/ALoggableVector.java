package bus.uigen.loggable;

import java.util.Collection;

import util.models.VectorChangeEvent;
import util.models.VectorChangeSupport;
import util.models.VectorListener;
import util.models.VectorMethodsListener;

public class ALoggableVector extends ALoggableRecord implements VectorListener, VectorMethodsListener {

	@Override
	public void elementAdded(Object source, Object element, int newSize) {
		vectorChangeSupport.fireElementAdded(source, element, newSize);
		
	}

	@Override
	public void elementChanged(Object source, Object element, int pos) {
		vectorChangeSupport.fireElementChanged(source, element, pos);
		
	}

	@Override
	public void elementInserted(Object source, Object element, int pos,
			int newSize) {
		vectorChangeSupport.fireElementInserted(source, element, pos, newSize);
		
	}

	@Override
	public void elementRemoved(Object source, int pos, int newSize) {
		
		vectorChangeSupport.fireElementRemoved(source, pos, newSize);
	}

	@Override
	public void elementRemoved(Object source, Object element, int newSize, int pos) {
		vectorChangeSupport.fireElementRemoved(source, element, newSize);
		
	}

	@Override
	public void elementsCleared(Object source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateVector(VectorChangeEvent evt) {
		vectorChangeSupport.fireUpdateVector(evt);
		
	}
	transient VectorChangeSupport vectorChangeSupport = new VectorChangeSupport(this);
	
	public void addVectorListener(VectorListener vectorListener) {
		vectorChangeSupport.addVectorListener(vectorListener);
	}
	public void removeVectorListener(VectorListener vectorListener) {
		vectorChangeSupport.removeVectorListener(vectorListener);
	}
	public void addVectorMethodsListener(VectorMethodsListener vectorListener) {
		vectorChangeSupport.addVectorMethodsListener(vectorListener);
	}
	public void removeVectorMethodsListener(VectorMethodsListener vectorListener) {
		vectorChangeSupport.removeVectorMethodsListener(vectorListener);
	}

	@Override
	public void elementsAdded(Object source, Collection element, int newSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementCopied(Object source, int fromIndex, int toIndex, int newSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementCopied(Object source, int fromIndex, int fromNewSize, Object to, int toIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementMoved(Object source, int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementMoved(Object source, int fromIndex, int fromNewSize, Object to, int toIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementReplaced(Object source, int fromIndex, int toIndex, int newSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementReplaced(Object source, int fromIndex, int newFromSize, Object to, int toIndex) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void elementSwapped(int index1, int index2) {
//		elementSwapped(null, index1, index2);
//	}

	@Override
	public void elementSwapped(Object newParam, int index1, int index2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementSwapped(Object source, int index1, Object other, int index2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementCopiedToUserObject(Object source, int fromIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementCopiedFromUserObject(Object source, int fromIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userObjectChanged(Object source, Object newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementRead(Object source, Object element, Integer pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tempChanged(Object source, Object newVal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementCopiedToTemp(Object source, int fromIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementCopiedFromTemp(Object source, int fromIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userObjectCopiedToTemp(Object source, Object copiedValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tempCopiedToUserObject(Object source, Object copiedValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userObjectRead(Object source, Object readValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tempRead(Object source, Object readValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementCopiedAndInserted(Object source, int fromIndex,
			int toIndex, int newSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementCopiedAndInserted(Object source, int fromIndex,
			int fromNewSize, Object to, int toIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pointerChanged(Object source, Integer pointerValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pointer2Changed(Object source, Integer pointerValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userOperationOccured(Object source, Integer aTargetIndex,
			Object anOperation) {
		// TODO Auto-generated method stub
		
	}

}
