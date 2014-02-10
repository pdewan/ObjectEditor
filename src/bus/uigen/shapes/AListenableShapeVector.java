package bus.uigen.shapes;
import java.awt.Rectangle;

import bus.uigen.ObjectEditor;
import shapes.BoundedShape;
import shapes.BoundedTextShape;
import shapes.StringModel;
import shapes.TextShape;
import util.misc.Common;
import util.models.AListenableVector;
import util.models.ListenableVector;

@util.annotations.IsCompositeShape(true)
public class AListenableShapeVector extends 
      AListenableVector<BoundedShape> 
      implements ListenableShapeVector {
	Rectangle bounds = new Rectangle();
	Object userObjectShape = new ARectangleModel(0, 0, 0, 0);
	BoundedShape markerShape, marker2Shape;


	public synchronized boolean add(BoundedShape element) {
		return super.add(element);
//		super.addElement(element);
//		maybeSetParentLinkOfDescendent(element);
//		if (size() == 1)
//			initFirstRow();
//		vectorChangeSupport.elementAdded(element);
	}
	// send insert and delete events to ObjectEditor, which does not
	// follow move etc events
	@Override
	public void move(int fromIndex, int toIndex) {
		if (fromIndex == toIndex)
		return;
		BoundedShape temp = get(fromIndex);
//		ElementType temp = get(fromIndex);

		if (toIndex > fromIndex) {
			insertElementAt(temp, toIndex);
			remove(fromIndex);
//			insertElementAt(temp, toIndex);
//			remove(fromIndex);
		} else if (toIndex < fromIndex) {
			insertElementAt(temp, toIndex);
			remove(fromIndex + 1);
//			insertElementAt(temp, toIndex);
//			remove(fromIndex + 1);
		}
		vectorChangeSupport.elementMoved(fromIndex, toIndex);

	}
public static void main (String[] args) {
	ListenableVector listenableVector = new AListenableVector();
	ListenableShapeVector listenableShapeVector = new AListenableShapeVector();
	BoundedTextShape stringModel = new AStringModel ("Hello world");
	
	listenableShapeVector.add(stringModel);
	listenableVector.add(listenableShapeVector);
	ObjectEditor.edit(listenableVector);
}

//@Override
//public boolean removeElement(BoundedShape c) {
//	return super
//	// TODO Auto-generated method stub
//	return false;
//}

@Override
public void setWidth(int width) {
	 bounds.width = width;
}

@Override
public void setHeight(int height) {
	 bounds.height = height;	
}

@Override
public int getHeight() {
	return bounds.height;
}

@Override
public int getWidth() {
	return bounds.width;
}

@Override
public Rectangle getBounds() {
	return bounds;
}

@Override
public void setBounds(Rectangle newVal) {
	bounds = newVal;
	
}

@Override
public boolean copy(BoundedShape aReference) {
	ListenableShapeVector aReferenceShapes = (ListenableShapeVector) aReference;
//	int referenceSize = aReferenceShapes.size();
	int myOriginalSize = size();	
	int sizeDifference = size() - aReferenceShapes.size();
	
	if (sizeDifference > 0) {
		for (int i = 0; i < sizeDifference; i++) {
			remove(size() - 1);
		}
	} else {
		for (int i = size(); i < aReferenceShapes.size() ; i++) {
			add ((BoundedShape) Common.deepCopy(aReferenceShapes.get(i)));
		
		}
	}
	for (int i = 0; i < myOriginalSize; i++) {
		get(i).copy(aReferenceShapes.get(i));
		
	}
	return true;
	
}

@Override
public boolean copyable(BoundedShape aReference) {
	if (!(aReference instanceof ListenableShapeVector)) return false;
	ListenableShapeVector aReferenceShapes = (ListenableShapeVector) aReference;
	int numCommonElements = Math.min(size(), aReferenceShapes.size());
	boolean retVal = true;
	for (int i = 0; i < numCommonElements; i++) {
		retVal &= get(i).copyable(aReferenceShapes.get(i));		
	}
	return retVal;
}

@Override
public int getX() {
	return bounds.x;
}

@Override
public int getY() {
	// TODO Auto-generated method stub
	return bounds.y;
}

@Override
public void setX(int newVal) {
	bounds.x = newVal;
	
}

@Override
public void setY(int newVal) {
	bounds.y =  newVal;
	
}
public Object getTempShape() {
	return userObjectShape;
}

public void setTempShape(Object newVal) {
	userObjectShape = newVal;
}
@Override
public boolean removeElement(BoundedShape c) {
	// TODO Auto-generated method stub
	return false;
}
@Override
public BoundedShape getMarkerShape() {
	return markerShape;
}
@Override
public void setMarkerShape(BoundedShape newVal) {
	markerShape = newVal;
	
}

@Override
public BoundedShape getMarker2Shape() {
	return marker2Shape;
}
@Override
public void setMarker2Shape(BoundedShape newVal) {
	marker2Shape = newVal;
	
}


}