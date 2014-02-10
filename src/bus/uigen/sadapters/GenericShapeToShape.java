package bus.uigen.sadapters;
import java.util.*;import java.lang.reflect.*;import java.beans.*;

import bus.uigen.editors.*;import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;
import bus.uigen.reflect.local.AClassProxy;
import bus.uigen.shapes.OEShapeModel;
import bus.uigen.undo.*;import bus.uigen.*;
import bus.uigen.introspect.*;import bus.uigen.attributes.AttributeNames;
import bus.uigen.controller.MethodInvocationManager;
import bus.uigen.loggable.ACompositeLoggable;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;

import logging.levy.logging.experiments.apps.shapes.shapes.ShapeModel;
import shapes.AttributedShape;
import util.annotations.StructurePatternNames;
public abstract class GenericShapeToShape extends BeanToRecord implements ConcreteShape {
  /*
  transient VirtualMethod getXMethod = null;  transient VirtualMethod setXMethod = null;       transient VirtualMethod getYMethod = null;  transient VirtualMethod setYMethod = null; 
  */
  transient MethodProxy getColorMethod = null;  transient MethodProxy setColorMethod = null;
  transient MethodProxy getFilledMethod = null;
  transient MethodProxy setFilledMethod = null;
  transient MethodProxy getZAxisMethod = null;
  transient MethodProxy setZAxisMethod = null;
  transient MethodProxy getGradientPaintMethod = null;
  transient MethodProxy setGradientPaintMethod = null;
  transient MethodProxy get3DMethod = null;
  transient MethodProxy set3DMethod = null;
  transient MethodProxy getRoundedMethod = null;
  transient MethodProxy setRoundedMethod = null;
  transient MethodProxy getRaisedMethod = null;
  transient MethodProxy setRaisedMethod = null;
  transient MethodProxy getBasicStrokeMethod = null;
  transient MethodProxy setBasicStrokeMethod = null;
  transient MethodProxy getFontMethod = null;
  transient MethodProxy setFontMethod = null;
  transient MethodProxy getFontSizeMethod = null;
  transient MethodProxy setFontSizeMethod = null;
  boolean isAttributedShape;
	
	public GenericShapeToShape (Object theTargetObject, uiFrame theFrame) {		init(theTargetObject, theFrame );
	}
		public GenericShapeToShape () {
	}
	public void setMethods(ClassProxy c) {	super.setMethods(c);
	  //setLocationMethods(c);
//	  setAttributeMethods(c);
	  setAttributeMethodsFromProperties(c);
  }
	
