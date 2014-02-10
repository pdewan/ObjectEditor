package bus.uigen.shapes;

import shapes.BoundedShape;
import util.models.ListenableVector;

public interface ListenableShapeVector extends ListenableVector<BoundedShape>, BoundedShape {
	public Object getTempShape() ;

	public void setTempShape(Object newVal) ;
	
	public BoundedShape getMarkerShape();
	public void setMarkerShape(BoundedShape newVal);
	
	public BoundedShape getMarker2Shape();
	public void setMarker2Shape(BoundedShape newVal);
}
