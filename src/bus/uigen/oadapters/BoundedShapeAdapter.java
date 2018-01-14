package bus.uigen.oadapters;
import java.rmi.RemoteException;
import java.util.*;import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.*;

import shapes.RemoteShape;
import shapes.ShapeModel;import util.trace.uigen.IllegalPropertyNotification;
import util.trace.uigen.ShapeLowerYLessThanZero;
import util.trace.uigen.ShapeRightXLessThanZero;
import bus.uigen.*;import bus.uigen.introspect.*;import bus.uigen.sadapters.ConcreteBoundedShape;
public class BoundedShapeAdapter extends ShapeObjectAdapter  {
	public BoundedShapeAdapter () throws RemoteException {
		
	}
	public ConcreteBoundedShape getConcreteBoundedShape() {		return (ConcreteBoundedShape) getConcreteObject();
	}
	/*  transient Method getWidthMethod = null;  transient Method setWidthMethod = null;       transient Method getHeightMethod = null;  transient Method setHeightMethod = null;   public Method getGetWidthMethod() {
    return getWidthMethod;
  }
  public void setGetWidthMethod(Method newVal) {
    setWidthMethod = newVal;
  }  public Method getSetWidthMethod() {
    return setWidthMethod;
  }
  public void setSetWidthMethod(Method newVal) {
    setWidthMethod = newVal;
  }
  public void setGetHeightMethod(Method newVal) {
    getHeightMethod = newVal;
  }  public Method getSetHeightMethod() {
    return setHeightMethod;
  }
  public void setSetHeightMethod(Method newVal) {
    setHeightMethod = newVal;
  }  public void setMethods() {
	  super.setMethods();
	  setBoundsMethods();  }
  public void setBoundsMethods() {
	  Class c = getRealObject().getClass();	  getWidthMethod = uiBean.getGetIntMethod(c, "Width");	  	  getHeightMethod = uiBean.getGetIntMethod(c, "Height");	  setWidthMethod = uiBean.getSetIntMethod(c, "Width");	  setHeightMethod = uiBean.getSetIntMethod(c, "Height");
  }
	*/  int oldWidth, oldHeight;  public void setViewObject(Object viewObject) {
	  if (viewObject == null)  {
		  return;
	  }
  	try {
	  super.setViewObject(viewObject);
	  if (textMode) return;	  Object obj = computeAndMaybeSetViewObject();
	  //ShapeModel shape = (ShapeModel) viewObject;
	  RemoteShape shape = (RemoteShape) viewObject;
	  oldWidth = shape.getWidth();
	  oldHeight = shape.getHeight();	  if (obj instanceof shapes.RemoteShape) {		  RemoteShape oldShape = (RemoteShape) obj;		  oldShape.setWidth(oldWidth);		  oldShape.setHeight(oldHeight);	  }
  	} catch (Exception e) {
  		e.printStackTrace();
  		//System.out.println(e);
  	}
	    }
  /*  public Object getViewObject(Object realObject) {
	  ShapeModel shape = (ShapeModel) getViewObject();
	  if (shape == null)		  return super.getViewObject(realObject);
	  return recalculateViewObject(shape, realObject);  }  */
  
  boolean respondToPropertyChange (PropertyChangeEvent event) {
		 try {
		  String propertyName = event.getPropertyName().toLowerCase();
		  if (!textMode) {
		  RemoteShape shape = (RemoteShape) computeAndMaybeSetViewObject();
		  if (shape == null) {
			  return true; // shape is not yet initialized, not sure why
		  }
		  if (propertyName.equals("width")) {
			  setWidth(shape, (int) event.getNewValue());
			  return true;
		  }
		  if (propertyName.equals("height")) {
			  setHeight(shape, (int) event.getNewValue());
			  return true;
		  }
		  }
		 
		  return super.respondToPropertyChange(event);
		 } catch (Exception e) {
			IllegalPropertyNotification.newCase(event, event.getSource(), this);

			 return false;
		 }
	  }
	 static String[] boundedShapePropertiesArray = {"width", "height"};
	 
	  
	  static void fillBoundedShapeProperties() {
		  for (String propertyName:boundedShapePropertiesArray) {
			  shapePropertiesSet.add(propertyName);
		  }
	  }
	  
