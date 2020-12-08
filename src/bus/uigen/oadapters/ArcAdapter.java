package bus.uigen.oadapters;
import java.rmi.RemoteException;
import java.util.*;import java.lang.reflect.*;

import shapes.ArcModel;
import shapes.LabelModel;
import shapes.RemoteArc;
import shapes.RemoteShape;
import shapes.ShapeModel;import util.models.RemotePropertyChangeListener;
import bus.uigen.*;import bus.uigen.introspect.*;import bus.uigen.sadapters.ConcreteArc;
import bus.uigen.sadapters.ConcreteTextShape;
public class ArcAdapter extends BoundedShapeAdapter implements RemotePropertyChangeListener  { 
	public ArcAdapter () throws RemoteException {
		
	}	public ConcreteArc getConcreteArc() {		return (ConcreteArc) getConcreteObject();
	}
	/*  transient Method getTextMethod = null;  transient Method setTextMethod = null;    public Method getGetTextMethod() {
    return getTextMethod;
  }
  public void setGetTextMethod(Method newVal) {
    setTextMethod = newVal;
  }  public Method getSetTextMethod() {
    return setTextMethod;
  }
  public void setSetTextMethod(Method newVal) {
    setTextMethod = newVal;
  }
	
    public void setMethods() {
	  super.setMethods();
	  setTextMethods();  }
  public void setTextMethods() {
	  Class c = getRealObject().getClass();	  getTextMethod = uiBean.getGetStringMethod(c, "Text");	  setTextMethod = uiBean.getSetStringMethod(c, "Text");
  }
  */ int oldStartAngle;
 int oldEndAngle;  public void setViewObject(Object viewObject) {
	  try {
	  super.setViewObject(viewObject);
	  if (getTextMode()) return;	  Object obj = computeAndMaybeSetViewObject();
	  //TextModel shape = (TextModel) viewObject;
	  shapes.RemoteArc shape = (shapes.RemoteArc) viewObject;
	  oldStartAngle = shape.getStartAngle();
	  oldEndAngle = shape.getEndAngle();	  if (obj instanceof shapes.ArcModel) {		  ArcModel oldShape = (ArcModel) obj;		  oldShape.setStartAngle(oldStartAngle);
		  oldShape.setEndAngle(oldEndAngle);	  }
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	    }
  /*  public Object getViewObject(Object realObject) {
	  ShapeModel shape = (ShapeModel) getViewObject();
	  if (shape == null)		  return super.getViewObject(realObject);
	  return recalculateViewObject(shape, realObject);  }  */
    public RemoteShape recalculateViewObject(RemoteShape shape, Object object) {	  //ShapeModel shape = (ShapeModel) getViewObject();	  //Object object = getRealObject();	  RemoteArc arcShape = (RemoteArc) shape;
	  super.recalculateViewObject(shape, object);	  
	  try {		    /*
		    Object[] params = {};			String text = (String) getTextMethod.invoke(object, params);		  */		  int startAngle = getConcreteArc().getStartAngle();
		  int endAngle = getConcreteArc().getEndAngle();
			if (arcShape.getStartAngle() != startAngle)				arcShape.setStartAngle(startAngle); 
			if (arcShape.getEndAngle() != endAngle)
				arcShape.setEndAngle(endAngle);			oldStartAngle = startAngle;
			oldEndAngle = endAngle;
		} catch (Exception e) {		  System.err.println("E**: exception invoking angle methods");		  e.printStackTrace();
		}	
	  return shape;   }
  public boolean recalculateRealObject() {
	  boolean retVal = super.recalculateRealObject();

	  if (textMode) return false;
		  RemoteArc shape = (RemoteArc) computeAndMaybeSetViewObject();	  Object object = getRealObject();
	  try {
		  int startAngle = shape.getStartAngle();
		  int endAngle = shape.getEndAngle();		  if (oldStartAngle != startAngle) {			  /*
		    Object[] params = {text};			setTextMethod.invoke(object, params);			  */			  getConcreteArc().setStartAngle(startAngle);
			  
			retVal = true;		  }
		  oldStartAngle = startAngle;
		  if (oldEndAngle != endAngle) {
			  /*
		    Object[] params = {text};
			setTextMethod.invoke(object, params);
			  */
			  getConcreteArc().setEndAngle(endAngle);
			  
			retVal = true;
		  }
		  oldEndAngle = endAngle;	
		} catch (Exception e) {		  System.err.println("E**: exception invoking set angle  methods");		  e.printStackTrace();
		}	  return retVal;
  }
  public static ArcAdapter createArcAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor)throws RemoteException  {
      
      ArcAdapter arcAdapter = new ArcAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return arcAdapter;  }  
}
