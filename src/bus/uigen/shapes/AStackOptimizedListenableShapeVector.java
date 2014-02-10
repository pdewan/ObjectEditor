package bus.uigen.shapes;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import shapes.BoundedShape;
import shapes.Disposable;
import shapes.FlexibleShape;
import util.models.AListenableVector;
import util.models.Hashcodetable;
@util.annotations.IsCompositeShape(true)

public class AStackOptimizedListenableShapeVector 
        extends AListenableShapeVector implements Observer{
	protected int curSize = 0;
	protected Hashcodetable<BoundedShape, BoundedShape> externalToInternalShape = new Hashcodetable();
	protected Hashcodetable<BoundedShape, BoundedShape> internalToExternalShape = new Hashcodetable();

	boolean isStack = true;
	
	public AStackOptimizedListenableShapeVector() {
		
	}
	
	void hide (BoundedShape anInternalShape) {
		if (anInternalShape == null) return;
		if (!(anInternalShape instanceof Disposable) )
//				|| 
//				anInternalShape == translateInternal(anInternalShape)) // user shape., do not muck with it; actually sometimes we do want it disposed
			
				anInternalShape.setBounds (new Rectangle(0, 0, 0, 0));
		else
			((Disposable) anInternalShape).setDisposed(true);
		
	}
	
	public AStackOptimizedListenableShapeVector(int initialSize, BoundedShape aReference) {
		for (int i = 0; i < initialSize; i++) {
			BoundedShape newElement;
			try {
				newElement = aReference.getClass().newInstance();
//				hide(newElement);
				if (newElement instanceof Disposable)
					((Disposable) newElement).setDisposed(true);
				else
				newElement.setBounds (new Rectangle(0, 0, 0, 0));
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				return;
			}
			super.add(newElement);
		}
//		curSize = initialSize;
		
	}
	BoundedShape translateInternal(BoundedShape internalShape) {
		if (internalShape == null) return null;
		BoundedShape retVal = internalToExternalShape.get(internalShape);
		if (retVal != null)
			return retVal;
		else
			return internalShape;
	}
	
	BoundedShape translateExternal(BoundedShape externalShape) {
		if (externalShape == null) return null;
		BoundedShape retVal = externalToInternalShape.get(externalShape);
		if (retVal != null)
			return retVal;
		else
			return externalShape;
	}
	@Override
	public int indexOf (Object externalShape) {
		int internalIndex = super.indexOf(translateExternal((BoundedShape) externalShape));
		return internalIndex;
	}
	
	public BoundedShape get(int index) {
		return elementAt(index);
	}
		
//	BoundedShape nonStackElementAt(int index) {
//		BoundedShape internalShape = super.displayElementAt(index);
//		return translateInternal(internalShape);
//	}
	public BoundedShape elementAt(int index) {
//		if (isStack) {
			BoundedShape internalShape = super.internalElementAt(index);
			return translateInternal(internalShape);
//		}
//		else return nonStackElementAt(index);
//		BoundedShape internalShape = super.displayElementAt(index);
//		return translateInternal(internalShape);
//		if (internalShape != null) {
//			BoundedShape retVal = internalToExternalShape.get(internalShape);
//			if (retVal != null)
//				return retVal;
//			else
//				return internalShape;
//		}
		
		
//		return null;
	}
	
	protected void nonStackInsertElementAt(BoundedShape element, int pos) {
		isStack = false;
		
		super.insertElementAt(element, pos);
	}
	
	protected BoundedShape nonStackRemoveElementAt(int pos) {
		isStack = false;
		return super.remove(pos);
	}
	
	protected void nonStackAdd(BoundedShape element) {
//		if (curSize < super.size()) {
//			set(curSize, element);
//			externalToInternalShape.put(element, element);
//		} else {
			super.addElement(element);
//			externalToInternalShape.put(element, element);
//			internalToExternalShape.put(element, element);
			curSize++;

//		}
	}
	protected void addNonFilled(BoundedShape element) {
		set(curSize, element);
	}
	public synchronized boolean add(BoundedShape element) {
		if (!isStack) {
			nonStackAdd(element);
		}
		else if (curSize < super.size()) {
			BoundedShape existingShape = internalElementAt(curSize);
			if (/*existingShape.getClass() == element.getClass() &&*/ existingShape.copyable(element) ) {
//				if (!existingShape.copy(element);
				existingShape.copy(element);
				internalToExternalShape.put(existingShape, element);
				externalToInternalShape.put(element, existingShape);
				
				if (element instanceof Observable && existingShape != element) {
					((Observable) element).addObserver(this);
				}
//				curSize++;
			} else {
				addNonFilled(element);
//				set(curSize, element);
//				externalToInternalShape.put(element, element);

			}			
		} else {
			super.add(element);
//			externalToInternalShape.put(element, element);
		}
		curSize++;
		return true;
		
	}
	public synchronized void addElement(BoundedShape element) {
		add(element);
		
	}
//	@Override
//	public synchronized boolean remove (Object element) {
//		return removeElement(element);
//	}
//	public synchronized boolean nonStackRemove(Object element) {
//		return super.remove(element);
//	}
//	
//	@Override
//	public synchronized boolean remove(Object element) {
//		if (isStack) {
//			BoundedShape internalShape = externalToInternalShape.get((BoundedShape) element);
//			if (internalShape == null)
//				return false;
//			internalShape.setBounds(new Rectangle (0, 0, 0, 0));
//			return true;
//			
//		} else
//			return nonStackRemove(element);
//	}
//	@Override
//	public synchronized boolean remove(Object element) {
//		return removeElement(element);
////		int index = indexOf(element);
////		if (index < 0)
////			return false;
////		 remove(index);
////		 return true;
////		if (isStack) {
////			BoundedShape internalShape = externalToInternalShape.get((BoundedShape) element);
////			if (internalShape == null)
////				return false;
////			internalShape.setBounds(new Rectangle (0, 0, 0, 0));
////			return true;
////			
////		} else
////			return nonStackRemove(element);
//	}
	public void  removeElementAt(int pos) {
		remove(pos);
	}

	public synchronized BoundedShape remove(int pos) {
		BoundedShape element = null;
		if (pos == curSize-1 && isStack) {
			   element = internalElementAt(pos);
			   if (element == null)
				   return null;
			   hide(element);
//			   if (element instanceof Disposable)
//					((Disposable) element).setDisposed(true);
//			   else 
//				   element.setBounds(new Rectangle(0, 0, 0, 0));
			   curSize--;
			   return element;
			} else {
				return nonStackRemoveElementAt(pos);
//				isStack = false;
//				super.removeElementAt(pos);
			}
	}
	@Override
	public synchronized boolean remove(Object element) {
		return removeElement((BoundedShape) element);
//		int index = indexOf(element);
//		if (index < 0)
//			return false;
//		 remove(index);
//		 return true;
//		if (isStack) {
//			BoundedShape internalShape = externalToInternalShape.get((BoundedShape) element);
//			if (internalShape == null)
//				return false;
//			internalShape.setBounds(new Rectangle (0, 0, 0, 0));
//			return true;
//			
//		} else
//			return nonStackRemove(element);
	}
	@Override
	public synchronized boolean removeElement(BoundedShape element) {
//		BoundedShape internalElement = externalToInternalShape.get(element);
		int pos = indexOf(element);
		if (pos != -1) {
		removeElementAt(pos);
		return true;
		} else
			return false;
		
//		if (internalElement != null) {
//		internalElement.setBounds(new Rectangle(0, 0, 0, 0));
//		curSize --;
//		return true;
//		} else return false;
	}
//	int indexOf (BoundedShape externalShape) {
//		BoundedShape internalShape = externalToInternalShape.get(externalShape);
//		return super.indexOf(internalShape);
//	}
//	public void removeElementAt(int pos) {
//		if (pos == curSize && isStack) {
//		   BoundedShape element = get(pos);
//		   element.setBounds(new Rectangle(0, 0, 0, 0));
//		   curSize--;
//		} else {
//			nonStackRemoveElementAt(pos);
////			isStack = false;
////			super.removeElementAt(pos);
//		}
//	}
	
	public synchronized boolean olddAll(
			Collection<? extends BoundedShape> appendedCollection) {
		int numEmptySlots = internalSize() - size();
		if (numEmptySlots == 0) {
			curSize += appendedCollection.size();
			return super.addAll(appendedCollection);
		}
		int numNewSlots = appendedCollection.size() - numEmptySlots;
		List<BoundedShape> newSlotsList = new ArrayList();
		int i = 0;
		for (BoundedShape aShape: appendedCollection) {
			if (numNewSlots <= 0 || i < numNewSlots) {
				add(aShape);
			} else {
				newSlotsList.add(aShape);
				externalToInternalShape.put(aShape, aShape);
			}
			i++;
		}
		if (newSlotsList.size() > 0)
		  return super.addAll(appendedCollection);
		else
			return true;
	}
	
	@Override
	public synchronized boolean addAll(
			Collection<? extends BoundedShape> appendedCollection) {
//		int numEmptySlots = internalSize() - size();
//		if (numEmptySlots == 0) {
//			curSize += appendedCollection.size();
//			return super.addAll(appendedCollection);
//		}
//		int numNewSlots = appendedCollection.size() - numEmptySlots;
//		List<BoundedShape> newSlotsList = new ArrayList();
//		int i = 0;
		for (BoundedShape aShape: appendedCollection) {
//			if  (i < numEmptySlots) {
				add(aShape);
//			} else {
//				newSlotsList.add(aShape);
//				externalToInternalShape.put(aShape, aShape);
//			}
//			i++;
		}
//		if (newSlotsList.size() > 0) {
//			curSize += newSlotsList.size();
//		  return super.addAll(newSlotsList);
//		} else
			return true;
	}
	
	public synchronized boolean optimizedAddAll(
			Collection<? extends BoundedShape> appendedCollection) {
		int numEmptySlots = internalSize() - size();
		if (numEmptySlots == 0) {
			curSize += appendedCollection.size();
			return super.addAll(appendedCollection);
		}
		int numNewSlots = appendedCollection.size() - numEmptySlots;
		List<BoundedShape> newSlotsList = new ArrayList();
		int i = 0;
		for (BoundedShape aShape: appendedCollection) {
			if  (i < numEmptySlots) {
				add(aShape);
			} else {
				newSlotsList.add(aShape);
				// looks like one has to do a bind to make this work
				// not worth figuring out why
				externalToInternalShape.put(aShape, aShape);
			}
			i++;
		}
		if (newSlotsList.size() > 0) {
			curSize += newSlotsList.size();
		  return super.addAll(newSlotsList);
		} else
			return true;
	}
	
	void nonStackClear() {
//		for (int i = 0; i < curSize; i++) {
//			displayElementAt(i).setBounds(new Rectangle (0, 0, 0, 0));
//		}
//			
//		curSize = 0;
		super.clear();
	}
	public synchronized void removeAllElements() {
		// unlike Vectors's version, this one seems to not all clear
       clear();
    }
	@Override
	public void clear() {
		if (!isStack) {
//			super.clear();
			nonStackClear();
			return;
		}
//		nonStackClear();
		for (int i = 0; i < curSize; i++) {
			BoundedShape internalElement = internalElementAt(i);
			hide(internalElement);
//			if (internalElement instanceof Disposable)
//				((Disposable) internalElement).setDisposed(true);
//			else
//				internalElement.setBounds(new Rectangle (0, 0, 0, 0));
//			internalElementAt(i).setBounds(new Rectangle (0, 0, 0, 0));
		}
			
		curSize = 0;

	}
	public synchronized void insertElementAt(BoundedShape element, int pos) {
		if (pos > size())
			throw new ArrayIndexOutOfBoundsException();
		if (pos == curSize && isStack) {
			add(element);
		} else {
			nonStackInsertElementAt(element, pos);
//		isStack = false;
//		super.insertElementAt(element, pos);
		}

		
	}
	
	public int size() {
		if (isStack)  {
			return curSize;
		} else
			return super.size();
			
	}
	@Override
	public int internalSize() {
		return super.size();
	}

	@Override
	public void update(Observable o, Object arg) {
		BoundedShape internalShape = externalToInternalShape.get((BoundedShape) o);
		if (internalShape != null && internalShape != o) {
			internalShape.copy((BoundedShape) o);
		}
	}

}
