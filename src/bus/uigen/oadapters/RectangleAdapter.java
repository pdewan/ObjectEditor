package bus.uigen.oadapters;import java.rmi.RemoteException;import util.models.RemotePropertyChangeListener;public class RectangleAdapter extends BoundedShapeAdapter implements RemotePropertyChangeListener {
	public static RectangleAdapter createRectangleAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor) throws RemoteException {
      
      RectangleAdapter rectangleAdapter = new RectangleAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return rectangleAdapter;  }	public RectangleAdapter () throws RemoteException {			}  
}
