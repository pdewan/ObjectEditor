package bus.uigen.oadapters;
import java.rmi.RemoteException;
import java.util.*;import java.lang.reflect.*;

import shapes.CurveModel;
import shapes.LabelModel;
import shapes.RemoteCurve;
import shapes.RemoteShape;
import shapes.ShapeModel;import util.models.RemotePropertyChangeListener;
import bus.uigen.*;import bus.uigen.introspect.*;import bus.uigen.sadapters.ConcreteCurve;
import bus.uigen.sadapters.ConcreteTextShape;
public class CurveAdapter extends BoundedShapeAdapter implements RemotePropertyChangeListener  { 
	public CurveAdapter () throws RemoteException {
		
	}	public ConcreteCurve getConcreteCurve() {		return (ConcreteCurve) getConcreteObject();
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
  */ int oldControlX;
 int oldControlY;
 Integer oldControlX2;
 Integer oldControlY2;  public void setViewObject(Object viewObject) {
	  try {
	  super.setViewObject(viewObject);
	  if (getTextMode()) return;	  Object obj = computeAndMaybeSetViewObject();
	  //TextModel shape = (TextModel) viewObject;
	  shapes.RemoteCurve shape = (shapes.RemoteCurve) viewObject;
	  oldControlX = shape.getControlX();
	  oldControlY = shape.getControlY();
	  oldControlX2 = shape.getControlX2();
	  oldControlY2 = shape.getControlY2();	  if (obj instanceof shapes.CurveModel) {		  CurveModel oldShape = (CurveModel) obj;		  oldShape.setControlX(oldControlX);
		  oldShape.setControlY(oldControlY);
		  oldShape.setControlX2(oldControlX2);
		  oldShape.setControlY2(oldControlY2);	  }
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	    }
  /*  public Object getViewObject(Object realObject) {
	  ShapeModel shape = (ShapeModel) getViewObject();
	  if (shape == null)		  return super.getViewObject(realObject);
	  return recalculateViewObject(shape, realObject);  }  */
    public RemoteShape recalculateViewObject(RemoteShape shape, Object object) {	  //ShapeModel shape = (ShapeModel) getViewObject();	  //Object object = getRealObject();	  RemoteCurve curveShape = (RemoteCurve) shape;
	  super.recalculateViewObject(shape, object);	  
	  try {		    /*
		    Object[] params = {};			String text = (String) getTextMethod.invoke(object, params);		  */		  int controlX = getConcreteCurve().getControlX();
		  int controlY = getConcreteCurve().getControlY();
			if (curveShape.getControlX() != controlX)				curveShape.setControlX(controlX); 
			if (curveShape.getControlY() != controlY)
				curveShape.setControlY(controlY);			oldControlX = controlX;
			oldControlY = controlY;
			
			Integer controlX2 = getConcreteCurve().getControlX2();
			  Integer controlY2 = getConcreteCurve().getControlY2();
				if (curveShape.getControlX2() != controlX2)
					curveShape.setControlX2(controlX2); 
				if (curveShape.getControlY2() != controlY2)
					curveShape.setControlY2(controlY2);
				oldControlX2 = controlX2;
				oldControlY2 = controlY2;
		} catch (Exception e) {		  System.out.println("E**: exception invoking control methods");		  e.printStackTrace();
		}	
	  return shape;   }
  public boolean recalculateRealObject() {
	  boolean retVal = super.recalculateRealObject();

	  if (textMode) return false;
		  RemoteCurve shape = (RemoteCurve) computeAndMaybeSetViewObject();	  Object object = getRealObject();
	  try {
		  int controlX = shape.getControlX();
		  int controlY = shape.getControlY();		  if (oldControlX != controlX) {			  /*
		    Object[] params = {text};			setTextMethod.invoke(object, params);			  */			  getConcreteCurve().setControlX(controlX);
			  
			retVal = true;		  }
		  oldControlX = controlX;
		  if (oldControlY != controlY) {
			  /*
		    Object[] params = {text};
			setTextMethod.invoke(object, params);
			  */
			  getConcreteCurve().setControlY(controlY);
			  
			retVal = true;
		  }
		  oldControlY = controlY;	
		} catch (Exception e) {		  System.out.println("E**: exception invoking set angle  methods");		  e.printStackTrace();
		}	  return retVal;
  }
  public static CurveAdapter createCurveAdapter (Object obj,											  Object obj1, 											  Object parentObject, 											  String name, 											  Class inputClass, 											  boolean propertyFlag, 											  ObjectAdapter adaptor)throws RemoteException  {
      
      CurveAdapter CurveAdapter = new CurveAdapter();
	  //ShapeModel shape = uiBean.toPointModel(obj1);	  //pointAdapter.setViewObject(shape);
      return CurveAdapter;  }  
}
