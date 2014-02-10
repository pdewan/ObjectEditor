package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.trace.NullLocationException;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;
import java.awt.Color;

import util.misc.Common;
import util.trace.Tracer;
public class GenericPointBasedShapeToShape extends GenericShapeToShape implements ConcreteLocatableShape {
  transient MethodProxy getLocationMethod = null;  transient MethodProxy setLocationMethod = null;
  ConcreteLocatableShape location;
  Object locationObject;  //transient VirtualMethod getYMethod = null;  //transient VirtualMethod setYMethod = null; 
  // VirtualMethod getColorMethod = null;  //transient VirtualMethod setColorMethod = null;
  
  	public Object clone() {
  		GenericPointBasedShapeToShape retVal = (GenericPointBasedShapeToShape) super.clone();
  		retVal.location = null;
  		retVal.locationObject = null;
  		return retVal;
  	}	
	public GenericPointBasedShapeToShape (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericPointBasedShapeToShape () {
	}
	public void init (Object theTargetObject, uiFrame theFrame) {
		super.init(theTargetObject, theFrame);
		//location = (new GenericPointToPointFactory()).toConcreteXYPoint(theTargetObject.getClass(), theTargetObject, theFrame);
	}
	
	void updateLocation () {
		try {
			//=Object locationObject = uiMethodInvocationManager.invokeMethod(getLocationMethod, targetObject, emptyParams);
			//updateLocationObject();
			locationObject = getLocationObject();
//			if (locationObject == null) {
//				Tracer.error("Null location for object: " + targetObject);
//			}
			if (location != null) {
				location.setTarget(locationObject);
			} else
			location = (new GenericPointToPoint(locationObject, frame));
				
		} catch (NullLocationException ne) {
			throw ne;
		}
		catch  (Exception e) {
			e.printStackTrace();
		}
	}
	public Object getLocationObject() {
		if (getLocationMethod == null) {
			return null;
		} try {
			return MethodInvocationManager.invokeMethod(getLocationMethod, targetObject, emptyParams);
			
				
		} catch  (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setLocationObject(Object locationObject) {
		if (setLocationMethod == null) return;
		Object[] params = {locationObject};
		
		try {
			 MethodInvocationManager.invokeMethod(setLocationMethod, targetObject, params);
			
				
		} catch  (Exception e) {
			 ;
		}
	}
	public void updateLocationObject() {
		locationObject = getLocationObject();
		if (location != null)
			location.setTarget(locationObject);
		else {
			location = (new GenericPointToPoint(locationObject, frame));
		}
	}
	
	public void setMethods(ClassProxy c) {	super.setMethods(c);
	  setLocationMethods(c);
	  //setAttributeMethods(c);  }
  /*  public void setAttributeMethods(Class c) {	  getColorMethod = uiBean.getGetMethod(c, "Color", Color.class);
	  setColorMethod = uiBean.getSetMethod(c, "Color", Color.class);  }
  */
  public void setLocationMethods(ClassProxy c) {	  getLocationMethod = IntrospectUtility.getGetPointMethod(c, "Location");	  	  	  setLocationMethod = IntrospectUtility.getSetPointMethod(c, "Location");	  //setYMethod = uiBean.getSetIntMethod(c, "Y");
	  
  }
  Object[] emptyParams = {};
  public int getX() {
	  try {
		  if (targetObject == null) {
//			  Message.error("Null target object");
			  return 0;
		  }
	  updateLocation();
	  return  location.getX();
	  } catch (NullLocationException e) {
//		  Tracer.error("GenericPointBasedShapeToShape: null location of object: " + targetObject);
		  return 0;
	  }
  }
  public void setXY (int newXValue, int newYValue, CommandListener cl) {
	  setXY(newXValue, newYValue);
  }
  public void setXY (int newXValue, int newYValue) {
	  updateLocation();
	  boolean isEditableLocation = location.isEditable();
	  if (isEditableLocation) {
		  location.setX(newXValue);
		  location.setY(newYValue);
		  //setLocationObject(locationObject);
	  } else {
		  setNewLocation(newXValue, newYValue);
		  
	  }
	  setLocationObject(locationObject);
	  //resetLocation()
  }
  
  void setNewLocation (int newXValue, int newYValue) {
	  Class locationClass = locationObject.getClass();
	  Object[] params = {newXValue, newYValue};
	  Object o = Common.newInstanceWithParameters(locationClass, params);
	  if (o == null)
		  return ;
	  locationObject = o;
	  location = (new GenericPointToPoint(locationObject, frame));
	  location.setX(newXValue);
	  location.setY(newYValue);
	  ;
	  return ;
	  
  }
  public void setX (int newValue, CommandListener cl) {
	  if (!location.isEditable()) {
		  setXY(newValue, location.getY(), cl);
		  return;
	  }
	  updateLocation();
	  location.setX(newValue, cl);
  }
  public void setX (int newValue) {
	  if (!location.isEditable()) {
		  setXY(newValue, location.getY());
		  return;
	  }
	  updateLocation();
	  location.setX(newValue);
  }
  public int getY() {
	  try {
	  updateLocation();
	  return location.getY();
  } catch (NullLocationException e) {
//	  Tracer.error("GenericPointBasedShapeToShape: null location of object: " + targetObject);
	  return 0;
  }
  }
  public void setY (int newValue){
	  if (!location.isEditable()) {
		  setXY(location.getX(), location.getY());
		  return;
	  }
	  updateLocation();
	  location.setY(newValue);
  }
  public void setY (int newValue, CommandListener cl) {
	  if (!location.isEditable()) {
		  setXY(location.getX(), location.getY(), cl);
		  return;
	  }
	  updateLocation();
	  location.setY(newValue, cl);
  }
  
  /*
  public void setColor (Color newValue) {
	  Object[] params = {newValue};
	  uiMethodInvocationManager.invokeMethod(setColorMethod, targetObject, params);
  }
  public void setColor (int newValue, CommandListener cl) {
	  Object[] params = {new Integer(newValue)};
	  uiMethodInvocationManager.invokeMethod(frame, targetObject, setColorMethod, params, cl);
  }
  */
  public boolean isXReadOnly() {
	  try {
	  updateLocation();
	  return location.isXReadOnly();
	  } catch (NullLocationException e) {
		  return true;
	  }
  }
  public boolean isYReadOnly() {
	  try {
	  updateLocation();
	  return location.isYReadOnly();
	  } catch (NullLocationException e) {
		  return true;
	  }
  }
  /*
  public boolean hasColor()   {
	  return getColorMethod != null;
  }
  public boolean isColorReadOnly()   {
	  return setColorMethod == null;
  }
  */
	
	
}