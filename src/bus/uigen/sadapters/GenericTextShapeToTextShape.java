package bus.uigen.sadapters;
import java.awt.Color;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import shapes.AttributedShape;
import shapes.BoundedShape;
import shapes.TextShape;

import bus.uigen.uiFrame;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.introspect.IntrospectUtility;
import bus.uigen.loggable.ACompositeLoggable;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.undo.CommandListener;
public class GenericTextShapeToTextShape extends 
	//GenericXYShapeToShape 
	GenericBoundedShapeToBoundedShape
	implements ConcreteTextShape {
	transient MethodProxy getTextMethod = null;  transient MethodProxy setTextMethod = null;
  transient MethodProxy getAttributedStringMethod = null;
  public static String ATTRIBUTED_STRING = "AttributedString";
	
	public GenericTextShapeToTextShape (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericTextShapeToTextShape () {
	}
	public void setMethods(ClassProxy c) {		super.setMethods(c);		setTextMethods(c);
		setAttributedStringMethods(c);
	}
  
  public void setTextMethods (ClassProxy c) {	  	  getTextMethod = IntrospectUtility.getGetStringMethod(c, "Text");	  setTextMethod = IntrospectUtility.getSetStringMethod(c, "Text");
	  
  }
  
   public void setAttributedStringMethods (ClassProxy c) {
	  
	   getAttributedStringMethod = IntrospectUtility.getGetMethod(c, ATTRIBUTED_STRING, AClassProxy.classProxy(AttributedString.class));	
	  
  }
  Object[] emptyParams = {};
  public String getText() {
	  if (targetObject instanceof TextShape) {
		  return ((TextShape) targetObject).getText();
		   
	  }
	  if (getTextMethod == null)
		  return "";
	  return  ((String) MethodInvocationManager.invokeMethod(getTextMethod, targetObject, emptyParams));
  }
  public void setText (String newValue, CommandListener cl) {
	  Object[] params = {newValue};
	   MethodInvocationManager.invokeMethod(frame, targetObject, setTextMethod, params, cl);
  }
  public void setText (String newValue) {
	  if (targetObject instanceof TextShape) {
		   ((TextShape) targetObject).setText(newValue);
		   return;
	  }
	  Object[] params = {new String(newValue)};
	  MethodInvocationManager.invokeMethod(setTextMethod, targetObject, params);
  }
  
  public boolean isTextReadOnly() {
	  return setTextMethod == null;
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

@Override
public boolean hasAttributedString() {
	return getAttributedStringMethod != null;
}

@Override
public AttributedString getAttributedString() {
	if (!hasAttributedString()) return null;
	  //Object colorObject = uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
	  //String colorString = colorObject.toString();
//	  Object retVal = MethodInvocationManager.invokeMethod(getAttributedStringMethod, targetObject, emptyParams);
	  Object retVal = null;
	  if (targetObject instanceof AttributedShape) {
		  retVal =  ((AttributedShape) targetObject).getPaint();
	  } else {
		  retVal = MethodInvocationManager.invokeMethod(getAttributedStringMethod, targetObject, emptyParams);
	  }
	  AttributedString attributedString = null;	  
	  if (retVal instanceof AttributedString)
		  attributedString = (AttributedString) retVal;
	  if (retVal instanceof ACompositeLoggable) {
		  attributedString = (AttributedString) ((ACompositeLoggable) retVal).getRealObject();
	  }
	  return attributedString;
	
}
}