 public static String Z_AXIS = "ZIndex";
 public static String COLOR = "Color";
 public static String STROKE = "Stroke";
 public static String PAINT = "Paint";
 public static String FILLED = "Filled";
 public static String THREE_D = "3D";
 public static String ROUNDED = "Rounded";
 public static String FONT = "Font";
 public static String FONT_SIZE = "FontSize";
    public void setAttributeMethods(ClassProxy c) {
	  isAttributedShape = AClassProxy.classProxy(AttributedShape.class).isAssignableFrom(c);
	  if (isAttributedShape)
		  return;
	  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
	  getColorMethod = IntrospectUtility.getGetMethod(c, COLOR, c.colorClass());
	  setColorMethod = IntrospectUtility.getSetMethod(c, COLOR, c.colorClass());
	  getGradientPaintMethod = IntrospectUtility.getGetMethod(c, PAINT, c.paintClass());	  
	  setGradientPaintMethod = IntrospectUtility.getSetMethod(c, PAINT, c.paintClass());
	  getBasicStrokeMethod = IntrospectUtility.getGetMethod(c, STROKE, c.strokeClass());	  
	  setBasicStrokeMethod = IntrospectUtility.getSetMethod(c, STROKE, c.strokeClass());
	  getFontMethod = IntrospectUtility.getGetMethod(c, FONT, c.fontClass());	  
	  setFontMethod = IntrospectUtility.getSetMethod(c, FONT, c.fontClass());
	  getFontSizeMethod = IntrospectUtility.getGetMethod(c, FONT_SIZE, c.integerType());	  
	  setFontSizeMethod = IntrospectUtility.getSetMethod(c, FONT_SIZE, c.integerType());
	  getFilledMethod = IntrospectUtility.getGetOrIsBooleanMethod(c, FILLED);
	  setFilledMethod = IntrospectUtility.getSetBooleanMethod(c, FILLED);
	  getZAxisMethod = IntrospectUtility.getGetIntMethod(c, Z_AXIS);
	  setZAxisMethod = IntrospectUtility.getSetIntMethod(c, Z_AXIS);
	  get3DMethod = IntrospectUtility.getGetOrIsBooleanMethod(c, THREE_D);
	  set3DMethod = IntrospectUtility.getSetBooleanMethod(c, THREE_D);
	  getRoundedMethod = IntrospectUtility.getGetOrIsBooleanMethod(c, ROUNDED);
	  setRoundedMethod = IntrospectUtility.getSetBooleanMethod(c, ROUNDED);
  }
  public void setAttributeMethodsFromProperties(ClassProxy c) {
//	  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
	  isAttributedShape = AClassProxy.classProxy(AttributedShape.class).isAssignableFrom(c);
	  if (isAttributedShape)
		  return;
	  getColorMethod = getReadMethod(COLOR, c.colorClass());
	  setColorMethod = getWriteMethod(COLOR, c.colorClass());
	  getGradientPaintMethod = getReadMethod(PAINT, c.paintClass());	  
	  setGradientPaintMethod =  getWriteMethod(PAINT, c.paintClass());
	  getBasicStrokeMethod = getReadMethod(STROKE, c.strokeClass());	  
	  setBasicStrokeMethod = getWriteMethod(STROKE, c.strokeClass());	  
	  getFontMethod = getReadMethod(FONT, c.fontClass());	  
	  setFontMethod = getWriteMethod(FONT, c.fontClass());
	  getFontSizeMethod = getReadMethod(FONT_SIZE, c.integerType());	  
	  setFontSizeMethod = getWriteMethod(FONT_SIZE, c.integerType());	
	  getFilledMethod = getReadMethod( FILLED, c.booleanType());
	  setFilledMethod = getWriteMethod( FILLED, c.booleanType());
	  getZAxisMethod = getReadMethod(Z_AXIS, c.integerType());
	  setZAxisMethod = getWriteMethod(Z_AXIS, c.integerType());
	  get3DMethod = getReadMethod(THREE_D, c.booleanType());
	  set3DMethod = getWriteMethod(THREE_D, c.booleanType());
	  getRoundedMethod = getReadMethod(ROUNDED, c.booleanType());
	  setRoundedMethod = getWriteMethod(ROUNDED, c.booleanType());
	 

//	  isAttributedShape = AClassProxy.classProxy(ShapeModel.class).isAssignableFrom(c);

  }
  /*
  public void setLocationMethods(Class c) {	  getXMethod = uiBean.getGetIntMethod(c, "X");	  	  getYMethod = uiBean.getGetIntMethod(c, "Y");	  setXMethod = uiBean.getSetIntMethod(c, "X");	  setYMethod = uiBean.getSetIntMethod(c, "Y");
	  
  }
  
  Object[] emptyParams = {};
  public int getX() {
	  return  ((Integer) uiMethodInvocationManager.invokeMethod(getXMethod, targetObject, emptyParams)).intValue();
  }
  public void setX (int newValue, CommandListener cl) {
	  Object[] params = {new Integer(newValue)};
	   uiMethodInvocationManager.invokeMethod(frame, targetObject, setXMethod, params, cl);
  }
  public void setX (int newValue) {
	  Object[] params = {new Integer(newValue)};
	  uiMethodInvocationManager.invokeMethod(setXMethod, targetObject, params);
  }
  public int getY() {
	  return ((Integer) uiMethodInvocationManager.invokeMethod(getYMethod, targetObject, emptyParams)).intValue();
  }
  public void setY (int newValue){
	  Object[] params = {new Integer(newValue)};
	   uiMethodInvocationManager.invokeMethod(setYMethod, targetObject, params);
  }
  public void setY (int newValue, CommandListener cl) {
	   Object[] params = {new Integer(newValue)};
	  uiMethodInvocationManager.invokeMethod(frame, targetObject, setYMethod, params, cl);
  }
  */
  public Color getColor() {
	  if (!hasColor()) return null;
	  //Object colorObject = uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
	  //String colorString = colorObject.toString();
	  Object colorObject = null;
	  if (targetObject instanceof AttributedShape) {
		  colorObject =  ((AttributedShape) targetObject).getColor();
	  } else {
	  colorObject = MethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
	  }
	  Color color = null;	  
	  if (colorObject instanceof Color)
		  color = (Color) colorObject;
	  if (colorObject instanceof ACompositeLoggable) {
		  color = (Color) ((ACompositeLoggable) colorObject).getRealObject();
	  }
	  return color;
	  //return (Color) uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
  }
  @Override
  public Stroke getBasicStroke() {
	if (!hasBasicStroke()) return null;
	  //Object colorObject = uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
	  //String colorString = colorObject.toString();
//	  Object basicStrokeObject = MethodInvocationManager.invokeMethod(getBasicStrokeMethod, targetObject, emptyParams);
	  Object basicStrokeObject = null;
	  if (targetObject instanceof AttributedShape) {
		  basicStrokeObject =  ((AttributedShape) targetObject).getStroke();
	  } else {
		  basicStrokeObject = MethodInvocationManager.invokeMethod(getBasicStrokeMethod, targetObject, emptyParams);
	  }
	  BasicStroke basicStroke = null;	  
	  if (basicStrokeObject instanceof BasicStroke)
		  basicStroke = (BasicStroke) basicStrokeObject;
	  if (basicStrokeObject instanceof ACompositeLoggable) {
		  basicStroke = (BasicStroke) ((ACompositeLoggable) basicStrokeObject).getRealObject();
	  }
	  return basicStroke;
	  //return (Color) uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
  }
  @Override
  public Font getFont() {
	  if (!hasFont()) return null;
	  //Object colorObject = uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
	  //String colorString = colorObject.toString();
//	  Object fontObject = MethodInvocationManager.invokeMethod(getFontMethod, targetObject, emptyParams);
	  Object fontObject = null;
	  if (targetObject instanceof AttributedShape) {
		  fontObject =  ((AttributedShape) targetObject).getFont();
	  } else {
		  fontObject = MethodInvocationManager.invokeMethod(getFontMethod, targetObject, emptyParams);
	  }
	  Font font = null;	  
	  if (fontObject instanceof Font)
		  font = (Font) fontObject;
	  if (fontObject instanceof ACompositeLoggable) {
		  font = (Font) ((ACompositeLoggable) fontObject).getRealObject();
	  }
	  return font;
	  //return (Color) uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
  }
  @Override
  public int getFontSize() {
	  if (!hasFontSize()) return -1;
	  //Object colorObject = uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
	  //String colorString = colorObject.toString();
	  if (targetObject instanceof AttributedShape) {
		  return ((AttributedShape) targetObject).getFontSize();
	  }
	 return (Integer) MethodInvocationManager.invokeMethod(getFontSizeMethod, targetObject, emptyParams);
	 
	  
	  //return (Color) uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
  }
  @Override
  public Paint getGradientPaint() {
	  if (!hasGradientPaint()) return null;
	  //Object colorObject = uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
	  //String colorString = colorObject.toString();
//	  Object gradientPaintObject = MethodInvocationManager.invokeMethod(getGradientPaintMethod, targetObject, emptyParams);
	  Object gradientPaintObject = null;
	  if (targetObject instanceof AttributedShape) {
		  gradientPaintObject =  ((AttributedShape) targetObject).getPaint();
	  } else {
		  gradientPaintObject = MethodInvocationManager.invokeMethod(getGradientPaintMethod, targetObject, emptyParams);
	  }
	  GradientPaint gradientPaint = null;	  
	  if (gradientPaintObject instanceof GradientPaint)
		  gradientPaint = (GradientPaint) gradientPaintObject;
	  if (gradientPaintObject instanceof ACompositeLoggable) {
		  gradientPaint = (GradientPaint) ((ACompositeLoggable) gradientPaintObject).getRealObject();
	  }
	  return gradientPaint;
	  //return (Color) uiMethodInvocationManager.invokeMethod(getColorMethod, targetObject, emptyParams);
  }
  public void setColor (Color newValue) {
	  if (targetObject instanceof AttributedShape) {
		  ((AttributedShape) targetObject).setColor(newValue);
		  return;
	  
	  }
	  Object[] params = {newValue};
	  MethodInvocationManager.invokeMethod(setColorMethod, targetObject, params);
  }
  
