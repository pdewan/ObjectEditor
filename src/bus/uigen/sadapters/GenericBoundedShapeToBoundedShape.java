package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;

import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.RemoteSelector;
import bus.uigen.reflect.MethodProxy;
import shapes.BoundedShape;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;

import java.awt.Color;

import shapes.ShapeModel;
import util.trace.Tracer;
import util.trace.uigen.NullLocationException;
public class GenericBoundedShapeToBoundedShape extends GenericShapeToShape implements ConcreteBoundedShape {
	
	public static final String WIDTH_PROPERTY_NAME = "Color";
	public static final String HEIGHT_PROPERTY_NAME = "Color";
	ConcreteLocatableShape locationShape;
	transient MethodProxy getWidthMethod = null;  transient MethodProxy setWidthMethod = null;       transient MethodProxy getHeightMethod = null;  transient MethodProxy setHeightMethod = null; 
  boolean xyLocation;
  //transient VirtualMethod getColorMethod = null;  //transient VirtualMethod setColorMethod = null;	
	public GenericBoundedShapeToBoundedShape (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
		
	}
	public Object clone() {
		GenericBoundedShapeToBoundedShape retVal = (GenericBoundedShapeToBoundedShape) super.clone();
  		retVal.locationShape = null;
  		return retVal;
  	}
	
	public void init (Object theTargetObject, uiFrame theFrame) {
		super.init(theTargetObject, theFrame);
		//boolean xyLocation = uiBean.hasXYLocation(theTargetObject.getClass());
		//xyLocation = uiBean.hasXYLocation(RemoteSelector.getClass(theTargetObject));
		xyLocation = IntrospectUtility.hasXYLocation(targetClass);
		//xyLocation = uiBean.hasXYLocation(ClassDescriptor.getTargetClass(theTargetObject));
		setLocationShape(theTargetObject, theFrame);
//		if (xyLocation)
//			locationShape =  (new GenericPointToPoint(theTargetObject, theFrame));
//			//(new GenericPointToPointFactory()).toConcreteXYPoint(theTargetObject.getClass(), theTargetObject, theFrame);
//		else
//			locationShape = (new GenericPointBasedShapeToShape(theTargetObject, theFrame));
	}
	
	void setLocationShape(Object theTargetObject, uiFrame theFrame) {
		if (xyLocation)
			locationShape =  (new GenericPointToPoint(theTargetObject, theFrame));
			//(new GenericPointToPointFactory()).toConcreteXYPoint(theTargetObject.getClass(), theTargetObject, theFrame);
		else
			locationShape = (new GenericPointBasedShapeToShape(theTargetObject, theFrame));
		
	}
	
	void refreshLocation(Object theTargetObject) {
		if (getTargetObject() == theTargetObject) {
			if (xyLocation)
				return;
			else {
				try {
				((GenericPointBasedShapeToShape) locationShape).updateLocationObject();
				} catch (NullLocationException e) {
					Tracer.error("Null value stored as location of:" + targetObject + ". " +  e.getMessage());
				}
			}
		} else if (getTargetObject() != null){
			if (locationShape != null) {
				locationShape.setTarget(theTargetObject);
			} else {
				
			
		 xyLocation = IntrospectUtility.hasXYLocation(RemoteSelector.getClass(theTargetObject));
		 setLocationShape(theTargetObject, getFrame());
//		 if (xyLocation) {
//			locationShape =  (new GenericPointToPoint(theTargetObject, getFrame()));
//			//locationShape.setTarget(theTargetObject);
//			//(new GenericPointToPointFactory()).toConcreteXYPoint(theTargetObject.getClass(), theTargetObject, theFrame);
//		} else {
//			locationShape = (new GenericPointBasedShapeToShape(theTargetObject, frame));
//			locationShape.setTarget(theTargetObject);
//		}
		}}
		
	}
	public void setTarget(Object theTargetObject) {
		if ( theTargetObject instanceof ShapeModel) {
			Tracer.error("Shape Model Target Object");
		}
		if (locationShape == null)
			setLocationShape(theTargetObject, frame);
//		if (locationShape == null && !xyLocation)
//			locationShape = (new GenericPointBasedShapeToShape(theTargetObject, getFrame()));
		refreshLocation(theTargetObject);
		super.setTarget(theTargetObject);
	}
		public GenericBoundedShapeToBoundedShape () {
	}
	public void setMethods(ClassProxy c) {		super.setMethods(c);		setBoundMethodsFromProperties(c);
	}
  
