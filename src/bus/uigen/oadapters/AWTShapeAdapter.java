package bus.uigen.oadapters;
import java.rmi.RemoteException;
import java.util.*;import java.awt.Shape;
import java.lang.reflect.*;

import bus.uigen.sadapters.ConcreteAWTShape;
import bus.uigen.sadapters.ConcreteArc;

import shapes.AWTShapeModel;
import shapes.ArcModel;
import shapes.RemoteAWTShape;
import shapes.RemoteArc;
import shapes.RemoteShape;
import shapes.ShapeModel;
import util.models.RemotePropertyChangeListener;
public class AWTShapeAdapter extends ShapeObjectAdapter implements RemotePropertyChangeListener {
	public static AWTShapeAdapter createAWTShapeAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor) throws RemoteException {
      
      AWTShapeAdapter retVal = new AWTShapeAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return retVal;  }
	public AWTShapeAdapter() throws RemoteException {
		
	}
	public ConcreteAWTShape getConcreteAWTShape() {
		return (ConcreteAWTShape) getConcreteObject();
	}
	 Shape oldAWTShape;
	 
	  public void setViewObject(Object viewObject) {
		  try {
		  super.setViewObject(viewObject);
		  if (getTextMode()) return;
		  Object obj = computeAndMaybeSetViewObject();
		  //TextModel shape = (TextModel) viewObject;
		  shapes.RemoteAWTShape shape = (shapes.RemoteAWTShape) viewObject;
		  oldAWTShape = shape.getShape();
		  if (obj instanceof shapes.AWTShapeModel) {
			  AWTShapeModel oldShape = (AWTShapeModel) obj;
			  oldShape.setShape(oldAWTShape);
		  }
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  
	  }
	  /*
	  public Object getViewObject(Object realObject) {
		  ShapeModel shape = (ShapeModel) getViewObject();
		  if (shape == null)
			  return super.getViewObject(realObject);
		  return recalculateViewObject(shape, realObject);
	  }
	  */
	  
	  
	  
	  public RemoteShape recalculateViewObject(RemoteShape shape, Object object) {
		  //ShapeModel shape = (ShapeModel) getViewObject();
		  //Object object = getRealObject();
		  RemoteAWTShape awtShape = (RemoteAWTShape) shape;
		  super.recalculateViewObject(shape, object);
		  
		  try {
			    /*
			    Object[] params = {};
				String text = (String) getTextMethod.invoke(object, params);
			  */
			  Shape newAWTShape = getConcreteAWTShape().getAWTShape();
			  // send notification because the shape object may be same but its components may have changed
//				if (newAWTShape != awtShape.getShape())
					awtShape.setShape(newAWTShape); 
				oldAWTShape = newAWTShape;
				
			} catch (Exception e) {
			  System.out.println("E**: exception invoking shape methods");
			  e.printStackTrace();
			}	
		  return shape;
	   }
	  
	  // this should never be called really
	  public boolean recalculateRealObject() {
		  boolean retVal = super.recalculateRealObject();

		  if (textMode) return false;
		
		  RemoteAWTShape shape = (RemoteAWTShape) computeAndMaybeSetViewObject();
		  Object object = getRealObject();
		  try {
			  Shape awtShape = shape.getShape();
			  if (oldAWTShape != awtShape) {
				  /*
			    Object[] params = {text};
				setTextMethod.invoke(object, params);
				  */
				  getConcreteAWTShape().setAWTShape(awtShape);
				  
				retVal = true;
			  }
			  
			} catch (Exception e) {
			  System.out.println("E**: exception invoking set shape  methods");
			  e.printStackTrace();
			}
		  return retVal;
	  }  
}
