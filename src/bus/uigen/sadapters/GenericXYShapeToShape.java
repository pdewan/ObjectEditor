package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import shapes.BoundedShape;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;
import java.awt.Color;

import util.trace.Tracer;
public class GenericXYShapeToShape extends GenericShapeToShape implements ConcreteLocatableShape {
  transient MethodProxy getXMethod = null;  transient MethodProxy setXMethod = null;       transient MethodProxy getYMethod = null;  transient MethodProxy setYMethod = null; 
  // VirtualMethod getColorMethod = null;  //transient VirtualMethod setColorMethod = null;	
	public GenericXYShapeToShape (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericXYShapeToShape () {
	}
	public void setMethods(ClassProxy c) {	super.setMethods(c);
	  setLocationMethods(c);
	  //setAttributeMethods(c);  }
  /*  public void setAttributeMethods(Class c) {	  getColorMethod = uiBean.getGetMethod(c, "Color", Color.class);
	  setColorMethod = uiBean.getSetMethod(c, "Color", Color.class);  }
  */
  public void setLocationMethods(ClassProxy c) {	  getXMethod = IntrospectUtility.getGetIntMethod(c, "X");	  	  getYMethod = IntrospectUtility.getGetIntMethod(c, "Y");	  setXMethod = IntrospectUtility.getSetIntMethod(c, "X");	  setYMethod = IntrospectUtility.getSetIntMethod(c, "Y");
	  
  }
  Object[] emptyParams = {};
  public int getX() {
	  if (targetObject == null) {
		  
		  Tracer.error("null target object");
		  return 0;
	  }
	  if (targetObject instanceof BoundedShape) {
		  return ((BoundedShape) targetObject).getX();
	  }
	  //try {
	  return  ((Integer) MethodInvocationManager.invokeMethod(getXMethod, targetObject, emptyParams)).intValue();
	  //} catch (Exception e) {
		 // Message.error("Returning 0 for X coordinate as location object not specified.)
		  //return 0;
	  //}
  }
  public void setXY (int newXValue, int newYValue, CommandListener cl) {
	  setX(newXValue, cl);
	  setY(newYValue, cl);
  }
  public void setXY (int newXValue, int newYValue) {
	  setX(newXValue);
	  setY(newYValue);
  }
  public void setX (int newValue, CommandListener cl) {
//	  if (targetObject instanceof BoundedShape) {
//		   ((BoundedShape) targetObject).setX(newValue);
//	  }
	  if (setXMethod == null)
		  return;
	  Object[] params = {new Integer(newValue)};
	   MethodInvocationManager.invokeMethod(frame, targetObject, setXMethod, params, cl);
  }
  public void setX (int newValue) {
	  if (targetObject instanceof BoundedShape) {
		   ((BoundedShape) targetObject).setX(newValue);
		   return;
	  }
	  if (setXMethod == null)
		  return;
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(setXMethod, targetObject, params);
  }
  public int getY() {
     if (targetObject == null) {
		  
		  Tracer.error("null target object");
		  return 0;
	  }
     if (targetObject instanceof BoundedShape) {
		  return ((BoundedShape) targetObject).getY();
	  }
	  
	  //try {
	  return ((Integer) MethodInvocationManager.invokeMethod(getYMethod, targetObject, emptyParams)).intValue();
	  //} catch (Exception e) {
		  //return 0;
	  //}
  }
  public void setY (int newValue){
	  if (targetObject instanceof BoundedShape) {
		   ((BoundedShape) targetObject).setY(newValue);
		   return;
	  }
	  if (setYMethod == null)
		  return;
	  Object[] params = {new Integer(newValue)};
	   MethodInvocationManager.invokeMethod(setYMethod, targetObject, params);
  }
  public void setY (int newValue, CommandListener cl) {
	  if (setYMethod == null)
		  return;
	   Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(frame, targetObject, setYMethod, params, cl);
  }
  /*
  public Color getColor() {
	  return (Color) uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
  }
  
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
	  return setXMethod == null;
  }
  public boolean isYReadOnly() {
	  return setYMethod == null;
  }
  public boolean isEditable() {
	  if (isXReadOnly() && isYReadOnly())
		  return false;
	  return super.isEditable();
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