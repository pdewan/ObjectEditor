package bus.uigen.oadapters;import java.rmi.RemoteException;import util.models.RemotePropertyChangeListener;public class OvalAdapter extends BoundedShapeAdapter implements RemotePropertyChangeListener  {
	public static OvalAdapter createOvalAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor) throws RemoteException {
      
      OvalAdapter ovalAdapter = new OvalAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return ovalAdapter;  }	public OvalAdapter () throws RemoteException  {			}  
}
