package bus.uigen.shapes;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import shapes.BoundedShape;
import shapes.Disposable;
import util.trace.Tracer;
@util.annotations.IsCompositeShape(true)

public class AnAddressSpaceTransformingListenableShapeVector extends AStackOptimizedListenableShapeVector{
	protected Map<Integer, Integer> externalToInternalIndex = new HashMap();
	protected Map<Integer, Integer> internalToExternalIndex = new HashMap();

	protected List<Integer> emptyIndices = new ArrayList();
	
	public AnAddressSpaceTransformingListenableShapeVector(int initialSize,
			BoundedShape aReference) {
		super(initialSize, aReference);
		// TODO Auto-generated constructor stub
	}
	
	public AnAddressSpaceTransformingListenableShapeVector() {
		
	}
	int translateInternal(int internalIndex) {
		if (internalIndex == -1) return -1;
		Integer retVal = internalToExternalIndex.get(internalIndex);
		if (retVal != null)
			return retVal;
		else
			return internalIndex;
	}
	
	int translateExternal(int externalIndex) {
		if (externalIndex < 0) return externalIndex;
		Integer retVal = externalToInternalIndex.get(externalIndex);
		if (retVal != null)
			return retVal;
		else
			return externalIndex;
	}
	
	void maybeConvertToNonStack() {
		if (!isStack)
			return;
//		for (int i =0; i < curSize; i++) {
//			externalToInternalIndex.put(i, i);
//			internalToExternalIndex.put(i, i);
//		}
//		for (int i = curSize; i < displaySize(); i++) {
//			emptyIndices.add(i);
//		}
//		emptyIndices.clear();
		for (int i = internalSize() - 1; i >= curSize; i--) {
			addEmptyIndex(i);
//			emptyIndices.add(i);
		}
		isStack = false;
		internalToExternalIndex.clear();
		externalToInternalIndex.clear();
	}
	
	int getNextEmptySlot() {
		if (emptyIndices.size() == 0)
			return -1;
		else {
			int retVal = emptyIndices.get(emptyIndices.size() - 1);
			emptyIndices.remove(emptyIndices.size() - 1);
			return retVal;
		}
	}
	
	int getNextEmptyCopyableSlot(BoundedShape anExternalShape) {
		Integer foundIndex = -1;
		for (Integer index: emptyIndices) {
			if (this.internalElementAt(index).copyable(anExternalShape)) {
				foundIndex = index;
				break;
			}
		}
		if (foundIndex != -1)
		
			emptyIndices.remove((Object) foundIndex );
			return foundIndex;
		
//		if (emptyIndices.size() == 0)
//			return -1;
//		else {
//			int retVal = emptyIndices.get(emptyIndices.size() - 1);
//			emptyIndices.remove(emptyIndices.size() - 1);
//			return retVal;
		
	}
	
	
	public BoundedShape elementAt(int index) {
		if (isStack)
//			return translateInternal (super.elementAt(index));
			return super.elementAt(index);
		else {
			if (index >= size()) throw new ArrayIndexOutOfBoundsException();
			int internalIndex = translateExternal(index);
			return translateInternal(super.internalElementAt(internalIndex));
			
		}
	}
	
	void bind (BoundedShape internalShape, BoundedShape externalShape, int internalIndex, int externalIndex) {
		externalToInternalShape.put(externalShape, internalShape);
		if (externalShape instanceof Observable && externalShape != internalShape) {
			((Observable) externalShape).addObserver(this);
		}
		internalToExternalShape.put(internalShape, externalShape);
		externalToInternalIndex.put(externalIndex, internalIndex);
		internalToExternalIndex.put(internalIndex, externalIndex);
		emptyIndices.remove((Integer) internalIndex);
	}
	
	
	
	void expandToInsert(BoundedShape element, int pos) {
		super.nonStackAdd(element);
		shiftIndicesDown(pos);
		bind (element, element, internalSize() - 1, pos);
	}
	
	void shiftIndicesDown(int pos) {
		for (int i = size() - 1; i > pos; i--) {
			Integer previousInternalPos = translateExternal(i - 1);
			if (previousInternalPos == null) continue;
//			int previousExistingInternalPos = externalToInternalIndex.get(pos);
			externalToInternalIndex.put(i, previousInternalPos);
			internalToExternalIndex.put(previousInternalPos, i);
			
		}
	}
	
