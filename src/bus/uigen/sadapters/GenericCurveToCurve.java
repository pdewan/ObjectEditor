package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;
public class GenericCurveToCurve extends 
	//GenericXYShapeToShape 
	GenericBoundedShapeToBoundedShape
	implements ConcreteCurve {
	transient MethodProxy getControlXMethod = null;
	transient MethodProxy getControlYMethod = null;  transient MethodProxy setControlXMethod = null;
  transient MethodProxy setControlYMethod = null;
  transient MethodProxy getControlX2Method = null;
	transient MethodProxy getControlY2Method = null;
transient MethodProxy setControlX2Method = null;
transient MethodProxy setControlY2Method = null;	
	public GenericCurveToCurve (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericCurveToCurve () {
	}
	public void setMethods(ClassProxy c) {		super.setMethods(c);
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);		//setControlXMethods(c);
		setControlXMethods(cd);
		setControlYMethods(cd);
	}
  /*
  public void setControlXMethods (ClassProxy c) {	  	  getControlXMethod = uiBean.getGetIntMethod(c, "ControlX");
	  if (getControlXMethod == null)
		  getControlXMethod = uiBean.getGetIntMethod(c, "ControlX1");	  setControlXMethod = uiBean.getSetIntMethod(c, "ControlX");
	  if (setControlXMethod == null)
		  setControlXMethod = uiBean.getSetIntMethod(c, "ControlX1");
	  getControlX2Method = uiBean.getGetIntMethod(c, "ControlX2");
	  setControlX2Method = uiBean.getSetIntMethod(c, "ControlX2");
	  
  }
  */
  public void setControlXMethods (ClassDescriptorInterface c) {
	  
	  getControlXMethod = IntrospectUtility.getGetIntMethod(c, "ControlX");
	  if (getControlXMethod == null)
		  getControlXMethod = IntrospectUtility.getGetIntMethod(c, "ControlX1");
	  setControlXMethod = IntrospectUtility.getSetIntMethod(c, "ControlX");
	  if (setControlXMethod == null)
		  setControlXMethod = IntrospectUtility.getSetIntMethod(c, "ControlX1");
	  getControlX2Method = IntrospectUtility.getGetIntMethod(c, "ControlX2");
	  setControlX2Method = IntrospectUtility.getSetIntMethod(c, "ControlX2");
	  
  }
  /*
  public void setControlYMethods (ClassProxy c) {
	  
	  getControlYMethod = uiBean.getGetIntMethod(c, "ControlY");
	  if (getControlYMethod == null)
		  getControlYMethod = uiBean.getGetIntMethod(c, "ControlY1");	  
	  setControlYMethod = uiBean.getSetIntMethod(c, "ControlY");
	  if (setControlYMethod == null)
		  setControlYMethod = uiBean.getSetIntMethod(c, "ControlY1");
	  getControlY2Method = uiBean.getGetIntMethod(c, "ControlY2");
	  setControlY2Method = uiBean.getSetIntMethod(c, "ControlY2");
	  
  }
  */
  public void setControlYMethods (ClassDescriptorInterface c) {
	  
	  getControlYMethod = IntrospectUtility.getGetIntMethod(c, "ControlY");
	  if (getControlYMethod == null)
		  getControlYMethod = IntrospectUtility.getGetIntMethod(c, "ControlY1");	  
	  setControlYMethod = IntrospectUtility.getSetIntMethod(c, "ControlY");
	  if (setControlYMethod == null)
		  setControlYMethod = IntrospectUtility.getSetIntMethod(c, "ControlY1");
	  getControlY2Method = IntrospectUtility.getGetIntMethod(c, "ControlY2");
	  setControlY2Method = IntrospectUtility.getSetIntMethod(c, "ControlY2");
	  
  }
  Object[] emptyParams = {};
  public int getControlX() {
	  return  ((Integer) MethodInvocationManager.invokeMethod(getControlXMethod, targetObject, emptyParams));
  }
  public Integer getControlX2() {
	  if (getControlX2Method == null)
		  return null;
	  return  ((Integer) MethodInvocationManager.invokeMethod(getControlXMethod, targetObject, emptyParams));
  }
  public void setControlX (int newValue, CommandListener cl) {
	  Object[] params = {newValue};
	   MethodInvocationManager.invokeMethod(frame, targetObject, setControlXMethod, params, cl);
  }
  public void setControlX (int newValue) {
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(setControlXMethod, targetObject, params);
  }
  @Override
  public boolean isControlXReadOnly() {
	  return setControlXMethod == null;
  }
  
  public int getControlY() {
	  return  ((Integer) MethodInvocationManager.invokeMethod(getControlYMethod, targetObject, emptyParams));
  }
  public Integer getControlY2() {
	  if (getControlY2Method == null)
		  return null;
	  return  ((Integer) MethodInvocationManager.invokeMethod(getControlYMethod, targetObject, emptyParams));
  }
  public void setControlY (int newValue, CommandListener cl) {
	  Object[] params = {newValue};
	   MethodInvocationManager.invokeMethod(frame, targetObject, setControlYMethod, params, cl);
  }
  public void setControlY (int newValue) {
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(setControlYMethod, targetObject, params);
  }
  public void setControlY2 (Integer newValue, CommandListener cl) {
	  if (setControlY2Method == null)
		  return;
	  Object[] params = {newValue};
	   MethodInvocationManager.invokeMethod(frame, targetObject, setControlY2Method, params, cl);
  }
  public void setControlY2 (Integer newValue) {
	  if (setControlY2Method == null)
		  return;
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(setControlY2Method, targetObject, params);
  }
  public void setControlX2 (Integer newValue, CommandListener cl) {
	  if (setControlY2Method == null)
		  return;
	  Object[] params = {newValue};
	   MethodInvocationManager.invokeMethod(frame, targetObject, setControlX2Method, params, cl);
  }
  public void setControlX2 (Integer newValue) {
	  if (setControlY2Method == null)
		  return;
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(setControlX2Method, targetObject, params);
  }
  @Override
  public boolean isControlYReadOnly() {
	  return setControlYMethod == null;
  }

@Override
public void setControlXControlY(int newControlXValue, int newControlYValue) {
	// TODO Auto-generated method stub
	setControlX(newControlXValue);
	setControlY(newControlYValue);
	
}

@Override
public void setControlXControlY(int newControlXValue, int newControlYValue,
		CommandListener cl) {
	setControlX(newControlXValue, cl);
	setControlY(newControlYValue, cl);
	
}
  
  /*
  public static String TEXT = "Text";
  public String programmingPatternKeyword() {
		return  super.programmingPatternKeyword() + ObjectEditor.KEYWORD_SEPARATOR + TEXT;
	}
	public String typeKeyword() {
		return super.typeKeyword() + ObjectEditor.KEYWORD_SEPARATOR + TEXT ;
	}
	*/
}