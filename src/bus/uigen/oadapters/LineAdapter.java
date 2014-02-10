package bus.uigen.oadapters;import java.rmi.RemoteException;import util.models.RemotePropertyChangeListener;public class LineAdapter extends BoundedShapeAdapter implements RemotePropertyChangeListener  {
	public static LineAdapter createLineAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor) throws RemoteException  {
      
      LineAdapter lineAdapter = new LineAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return lineAdapter;  }	public LineAdapter () throws RemoteException  {			}  
}