	void shiftIndicesUp(int pos) {
		for (int i = pos; i < size() - 1; i++) {
			Integer nextInternalPos = translateExternal(i + 1);
			if (nextInternalPos == null) continue;
//			int previousExistingInternalPos = externalToInternalIndex.get(pos);
			externalToInternalIndex.put(i, nextInternalPos);
			internalToExternalIndex.put(nextInternalPos, i);
			
		}
	}
	
	protected void nonStackInsertElementAt(BoundedShape element, int pos) {
		maybeConvertToNonStack();
//		int internalPos = getNextEmptySlot();
		int internalPos = getNextEmptyCopyableSlot(element);

		BoundedShape internalShape = null;
//		shiftIndicesDown(pos);
//		for (int i = size() - 1; i > 0; i--) {
//			int previousInternalPos = externalToInternalIndex.get(pos -1);
////			int previousExistingInternalPos = externalToInternalIndex.get(pos);
//			externalToInternalIndex.put(pos, previousInternalPos);
//			internalToExternalIndex.put(previousInternalPos, pos);
//			
//		}
		if (internalPos >= 0) {
			shiftIndicesDown(pos);

			internalShape = internalElementAt(internalPos);
//			if (internalShape.getClass() == element.getClass()) {
//			if (internalShape.copyable( element)) {
				// check copy
				boolean retVal = internalShape.copy(element);	
				if (!retVal) { // unreachable code
					if (internalShape instanceof Disposable)
						((Disposable) internalShape).setDisposed(true);
					else
					internalShape.setBounds(new Rectangle (0, 0, 0, 0));
					expandToInsert(element, pos);
					return;
				}
				bind(internalShape, element, internalPos, pos);
//			} else {
//				expandToInsert(element, pos);
//				
//			}
		} else 
			expandToInsert(element, pos);
//		shiftIndicesDown(pos);

		
	}
	
	// this is crazy, OE needs internal indices. This will work only if no index operation is exeectuted
	// jhave added displayIndexOf
	public int indexOf (Object externalShape) {
		int internalIndex = super.indexOf(externalShape);
		return translateInternal(internalIndex);
	}
//	public synchronized boolean nonStackRemove(Object element) {
//		maybeConvertToNonStack();
//
//	}
	void addEmptyIndex(int pos) {
//		System.out.println("adding empty pos:" + pos);
		if (emptyIndices.contains(pos)) {
			Tracer.error("Duplicate attempt to add empty index:" + pos);
			return;
		}
		emptyIndices.add(pos);
	}
	protected BoundedShape nonStackRemoveElementAt(int pos) {
		maybeConvertToNonStack();
		Integer internalPos = translateExternal(pos);
		shiftIndicesUp(pos);
		if (internalPos < 0) return null;
		addEmptyIndex(internalPos);
//		emptyIndices.add(internalPos);

		BoundedShape internalShape = internalElementAt(internalPos);
		hide(internalShape);
//		if (internalShape instanceof Disposable)
//			((Disposable) internalShape).setDisposed(true);
//		else
//		internalShape.setBounds(new Rectangle(0, 0, 0, 0));
		return translateInternal(internalShape);
	}
	protected void nonStackAdd(BoundedShape element) {
		nonStackInsertElementAt(element, size());
	}
	protected void addNonFilled(BoundedShape element) {
		nonStackAdd(element);
	}
	public int size() {
		if (isStack)
			return super.size();
		return internalSize() - emptyIndices.size();
			
	}
	void nonStackClear() {
		for (int i = 0; i < internalSize(); i++) {
			if (emptyIndices.contains(i))
				continue;
			BoundedShape internalShape = internalElementAt(i);
			hide(internalShape);

//			if (internalShape instanceof Disposable)
//				((Disposable) internalShape).setDisposed(true);
//			else
//				internalShape.setBounds(new Rectangle(0, 0, 0, 0));

//			internalElementAt(i).setBounds(new Rectangle (0, 0, 0, 0));
		}
		emptyIndices.clear();
		isStack = true;
		curSize = 0;
	}

}