  public void setColor (int newValue, CommandListener cl) {
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(frame, targetObject, setColorMethod, params, cl);
  }
  /*
  public boolean isXReadOnly() {
	  return setXMethod == null;
  }
  
  public boolean isYReadOnly() {
	  return setYMethod == null;
  }
  */
  @Override
  public boolean hasColor()   {
	  return isAttributedShape || getColorMethod != null;
  }
  @Override
  public boolean hasFont()   {
	  return isAttributedShape || getFontMethod != null;
  }
  @Override
  public boolean hasFontSize()   {
	  return isAttributedShape || getFontSizeMethod != null;
  }
  @Override
  public boolean hasBasicStroke()   {
	  return isAttributedShape || getBasicStrokeMethod != null;
  }
  @Override
  public boolean has3D()   {
	  return isAttributedShape || get3DMethod != null;
  }
  @Override
  public boolean hasRounded()   {
	  return isAttributedShape ||getRoundedMethod != null;
  }
  @Override
  public boolean hasGradientPaint()   {
	  return isAttributedShape || getGradientPaintMethod != null;
  }
  
  public boolean isColorReadOnly()   {
	  return isAttributedShape || setColorMethod == null;
  }
  AttributedShape getAttributedShape() {
	  return (AttributedShape) targetObject;
  }
  @Override
  public boolean get3D() {
	  if (isAttributedShape)
		  return getAttributedShape().is3D();
	  return (Boolean) MethodInvocationManager.invokeMethod(get3DMethod, targetObject, emptyParams);
  }
  @Override
  public boolean getRounded() {
	  if (isAttributedShape)
		  return getAttributedShape().isRounded();
	  return (Boolean) MethodInvocationManager.invokeMethod(getRoundedMethod, targetObject, emptyParams);
  }
  @Override
  public boolean getFilled() {
	  if (isAttributedShape)
		  return getAttributedShape().isFilled();
	  return (Boolean) MethodInvocationManager.invokeMethod(getFilledMethod, targetObject, emptyParams);
  }
  public void setFilled(boolean newValue) {
	  if (isAttributedShape) {
		  getAttributedShape().setFilled(newValue);
		  return;
	  }
	  Object[] params = {newValue};
	  MethodInvocationManager.invokeMethod(setFilledMethod, targetObject, params);
  }
  
