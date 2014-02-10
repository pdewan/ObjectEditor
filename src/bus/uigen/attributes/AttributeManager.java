package bus.uigen.attributes;

import java.util.Vector;

import util.annotations.ComponentWidth;
import util.annotations.MaxValue;
import util.annotations.MinValue;
import util.annotations.SeparateThread;
import util.misc.Common;
import bus.uigen.uiFrame;
import bus.uigen.attributes.AnInheritedAttributeValue.InheritanceKind;
import bus.uigen.introspect.Attribute;
import bus.uigen.introspect.AClassDescriptor;
import bus.uigen.introspect.ClassDescriptorCache;
import bus.uigen.introspect.MethodDescriptorProxy;
import bus.uigen.introspect.ClassDescriptorInterface;
import bus.uigen.introspect.VirtualMethodDescriptor;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class AttributeManager {
  //public final static String  METHOD_PREFIX = "method";
  //public final static String  METHOD_PREFIX = "method:";

  public final static String  METHOD_PREFIX = "";

  private static UIAttributeManager environment = new UIAttributeManager();
  public static UIAttributeManager getEnvironment() {
    return environment;
  }
  public static void setEnvironment(UIAttributeManager env) {
    environment = env;
  }
  public static void clearClassAttributes(String cl) {
	  environment.clearClassAttributes(cl);
  }
// should really pass object to getClassDescriptor
  public static AnInheritedAttributeValue  getInheritedAttribute( MethodProxy method, String attribute, ObjectAdapter objectAdapter) {
	  if (method == null) return null;
	  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(method.getDeclaringClass());
	  MethodDescriptorProxy md = cd.getMethodDescriptor(method);
	  if (md == null) { //dynamic method 
		  Object val = 	AttributeNames.getDefault(attribute);		  
		  InheritanceKind inheritanceKind = InheritanceKind.DEFAULT;
		  if (val ==null) {
			  val = 	AttributeNames.getSystemDefault(attribute);
			  inheritanceKind = InheritanceKind.SYSTEM_DEFAULT;
		  }
		  return new AnInheritedAttributeValue (val, inheritanceKind);

		  
		  //return null;
//		  return new AnInheritedAttributeValue (AttributeNames.getSystemDefault(attribute), AnInheritedAttributeValue.InheritanceKind.DEFAULT);
     }
	  //return AttributeManager.getInheritedAttribute(null, md, attribute, null);
	  return AttributeManager.getInheritedAttribute(null, md, attribute, objectAdapter);
  }
public static AnInheritedAttributeValue  getInheritedAttribute(uiFrame theFrame, MethodDescriptorProxy md, String attribute, ObjectAdapter objectAdapter) {
	  //Object retVal = md.getValue(attribute);
	  Object retVal = null;
	  
	  if (md == null) retVal = null;
	  else {
		  if (objectAdapter != null) {
			   retVal = objectAdapter.getMultiLevelPathAttributeOfMethod(METHOD_PREFIX + md.getName(), attribute);
		  }
		  if (retVal == null)  
		  retVal = AClassDescriptor.getMethodAttribute(md, attribute);
	  //Object retVal = getMethodAttribute(md, attribute);
	  if (retVal != null) return new AnInheritedAttributeValue (retVal, AnInheritedAttributeValue.InheritanceKind.INSTANCE);
	 //Class c = getMostSpecificType( VirtualMethodDescriptor.getVirtualMethod(md));
	  //ViewInfo cd = ClassDescriptorCache.getClassDescriptor(c);
	  //retVal =  cd.getAttribute(attribute);
	  if (objectAdapter != null) {
	  Object realObject = objectAdapter.getRealObject();
	  if (realObject != null) {
		  ClassDescriptorInterface  cd = ClassDescriptorCache.getClassDescriptor(realObject.getClass()); 
			 retVal = cd.getMethodAttribute(md.getName(), attribute);
			 if (retVal != null)
				 return new AnInheritedAttributeValue (retVal, AnInheritedAttributeValue.InheritanceKind.INSTANCE);
				 	
	  }
	  }
	  if (attribute.equals(AttributeNames.VISIBLE))
		  attribute = AttributeNames.METHODS_VISIBLE;
	  retVal = AttributeManager.getInheritedClassAttribute(VirtualMethodDescriptor.getVirtualMethod(md), attribute, null);
	  if (retVal != null)
		  return  new AnInheritedAttributeValue (retVal, AnInheritedAttributeValue.InheritanceKind.CLASS);;
	  
	  if (theFrame != null) //{
			retVal = theFrame.getDefaultAttribute(attribute);
		if (retVal != null)
			return  new AnInheritedAttributeValue (retVal, AnInheritedAttributeValue.InheritanceKind.FRAME_DEFAULT);
	  //}
	  }
	  retVal = AttributeNames.getDefault(attribute);
		if (retVal != null)
			return  new AnInheritedAttributeValue (retVal, AnInheritedAttributeValue.InheritanceKind.DEFAULT);
		else
			return new AnInheritedAttributeValue (AttributeNames.getSystemDefault(attribute), AnInheritedAttributeValue.InheritanceKind.SYSTEM_DEFAULT);
		/*
	  retVal = attribute
	  return cd.getAttribute(attribute);
	  */
	  
  }
public static AnInheritedAttributeValue  getInheritedAttribute(MethodDescriptorProxy md, String attribute, ObjectAdapter objectAdapter) {
	  //return getInheritedAttribute(null, md, attribute, null);
	  return getInheritedAttribute(null, md, attribute, objectAdapter);
  }
public static AnInheritedAttributeValue  getInheritedAttribute(Object object, MethodProxy method, String attribute, ObjectAdapter objectAdapter) {
	  if (method == null) {
		  return null;
	  }
	  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(method.getDeclaringClass(), object);
	  MethodDescriptorProxy md = cd.getMethodDescriptor(method);
	  if (md == null) //dynamic method
		  return null;
	  return getInheritedAttribute(null, md, attribute, null);
  }
public static Object  getInheritedAttributeValue(Object object, MethodProxy method, String attribute, ObjectAdapter objectAdapter) {
	  AnInheritedAttributeValue inheritedObject = getInheritedAttribute(object, method, attribute, null);
	  if (inheritedObject != null)
		  return inheritedObject.getValue();
	  else
		  return null;
}
public static AnInheritedAttributeValue  getInheritedAttribute(Object object, MethodDescriptorProxy md, String attribute, ObjectAdapter objectAdapter) {
	  
	 
	  if (md == null) //dynamic method
		  return null;
	  return getInheritedAttribute(null, md, attribute, objectAdapter);
}
public static Object  getInheritedAttributeValue(Object object, MethodDescriptorProxy md, String attribute, ObjectAdapter objectAdapter) {
	  AnInheritedAttributeValue inheritedObject = getInheritedAttribute(object, md, attribute, objectAdapter);
	  if (inheritedObject != null)
		  return inheritedObject.getValue();
	  else
		  return null;
}
public static Object  getInheritedAttributeValue( MethodDescriptorProxy method, String attribute, ObjectAdapter objectAdapter) {
	  //AnInheritedAttributeValue inheritedValue = getInheritedAttribute(method, attribute, null);
	  AnInheritedAttributeValue inheritedValue = getInheritedAttribute(method, attribute, objectAdapter);

	  if (inheritedValue == null)
		  return null;
	  else 
		  return inheritedValue.getValue();
  }
public static Object  getInheritedAttributeValue( MethodProxy method, String attribute, ObjectAdapter objectAdapter) {
	  AnInheritedAttributeValue inheritedValue = getInheritedAttribute(method, attribute, null);
	  if (inheritedValue == null)
		  return null;
	  else 
		  return inheritedValue.getValue();
  }
public static Object getAttribute (ClassProxy c, String attribute) {
	  if (c == null) return null;
	  ClassDescriptorInterface cd = ClassDescriptorCache.getClassDescriptor(c);
	  return cd.getAttribute(attribute);
  }
//  public ClassDescriptorCustomizer getClassDescriptorCustomizer(uiObjectAdapter theAdapter) {
//		 return new ClassDescriptorCustomizer(this, theAdapter);
//		  //return customizer;
//	  }
  
public static Object getInheritedClassAttribute (MethodProxy m, String attribute, ObjectAdapter objectAdapter) {
	  Object retVal;
	  // newly added untested code
	  if (objectAdapter != null) {
		  retVal = objectAdapter.getMultiLevelPathAttributeOfMethod(METHOD_PREFIX + m.getName(), attribute);
		  if (retVal != null)
			  return retVal;
	  }
	  //Class methodClass = 
	  //if (methodClass != null)
	  retVal = getAttribute(m.getDynamicClass(), attribute);
	  if (retVal != null)
		  return retVal;
	  retVal = AttributeManager.getInheritedClassAttribute(m.getDeclaringClass(), m, attribute, null); 
	  if (retVal != null)
		  return retVal;
	  ClassProxy[] interfaces = m.getDeclaringClass().getInterfaces();	  
	  for (int i = 0; i < interfaces.length; i++) {
		  if (AClassDescriptor.contains (interfaces[i], m))
			  retVal = AttributeManager.getInheritedClassAttribute(interfaces[i], m, attribute, null); 
		  if (retVal != null)
			  return retVal;
	  }
	  return retVal;
	 
  }
public static Object getInheritedClassAttribute (ClassProxy c, MethodProxy m, String attribute, ObjectAdapter objectAdapter) {
	Object retVal; 
	if (c == null) return null;
	// newly added untested code
	if (objectAdapter != null) {
		  retVal = objectAdapter.getMultiLevelPathAttributeOfMethod(METHOD_PREFIX + m.getName(), attribute);
		  if (retVal != null)
			  return retVal;
	  }
	  if (! AClassDescriptor.contains(c, m) && m.isMethod()) return null;
	  ClassDescriptorInterface viewInfo = ClassDescriptorCache.getClassDescriptor(c);
	  
	  //Object retVal = viewInfo.getMethodAttribute(m, attribute);
	  retVal = viewInfo.getMethodAttribute(m, attribute);
	  if (retVal != null)
		  return retVal;
	  
	  //Object retVal =  getAttribute(c, attribute); 
	  // should not inherit clsase's attribute directly
	  //retVal =  getAttribute(c, attribute); 
	  if (retVal != null)
		  return retVal;		
	  return getInheritedClassAttribute (c.getSuperclass(), m, attribute, objectAdapter);	  
  }
public static boolean getMethodVisible(MethodDescriptorProxy md, ObjectAdapter objectAdapter) {	
	Object visible = getInheritedAttributeValue(md, AttributeNames.VISIBLE, objectAdapter);
	 // Object visible = md.getValue(AttributeNames.VISIBLE);
	  if (visible == null)
		  return true;
	  else
		  return (Boolean) visible;
	  
  }
}
