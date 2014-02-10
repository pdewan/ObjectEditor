package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;
import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.controller.MethodInvocationManager;
public class GenericArcToArc extends 
	//GenericXYShapeToShape 
	GenericBoundedShapeToBoundedShape
	implements ConcreteArc {
	transient MethodProxy getStartAngleMethod = null;
	transient MethodProxy getEndAngleMethod = null;  transient MethodProxy setStartAngleMethod = null;
  transient MethodProxy setEndAngleMethod = null;
  public static String START_ANGLE = "StartAngle";	
	public GenericArcToArc (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericArcToArc () {
	}
	public void setMethods(ClassProxy c) {		super.setMethods(c);
		ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);		setStartAngleMethods(cd);
		setEndAngleMethods(cd);
	}
  
  public void setStartAngleMethods (ClassDescriptorInterface c) {	  	  getStartAngleMethod = IntrospectUtility.getGetIntMethod(c, "StartAngle");	  setStartAngleMethod = IntrospectUtility.getSetIntMethod(c, "StartAngle");
	  
  }
  public void setEndAngleMethods (ClassDescriptorInterface c) {
	  
	  getEndAngleMethod = IntrospectUtility.getGetIntMethod(c, "EndAngle");
	  setEndAngleMethod = IntrospectUtility.getSetIntMethod(c, "EndAngle");
	  
  }
  Object[] emptyParams = {};
  public int getStartAngle() {
	  return  ((Integer) MethodInvocationManager.invokeMethod(getStartAngleMethod, targetObject, emptyParams));
  }
  public void setStartAngle (int newValue, CommandListener cl) {
	  Object[] params = {newValue};
	   MethodInvocationManager.invokeMethod(frame, targetObject, setStartAngleMethod, params, cl);
  }
  public void setStartAngle (int newValue) {
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(setStartAngleMethod, targetObject, params);
  }
  
  public boolean isStartAngleReadOnly() {
	  return setStartAngleMethod == null;
  }
  
  public int getEndAngle() {
	  return  ((Integer) MethodInvocationManager.invokeMethod(getEndAngleMethod, targetObject, emptyParams));
  }
  public void setEndAngle (int newValue, CommandListener cl) {
	  Object[] params = {newValue};
	   MethodInvocationManager.invokeMethod(frame, targetObject, setEndAngleMethod, params, cl);
  }
  public void setEndAngle (int newValue) {
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(setEndAngleMethod, targetObject, params);
  }
  
  public boolean isEndAngleReadOnly() {
	  return setEndAngleMethod == null;
  }

@Override
public void setStartAngleEndAngle(int newStartAngleValue, int newEndAngleValue) {
	// TODO Auto-generated method stub
	setStartAngle(newStartAngleValue);
	setEndAngle(newEndAngleValue);
	
}

@Override
public void setStartAngleEndAngle(int newStartAngleValue, int newEndAngleValue,
		CommandListener cl) {
	setStartAngle(newStartAngleValue, cl);
	setEndAngle(newEndAngleValue, cl);
	
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