  public void setFilled (boolean newValue, CommandListener cl) {
	  Object[] params = {newValue};
	  MethodInvocationManager.invokeMethod(frame, targetObject, setFilledMethod, params, cl);
  }
  public boolean hasFilled()   {
	  return isAttributedShape || getFilledMethod != null;
  }
  
  public boolean isFilledReadOnly()   {
	  return !isAttributedShape && setFilledMethod == null;
  }
  
  public int getZIndex() {
	  if (isAttributedShape) 
		  return getAttributedShape().getZIndex();
	  return (Integer) MethodInvocationManager.invokeMethod(getZAxisMethod, targetObject, emptyParams);
  }
	  
  public void setZIndex(int newValue) {
	  if (isAttributedShape) {
		   getAttributedShape().setZIndex(newValue);
	  }
	  Object[] params = {newValue};
	  MethodInvocationManager.invokeMethod(setZAxisMethod, targetObject, params);
  }
  
  public void setZIndex (int newValue, CommandListener cl) {
	  Object[] params = {new Integer(newValue)};
	  MethodInvocationManager.invokeMethod(frame, targetObject, setZAxisMethod, params, cl);
  }
  public boolean hasZIndex()   {
	  return isAttributedShape || getZAxisMethod != null;
  }
  
  public boolean isZIndexReadOnly()   {
	  return setZAxisMethod == null;
  }
  
    public static String SHAPE = "Shape";
	public String programmingPatternKeyword() {
		return  AbstractConcreteType.PROGRAMMING_PATTERN + AttributeNames.KEYWORD_SEPARATOR + SHAPE;
	}
	public String typeKeyword() {
		return ObjectEditor.TYPE_KEYWORD + AttributeNames.KEYWORD_SEPARATOR + SHAPE ;
	}
	public String applicationKeyword() {
		return ObjectEditor.GRAPHICS_KEYWORD ;
	}
	public String getPatternName() {
		if (patternName != null)	
			return patternName;
		else return super.getPatternName();
	}
	
}