	  static {
		  fillBoundedShapeProperties();
	  }
	  void setWidth(RemoteShape shape, int newVal) {
		  try {
			  if (newVal + shape.getX() < 0 && shape.getHeight() != 0 && shape.getHeight() == 0)  {
				  ShapeRightXLessThanZero.newCase(this, getRealObject(), newVal, this);
//				  Tracer.warning("X < 0 of " + getRealObject());
			  }
			  shape.setWidth(newVal);
			  oldWidth = newVal;
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	  }
	  void setHeight(RemoteShape shape, int newVal) {
		  try {
			
			  shape.setHeight(newVal);
			  oldHeight = newVal;
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	  }
  public RemoteShape recalculateViewBounds(RemoteShape shape) {
	
	  if (getTextMode()) return shape;
	 
	  
	  try {
		  /*
		    Object[] params = {};
			int width = ((Integer) getWidthMethod.invoke(object, params)).intValue();
			int height = ((Integer) getHeightMethod.invoke(object, params)).intValue();
		  */
			int width = getConcreteBoundedShape().getWidth();
			int height = getConcreteBoundedShape().getHeight();
			if (shape == null) {
				return null;
			}
			if (shape.getWidth() != width /*&& width != 0*/)
//				shape.setWidth(width);
				setWidth(shape, width);
			if (shape.getHeight() != height /*&& height != 0*/)
//				shape.setHeight(height);   
				setHeight(shape, height);
//			if (shape.getWidth() != width && width != 0)
//				shape.setWidth(width);
//			if (shape.getHeight() != height && height != 0)
//				shape.setHeight(height);   
//			oldWidth = width;
//			oldHeight = height;
		} catch (Exception e) {
		  System.out.println("E**: exception invoking bounds methods");
		  e.printStackTrace();
		}	
	  return shape;
   }
    public RemoteShape recalculateViewObject(RemoteShape shape, Object object) {	  //ShapeModel shape = (ShapeModel) getViewObject();	  //Object object = getRealObject();
	  super.recalculateViewObject(shape, object);
	  if (getTextMode()) return shape;
	 return  recalculateViewBounds(shape);	  
	    }
  void setViewObjectAttributes(RemoteShape shape) {
	  super.setViewObjectAttributes(shape);
	  recalculateViewBounds(shape);
	  
//	  recalculateViewObjectColor(shape);
//	  recalculateViewObjectFilled(shape);
//	  recalculateViewObjectZAxis(shape);
//	  recalculateViewObject3D(shape);
//	  recalculateViewObjectRounded(shape);
//	  recalculateViewObjectFont(shape);
//	  recalculateViewObjectFontSize(shape);
//	  recalculateViewObjectGradientPaint(shape);
//	  recalculateViewObjectBasicStroke(shape);
	  
  }
  public boolean recalculateRealObject() {	  if (textMode) return super.recalculateRealObject();
	  boolean retVal = super.recalculateRealObject();	  //ShapeModel shape = (ShapeModel) getViewObject();
	  RemoteShape shape = (RemoteShape) computeAndMaybeSetViewObject();	  //Object object = getRealObject();	  ConcreteBoundedShape concreteBoundedShape = getConcreteBoundedShape();
	  try {
		  int width = shape.getWidth();		  if (oldWidth != width) {			  /*
		    Object[] params = {new Integer(width)};			setWidthMethod.invoke(object, params);			  */			  concreteBoundedShape.setWidth(width);
			retVal = true;		  }
		  int height = shape.getHeight();		  if (oldHeight != height) {			  /*
			  Object[] params = {new Integer(height)};
			setHeightMethod.invoke(object, params);
			  */
			 concreteBoundedShape.setHeight(height); 			retVal = true;
		  }
		  oldWidth = width;
		  oldHeight = height;			 
		} catch (Exception e) {		  System.out.println("E**: exception invoking  bounds methods");		  e.printStackTrace();
		}	  return retVal;
  }  
}
