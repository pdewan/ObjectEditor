package bus.uigen.oadapters;
import java.rmi.RemoteException;
import java.util.*;import java.lang.reflect.*;

import shapes.ShapeModel;
import util.models.RemotePropertyChangeListener;
public class PointAdapter extends ShapeObjectAdapter implements RemotePropertyChangeListener {
	public static PointAdapter createPointAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor) throws RemoteException {
      
      PointAdapter pointAdapter = new PointAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return pointAdapter;  }
	public PointAdapter() throws RemoteException {
		
	}  
}
