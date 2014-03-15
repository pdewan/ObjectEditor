package bus.uigen.introspect;

import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.beans.FeatureDescriptor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

import bus.uigen.controller.models.ClassDescriptorCustomizer;
import bus.uigen.oadapters.ObjectAdapter;
import bus.uigen.reflect.ClassProxy;
import bus.uigen.reflect.MethodProxy;

public class SimpleViewInfo /*extends SimpleBeanInfo*/ implements ClassDescriptorInterface {
  public FieldDescriptorProxy[] getFieldDescriptors() {return null;}
  public FieldDescriptorProxy[] getConstantDescriptors() {return null;}
  public ConstructorDescriptorProxy[] getConstructorDescriptors() {return null;}

  public FeatureDescriptorProxy[] getFeatureDescriptors() {
    FeatureDescriptorProxy[] fields = getFieldDescriptors();
    FeatureDescriptorProxy[] props = getPropertyDescriptors();
    if (fields == null)
      return props;
    if (props == null)
      return fields;
    FeatureDescriptorProxy[] f = new AFeatureDescriptorProxy[fields.length+props.length];
    int j=0;
    for (int i=0;i<fields.length; i++)
      f[j++] = fields[i];
    for (int i=0; i<props.length; i++)
      f[j++] = props[j];
    return f;
  }  public void setAttribute(String attr, Object val) {}  public void setPropertyAttribute(String property, String attr, Object val) {}
  public void setMethodAttribute(String property, String attr, Object val) {}
  public Object getPrototypeObject() {return null;}
  public void setPrototypeObject (Object newVal) {};
  public MethodProxy[] getVirtualMethods() { return null;}
  //public void addProperties (Vector<PropertyDescriptor> newElements) {}
  public void addMethods(Vector<VirtualMethodDescriptor> newElements) {}

@Override
public Object getAttribute(String attr) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public BeanDescriptorProxy getBeanDescriptor() {
	// TODO Auto-generated method stub
	return null;
}
//@Override
//public ClassDescriptorCustomizer getClassDescriptorCustomizer() {
//	// TODO Auto-generated method stub
//	return null;
//}
//@Override
//public ClassDescriptorCustomizer getClassDescriptorCustomizer(
//		uiObjectAdapter selectedAdapter) {
//	// TODO Auto-generated method stub
//	return null;
//}
@Override
public MethodDescriptorProxy getDynamicCommandsMethodDescriptor() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public Vector<VirtualMethodDescriptor> getDynamicMethodDescriptors() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public PropertyDescriptorProxy[] getIndexOrKeyPropertyDescriptors() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public Object getMethodAttribute(String method, String attribute) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodDescriptorProxy getMethodDescriptor(String name) {
	// TODO Auto-generated method stub
	return null;
}
/*
@Override
public MethodDescriptorProxy getMethodDescriptor(Method method) {
	// TODO Auto-generated method stub
	return null;
}
*/
@Override
public MethodDescriptorProxy getMethodDescriptor(MethodProxy method) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodDescriptorProxy[] getMethodDescriptors() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public Object getPropertyAttribute(String property, String attribute) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public PropertyDescriptorProxy getPropertyDescriptor(String name) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public PropertyDescriptorProxy[] getPropertyDescriptors() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodProxy[] getVirtualConstructors() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public boolean isDynamic(MethodDescriptorProxy md) {
	// TODO Auto-generated method stub
	return false;
}
@Override
public void setAttributeOfAllMethods(String attribute, Object value) {
	// TODO Auto-generated method stub
	
}
@Override
public void setAttributeOfAllProperties(String attribute, Object value) {
	// TODO Auto-generated method stub
	
}
@Override
public void setMethodDescriptors(MethodDescriptorProxy[] newVal) {
	// TODO Auto-generated method stub
	
}
@Override
public void setMethodDescriptors(Vector<MethodDescriptorProxy> newVal) {
	// TODO Auto-generated method stub
	
}
@Override
public void setPropertyDescriptors(PropertyDescriptorProxy[] newVal) {
	// TODO Auto-generated method stub
	
}
@Override
public void setPropertyDescriptors(Vector<PropertyDescriptorProxy> newVal) {
	// TODO Auto-generated method stub
	
}
@Override
public void addProperties(Vector<PropertyDescriptorProxy> newElements) {
	// TODO Auto-generated method stub
	
}
@Override
public void setMethodAttribute(MethodProxy method, String attr, Object val) {
	// TODO Auto-generated method stub
	
}
@Override
public Object getMethodAttribute(MethodProxy m, String name) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public ClassProxy getRealClass() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodProxy getAddPropertyChangeListenerMethod() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodProxy getAddHashtableListenerMethod() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodProxy getAddRemotePropertyChangeListenerMethod() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodProxy getAddTableModelListenerMethod() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodProxy getAddTreeModelListenerMethod() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodProxy getAddVectorListenerMethod() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public MethodProxy getAddObserverMethod() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public Hashtable getAttributes() {
	// TODO Auto-generated method stub
	return null;
}
//@Override
//public PropertyDescriptorProxy[] getPropertyDesciptors() {
//	// TODO Auto-generated method stub
//	return null;
//}
@Override
public MethodProxy getAddRefresherMethod() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public String getName() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public void initPropertyMergedAttributes(PropertyDescriptorProxy pd) {
	// TODO Auto-generated method stub
	
}
@Override
public Object getNonDefaultMethodAttribute(String method, String attribute) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public boolean isComponentsVisible() {
	// TODO Auto-generated method stub
	return false;
}
@Override
public void setComponentsVisible(boolean componentsVisible) {
	// TODO Auto-generated method stub
	
}


}