  public void setBoundMethods(ClassProxy c) {	  getWidthMethod = IntrospectUtility.getGetIntMethod(c, "Width");	  	  getHeightMethod = IntrospectUtility.getGetIntMethod(c, "Height");	  setWidthMethod = IntrospectUtility.getSetIntMethod(c, "Width");	  setHeightMethod = IntrospectUtility.getSetIntMethod(c, "Height");
	  
//	  PropertyDescriptorProxy widthProperty = propertyTable.get("width");
//	  getWidthMethod = widthProperty.getReadMethod();
	  
  }
  public void setBoundMethodsFromProperties(ClassProxy c) {
	  getWidthMethod = getReadMethod("Width", c.integerType());	  
	  setWidthMethod = getWriteMethod("Width", c.integerType());
	  getHeightMethod = getReadMethod("Height", c.integerType());
	  setHeightMethod = getWriteMethod("Height", c.integerType());
	  
//	  PropertyDescriptorProxy widthProperty = propertyTable.get("width");
//	  getWidthMethod = widthProperty.getReadMethod();
	  
  }
  Object[] emptyParams = {};
  public int getWidth() {
	  if (targetObject instanceof BoundedShape) {
		  return  ((BoundedShape) targetObject).getWidth();
	  }
	  if (getWidthMethod == null) return -1;
	  return  ((Integer) MethodInvocationManager.invokeMethod(getWidthMethod, targetObject, emptyParams)).intValue();
  }
  public void setWidth (int newValue, CommandListener cl) {
	
	  if (setWidthMethod == null) return;
	  Object[] params = {new Integer(newValue)};
	   MethodInvocationManager.invokeMethod(frame, targetObject, setWidthMethod, params, cl);
  }
  public void setWidth (int newValue) {
	  if (targetObject instanceof BoundedShape) {
		    ((BoundedShape) targetObject).setWidth(newValue);
		    return;
	  }
	  if (setWidthMethod == null) return;
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(setWidthMethod, targetObject, params);
  }
  public int getHeight() {
	  if (targetObject instanceof BoundedShape) {
		  return  ((BoundedShape) targetObject).getHeight();
	  }
	  if (getHeightMethod == null) return -1;
	  return ((Integer) MethodInvocationManager.invokeMethod(getHeightMethod, targetObject, emptyParams)).intValue();
  }
  public void setHeight (int newValue){
	  if (targetObject instanceof BoundedShape) {
		    ((BoundedShape) targetObject).setWidth(newValue);
		    return;
	  }
	  if (setHeightMethod == null) return;
	  Object[] params = {new Integer(newValue)};
	   MethodInvocationManager.invokeMethod(setHeightMethod, targetObject, params);
  }
  public void setHeight (int newValue, CommandListener cl) {
	  if (setHeightMethod == null) return;

	   Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(frame, targetObject, setHeightMethod, params, cl);
  }
  
  public boolean isWidthReadOnly() {
	  return setWidthMethod == null;
  }
  public boolean isHeightReadOnly() {
	  return setHeightMethod == null;
  }
  public int getX() {
	  return locationShape.getX();
  }
  public void setXY (int newXValue, int newYValue, CommandListener cl) {
	  setX(newXValue, cl);
	  setY(newYValue, cl);
  }
  public void setXY (int newXValue, int newYValue) {
	  /*
	  if (locationShape == null) {
	  setX(newXValue);
	  setY(newYValue);
	  } else {
	  */
		  locationShape.setXY(newXValue, newYValue);
	  
  }
  
	public void setX (int newValue, CommandListener cl) {
		locationShape.setX(newValue, cl);
	}
	public void setX (int newValue) {
		locationShape.setX(newValue);
	}
	public int getY() {
		return locationShape.getY();
	}
	public void setY (int newValue) {
		locationShape.setY(newValue);
	}
	public void setY (int newValue, CommandListener cl) {
		locationShape.setY(newValue, cl);
	}
	
	public boolean isXReadOnly() {
		return locationShape.isXReadOnly();
	}
	public boolean isYReadOnly() {
		return locationShape.isYReadOnly();
	}
	
	
}